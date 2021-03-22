package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import be.vinci.pae.persistence.dao.UserDAO;
import jakarta.inject.Inject;

public class FurnitureUCCImpl implements FurnitureUCC {

  @Inject
  private FurnitureDAO furnitureDAO;
  @Inject
  private UserDAO userDAO;
  @Inject
  private ConnectionDalServices dalServices;

  @Override
  public FurnitureDTO getOne(int id) {
    dalServices.startTransaction();
    FurnitureDTO res = furnitureDAO.findById(id);
    if(res.getBuyerId() != 0) {
      UserDTO u = userDAO.findById(res.getBuyerId());
      res.setBuyer(u);
    }
    if(res.getSellerId() != 0) {
      UserDTO u = userDAO.findById(res.getSellerId());
      res.setSeller(u);
    }
    dalServices.commitTransaction();
    return res;
  }
}
