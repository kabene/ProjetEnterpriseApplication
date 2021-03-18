package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.factories.FurnitureFactory;
import jakarta.inject.Inject;

public class FurnitureDAOImpl implements FurnitureDAO{

  @Inject
  private FurnitureFactory furnitureFactory;

  /**
   * Finds one piece of furniture in the db having a specific id.
   *
   * @param id : the id of the piece of furniture
   * @return The piece of furniture as a FurnitureDTO
   */
  @Override
  public FurnitureDTO findById(int id) {
    FurnitureDTO res = furnitureFactory.getFurnitureDTO(); //stub
    res.setFurnitureId(id);
    res.setDescription("desc");
    res.setToPickUp(true);
    return res;
  }
}
