package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.persistence.dao.RequestForVisitDAO;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PhotoUCCImpl implements PhotoUCC {

  @Inject
  private PhotoDAO photoDAO;
  @Inject
  private FurnitureDAO furnitureDAO;
  @Inject
  private FurnitureTypeDAO furnitureTypeDAO;
  @Inject
  private RequestForVisitDAO requestDAO;
  @Inject
  private ConnectionDalServices dalServices;

  /**
   * get all the photos that are visible on the home page's carousel.
   *
   * @return a list containing all the photos that are visible on the home page's carousel.
   */
  @Override
  public List<PhotoDTO> getAllHomePageVisiblePhotos() {
    List<PhotoDTO> res;
    try {
      dalServices.startTransaction();
      res = photoDAO.getAllHomePageVisiblePhotos();
      for (PhotoDTO photo : res) {
        FurnitureDTO furnitureDTO = furnitureDAO.findById(photo.getFurnitureId());
        furnitureDTO.setPhotos(new ArrayList<>());
        photo.setFurniture(furnitureDTO);
        photo.getFurniture().setType(furnitureTypeDAO.findById(photo.getFurniture().getTypeId())
            .getTypeName());
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  /**
   * Add a photo.
   *
   * @param furnitureId id of the furniture linked to the picture.
   * @param source      the base64 img.
   * @return the photoDTO.
   */
  @Override
  public PhotoDTO add(Integer furnitureId, String source) {
    PhotoDTO res;
    try {
      dalServices.startTransaction();
      furnitureDAO.findById(furnitureId);
      int id = photoDAO.insert(furnitureId, source);
      res = photoDAO.findById(id);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  /**
   * updates one photo's display flags (isVisible & isOnHomePage) by id.
   *
   * @param id           : the photo's id
   * @param isVisible    : the photo's new visibility flag.
   * @param isOnHomePage : the photo's new isOnHomePage flag.
   * @return the modified resource as PhotoDTO
   */
  @Override
  public PhotoDTO patchDisplayFlags(int id, boolean isVisible, boolean isOnHomePage) {
    PhotoDTO res;
    try {
      dalServices.startTransaction();
      PhotoDTO foundDto = photoDAO.findById(id);
      if (!isVisible && isOnHomePage) {
        throw new ConflictException(
            "Error: impossible flag configuration "
                + "(non-visible photos cannot be displayed on the homepage)");
      }
      //TODO: verify origin
      foundDto.setVisible(isVisible);
      foundDto.setOnHomePage(isOnHomePage);

      res = photoDAO.updateDisplayFlags(foundDto);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  /**
   * Finds the favourite photo for a specific furniture id.
   *
   * @param furnitureId : furniture id
   * @return favourite photo as PhotoDTO
   */
  @Override
  public PhotoDTO getFavourite(int furnitureId) {
    PhotoDTO res;
    try {
      dalServices.startTransaction();
      FurnitureDTO furnitureDTO = furnitureDAO.findById(furnitureId);
      if (furnitureDTO.getFavouritePhotoId() == null) {
        throw new NotFoundException(
            "Error : no favourite photo for furniture " + furnitureDTO.getFurnitureId());
      }
      res = photoDAO.findById(furnitureDTO.getFavouritePhotoId());
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  /**
   * Finds all photos for a specific furniture id.
   *
   * @param furnitureId : furniture id
   * @return list of PhotoDTO
   */
  @Override
  public List<PhotoDTO> getAllForFurniture(int furnitureId) {
    List<PhotoDTO> res;
    try {
      dalServices.startTransaction();
      furnitureDAO.findById(furnitureId); //check for not found
      res = photoDAO.findAllByFurnitureId(furnitureId);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  /**
   * Finds all request photos for a specific furniture id. (verifies if the current user is the
   * owner of the request)
   *
   * @param currentUserDTO : dto containing current user information.
   * @param furnitureId    : furniture id.
   * @return PhotoDTO list
   */
  @Override
  public List<PhotoDTO> getRequestPhotos(UserDTO currentUserDTO, int furnitureId) {
    List<PhotoDTO> res;
    try {
      dalServices.startTransaction();
      FurnitureDTO furnitureDTO = furnitureDAO.findById(furnitureId);
      RequestForVisitDTO requestDTO = requestDAO.findById(furnitureDTO.getRequestId());
      int ownerId = requestDTO.getUserId();
      int currentUserId = currentUserDTO.getId();
      if(currentUserId != ownerId) {
        throw new UnauthorizedException("Error : you are not the owner of this request for visit");
      }
      res = photoDAO.findAllRequestPhotosByFurnitureId(furnitureId);
      dalServices.commitTransaction();
    } catch(Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

}
