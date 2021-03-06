package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.FurnitureTypeDTO;
import java.util.List;

public interface FurnitureTypeDAO {

  /**
   * Finds one furniture type with its id.
   *
   * @param id : the type's id
   * @return the type label as a String
   */
  FurnitureTypeDTO findById(int id);

  /**
   * Finds all types in the DB.
   *
   * @return the types as a List of Strings.
   */
  List<FurnitureTypeDTO> findAll();
}
