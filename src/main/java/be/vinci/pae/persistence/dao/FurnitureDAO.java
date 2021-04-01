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


  List<FurnitureDTO> findAll();

  FurnitureDTO updateStatusOnly(FurnitureDTO furnitureDTO);

  FurnitureDTO updateToAvailable(FurnitureDTO furnitureDTO);

  FurnitureDTO updateToWithdrawn(FurnitureDTO furnitureDTO);
}
