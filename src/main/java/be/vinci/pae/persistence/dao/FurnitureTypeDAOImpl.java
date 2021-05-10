package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.FurnitureTypeDTO;
import be.vinci.pae.business.factories.FurnitureTypeFactory;
import jakarta.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FurnitureTypeDAOImpl extends AbstractDAO implements FurnitureTypeDAO {
  @Inject
  FurnitureTypeFactory furnitureTypeFactory;

  /**
   * Finds one furniture type with its id.
   *
   * @param id : the type's id
   * @return the type label as a String
   */
  @Override
  public FurnitureTypeDTO findById(int id) {
    return findOneByConditions("furniture_types",
        new QueryParameter("type_id", id));
  }

  /**
   * Finds all types in the DB.
   *
   * @return the types as a List of Strings.
   */
  @Override
  public List<FurnitureTypeDTO> findAll() {
    return findAll("furniture_types");
  }


  /**
   * Creates a string containing a furnitureType using a ResultSet.
   *
   * @param rs : the ResultSet containing the information
   * @return a FurnitureTypeDTO containing the information from the result set
   * @throws SQLException in case of problem during access to the ResultSet
   */
  @Override
  protected FurnitureTypeDTO toDTO(ResultSet rs) throws SQLException {
    FurnitureTypeDTO dto = furnitureTypeFactory.getFurnitureTypeDTO();
    dto.setTypeName(rs.getString("type_name"));
    dto.setTypeId(rs.getInt("type_id"));
    return dto;
  }
}
