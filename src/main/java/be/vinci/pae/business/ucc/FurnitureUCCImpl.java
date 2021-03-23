package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import be.vinci.pae.persistence.dao.PhotoDAO;
import be.vinci.pae.persistence.dao.UserDAO;
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
  private ConnectionDalServices dalServices;

  @Override
  public FurnitureDTO getOne(int id) {
    dalServices.startTransaction();
    FurnitureDTO res = furnitureDAO.findById(id);
    completeFurnitureDTO(res);
    dalServices.commitTransaction();
    return res;
  }

  @Override
  public List<FurnitureDTO> getAll() {
    dalServices.startTransaction();
    List<FurnitureDTO> dtos = furnitureDAO.findAll();
    for (FurnitureDTO dto : dtos) {
      completeFurnitureDTO(dto);
    }
    dalServices.commitTransaction();
    return dtos;
  }

  /**
   * Completes the FurnitureDTO given as an argument with it's references in the db.
   *
   * @param dto : the FurnitureDTO to complete
   */
  private void completeFurnitureDTO(FurnitureDTO dto) {
    if (dto.getBuyerId() != null && dto.getBuyerId() != 0) {
      UserDTO u = userDAO.findById(dto.getBuyerId());
      dto.setBuyer(u);
    }
    if (dto.getSellerId() != null && dto.getSellerId() != 0) {
      UserDTO u = userDAO.findById(dto.getSellerId());
      dto.setSeller(u);
    }
    if (dto.getFavouritePhotoId() != null && dto.getFavouritePhotoId() != 0) {
      PhotoDTO favPhoto = photoDAO.getPhotoById(dto.getFavouritePhotoId());
      dto.setFavouritePhoto(favPhoto);
    }
    List<PhotoDTO> photos = photoDAO.getPhotosByFurnitureId(dto.getFurnitureId());
    dto.setPhotos(photos);
    String type = furnitureTypeDAO.findById(dto.getTypeId());
    dto.setType(type);
  }
}
