package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureTypeDTO;
import java.util.List;

public interface FurnitureTypeUCC {

  /**
   * Finds all furnitureType resources.
   *
   * @return List of FurnitureTypeDTO
   */
  List<FurnitureTypeDTO> findAll();
}
