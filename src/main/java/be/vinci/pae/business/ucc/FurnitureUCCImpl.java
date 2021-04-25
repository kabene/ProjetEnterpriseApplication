package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.pojos.FurnitureStatus;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.*;
import jakarta.inject.Inject;
import java.util.List;

public class FurnitureUCCImpl implements FurnitureUCC {

  @Inject
  private FurnitureDAO furnitureDAO;
  @Inject
  private UserDAO userDAO;
  @Inject
  private PhotoDAO photoDAO;
  @Inject
  private FurnitureTypeDAO furnitureTypeDAO;
  @Inject
  private OptionDAO optionDAO;
  @Inject
  private RequestForVisitDAO requestForVisitDAO;
  @Inject
  private ConnectionDalServices dalServices;


  /**
   * get the piece of furniture searched by its id.
   *
   * @param id the furniture id.
   * @return Furniture represented bu FurnitureDTO.
   */
  @Override
  public FurnitureDTO getOne(int id) {
    FurnitureDTO res;

    try {
      dalServices.startTransaction();
      res = furnitureDAO.findById(id);
      completeFurnitureDTO(res);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  /**
   * get all the furniture.
   *
   * @return list contains furniture.
   */
  @Override
  public List<FurnitureDTO> getAll() {
    List<FurnitureDTO> dtos;
    try {
      dalServices.startTransaction();
      dtos = furnitureDAO.findAll();
      for (FurnitureDTO dto : dtos) {
        completeFurnitureDTO(dto);
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return dtos;
  }

  /**
   * set the status of the furniture to IN_RESTORATION.
   *
   * @param furnitureId the furniture id.
   * @return the furniture modified.
   */
  @Override
  public FurnitureDTO toRestoration(int furnitureId) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getStatus().equals(FurnitureStatus.ACCEPTED)) {
        throw new ConflictException(
            "The resource cannot change from its current state to the 'in_restoration' state");
      }
      furnitureDTO.setStatus(FurnitureStatus.IN_RESTORATION);
      furnitureDTO = furnitureDAO.updateStatusOnly(furnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  /**
   * set the status of the furniture to AVAILABLE_FOR_SALE and set its selling price.
   *
   * @param furnitureId  the furniture id.
   * @param sellingPrice the selling price of the furniture.
   * @return the furniture modified.
   */
  @Override
  public FurnitureDTO toAvailable(int furnitureId, double sellingPrice) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getStatus().equals(FurnitureStatus.ACCEPTED) && !furnitureDTO.getStatus()
          .equals(FurnitureStatus.IN_RESTORATION)) {
        throw new ConflictException(
            "The resource cannot change from its current state to the 'available_for_sale' state");
      }
      furnitureDTO.setStatus(FurnitureStatus.AVAILABLE_FOR_SALE);
      furnitureDTO.setSellingPrice(sellingPrice);
      furnitureDTO = furnitureDAO.updateToAvailable(furnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  /**
   * set the status of the furniture to WITHDRAWN.
   *
   * @param furnitureId the furniture id.
   * @return the furniture modified.
   */
  @Override
  public FurnitureDTO withdraw(int furnitureId) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      furnitureDTO = furnitureDAO.findById(furnitureId);
      if (!furnitureDTO.getStatus().equals(FurnitureStatus.AVAILABLE_FOR_SALE) && !furnitureDTO.getStatus()
          .equals(FurnitureStatus.IN_RESTORATION)) {
        throw new ConflictException(
            "The resource isn't in a withdrawable state");
      }
      furnitureDTO.setStatus(FurnitureStatus.WITHDRAWN);
      furnitureDTO = furnitureDAO.updateToWithdrawn(furnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  /**
   * updates the favourite photo id of a specific piece of furniture.
   *
   * @param furnitureId : the furniture id
   * @param photoId     : the new favourite photo id
   * @return the updated furniture
   */
  @Override
  public FurnitureDTO updateFavouritePhoto(int furnitureId, int photoId) {
    FurnitureDTO furnitureDTO;
    try {
      dalServices.startTransaction();
      FurnitureDTO foundFurnitureDTO = furnitureDAO.findById(furnitureId);
      PhotoDTO foundPhotoDTO = photoDAO.getPhotoById(photoId);
      if (foundPhotoDTO.getFurnitureId() != foundFurnitureDTO.getFurnitureId()) {
        throw new ConflictException(
            "Error: The photo doesn't belong to the specified piece of furniture");
      }
      foundFurnitureDTO.setFavouritePhoto(foundPhotoDTO);
      foundFurnitureDTO.setFavouritePhotoId(foundPhotoDTO.getPhotoId());

      furnitureDTO = furnitureDAO.updateFavouritePhoto(foundFurnitureDTO);
      completeFurnitureDTO(furnitureDTO);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return furnitureDTO;
  }

  /**
   * Completes the FurnitureDTO given as an argument with it's references in the db.
   *
   * @param dto : the FurnitureDTO to complete
   */
  private void completeFurnitureDTO(FurnitureDTO dto) {
    if (dto.getBuyerId() != null) {
      UserDTO u = userDAO.findById(dto.getBuyerId());
      dto.setBuyer(u);
    }
    if (dto.getSellerId() != null) {
      UserDTO u = userDAO.findById(dto.getSellerId());
      dto.setSeller(u);
    }
    if (dto.getFavouritePhotoId() != null) {
      PhotoDTO favPhoto = photoDAO.getPhotoById(dto.getFavouritePhotoId());
      dto.setFavouritePhoto(favPhoto);
    }
    if (dto.getStatus().equals(FurnitureStatus.UNDER_OPTION)) {
      OptionDTO opt = optionDAO.findByFurnitureId(dto.getFurnitureId());
      opt.setUser(userDAO.findById(opt.getUserId()));
      dto.setOption(opt);
    }
    List<PhotoDTO> photos = photoDAO.findAllByFurnitureId(dto.getFurnitureId());
    dto.setPhotos(photos);
    String type = furnitureTypeDAO.findById(dto.getTypeId());
    dto.setType(type);
    dto.setRequest(requestForVisitDAO.findByRequestId(dto.getRequestId()));
  }
}
