package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.FurnitureTypeDTO;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.FurnitureTypeDAO;
import jakarta.inject.Inject;
import java.util.List;

public class FurnitureTypeUCCImpl implements FurnitureTypeUCC {

  @Inject
  private ConnectionDalServices dalServices;
  @Inject
  private FurnitureTypeDAO furnitureTypeDAO;

  /**
   * Finds all furnitureType resources.
   *
   * @return List of FurnitureTypeDTO
   */
  @Override
  public List<FurnitureTypeDTO> findAll() {
    List<FurnitureTypeDTO> res;
    try {
      dalServices.startTransaction();
      res = furnitureTypeDAO.findAll();
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }
}
