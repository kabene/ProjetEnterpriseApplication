package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.FurnitureDTO;
import java.util.List;

public interface FurnitureDAO {

  /**
   * Finds one piece of furniture in the db having a specific id.
   *
   * @param id : the id of the piece of furniture
   * @return The piece of furniture as a FurnitureDTO
   */
  FurnitureDTO findById(int id);


  /**
   * Finds all pieces of furniture in the database.
   *
   * @return a list of all entries in the furniture table
   */
  List<FurnitureDTO> findAll();

  FurnitureDTO updateConditionOnly(FurnitureDTO furnitureDTO);

  FurnitureDTO updateToAvailable(FurnitureDTO furnitureDTO);

  FurnitureDTO updateToWithdrawn(FurnitureDTO furnitureDTO);
}
