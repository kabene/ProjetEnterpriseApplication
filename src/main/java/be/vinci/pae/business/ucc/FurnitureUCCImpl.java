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
    if (res.getBuyerId() != 0) {
      UserDTO u = userDAO.findById(res.getBuyerId());
      res.setBuyer(u);
    }
    if (res.getSellerId() != 0) {
      UserDTO u = userDAO.findById(res.getSellerId());
      res.setSeller(u);
    }
    if (res.getFavouritePhotoId() != 0) {
      PhotoDTO favPhoto = photoDAO.getPhotoById(res.getFavouritePhotoId());
      res.setFavouritePhoto(favPhoto);
    }
    List<PhotoDTO> photos = photoDAO.getPhotosByFurnitureId(res.getFurnitureId());
    res.setPhotos(photos);
    String type = furnitureTypeDAO.findById(res.getTypeId());
    res.setType(type);
    dalServices.commitTransaction();
    return res;
  }
}
