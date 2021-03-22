package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import jakarta.inject.Inject;

public class FurnitureUCCImpl implements FurnitureUCC {

  @Inject
  private FurnitureDAO furnitureDAO;
  @Inject
  private ConnectionDalServices dalServices;

  @Override
  public FurnitureDTO getOne(int id) {
    dalServices.startTransaction();
    FurnitureDTO res = furnitureDAO.findById(id);
    dalServices.commitTransaction();
    return res;
  }
}
