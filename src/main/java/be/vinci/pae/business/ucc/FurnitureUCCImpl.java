package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.persistence.dao.FurnitureDAO;
import jakarta.inject.Inject;

public class FurnitureUCCImpl implements FurnitureUCC{

  @Inject
  private FurnitureDAO furnitureDAO;

  @Override
  public FurnitureDTO getOne(int id) {
    return furnitureDAO.findById(id);
  }
}
