package be.vinci.pae.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FurnitureTypeDAOImpl extends AbstractDAO implements FurnitureTypeDAO {

  /**
   * Finds one furniture type with its id.
   *
   * @param id : the type's id
   * @return the type label as a String
   */
  @Override
  public String findById(int id) {
    return findById(id, "furniture_types", "type_id");
  }

  /**
   * Finds all types in the DB.
   *
   * @return the types as a List of Strings.
   */
  @Override
  public List<String> findAll() {
    return findAll("furniture_type");
  }


  /**
   * Creates a string containing a furnitureType using a ResultSet.
   *
   * @param rs : the ResultSet containing the information
   * @return a string containing the information from the result set
   * @throws SQLException in case of problem during access to the ResultSet
   */
  @Override
  protected String toDTO(ResultSet rs) throws SQLException {
    return rs.getString("type_name");
  }
}
