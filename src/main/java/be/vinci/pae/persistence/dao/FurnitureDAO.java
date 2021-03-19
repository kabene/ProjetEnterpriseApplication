package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.FurnitureDTO;

public interface FurnitureDAO {

  /**
   * Finds one piece of furniture in the db having a specific id.
   *
   * @param id : the id of the piece of furniture
   * @return The piece of furniture as a FurnitureDTO
   */
  FurnitureDTO findById(int id);
}
