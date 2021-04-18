package be.vinci.pae.persistence.dao;

import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO {

  @Inject
  protected ConnectionBackendDalServices dalServices;


  /**
   * Finds all entries in a specific table. This method is inherited by all classes extending
   * AbstractDAO and is intended to be used to implement findAll() methods.
   *
   * @param tableName : the name of the table (db)
   * @param <T>     : The class of DTO to use in the result List
   * @return a List of 'Dto' containing all found entries
   */
  protected <T> List<T> findAll(String tableName) {
    List<T> dtoList = new ArrayList<>();
    String query = "SELECT t.* FROM satchoFurniture." + tableName + " t";
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        dtoList.add(toDTO(rs));
      }
      rs.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return dtoList;
  }

  /**
   * Instantiates and fills a DTO object using an entry from a ResultSet.
   *
   * @param rs    A ResultSet.
   * @param <T> The type of DTO to return.
   * @return A DTO instance filled with information from the result set.
   * @throws SQLException if a problem occurs while reading the result set.
   */
  protected abstract <T> T toDTO(ResultSet rs) throws SQLException;

}
