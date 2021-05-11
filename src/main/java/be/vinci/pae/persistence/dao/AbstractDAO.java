package be.vinci.pae.persistence.dao;

import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.main.Main;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import be.vinci.pae.utils.Configurate;
import jakarta.inject.Inject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDAO {

  private static final String schema = Configurate.getConfiguration("schema");

  @Inject
  protected ConnectionBackendDalServices dalServices;

  // -- Internal Classes --

  /**
   * Class representing a (columnName, value) pair in the database. Used for queries with unknown
   * parameter types, and/or multiple parameters.
   */
  protected class QueryParameter {

    private String columnName;
    private Object value;

    public QueryParameter(String columnName, Object value) {
      this.columnName = columnName;
      this.value = value;
    }
  }

  // -- Protected (Available in DAOs) Methods --


  /**
   * Finds all entries in a specific table. This method is inherited by all classes extending
   * AbstractDAO and is intended to be used to implement findAll() methods.
   *
   * @param tableName : the name of the table (db)
   * @param <T>       : The class of DTO to use in the result List
   * @return a List of 'Dto' containing all found entries
   */
  protected <T> List<T> findAll(String tableName) {
    return findByConditions(tableName);
  }

  /**
   * Finds all the entries in a table that matches all the given conditions.
   * All conditions are separated by AND.
   *
   * @param tableName       : The table name
   * @param queryParameters : All conditions as QueryParameter (columnName + value)
   * @param <T>             : The type of DTO to return.
   * @return a List of DTOs (T)
   */
  protected <T> List<T> findByConditions(String tableName, QueryParameter... queryParameters) {
    String query = "SELECT t.* FROM " + schema + "." + tableName + " t";
    query += generateWhere(true, queryParameters);
    //Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.FINE, query);
    List<T> res = new ArrayList<>();
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      int parameterIndex = 1;
      for (QueryParameter qp : queryParameters) {
        ps.setObject(parameterIndex, qp.value);
        parameterIndex++;
      }
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        res.add(toDTO(rs));
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return res;
  }

  /**
   * Finds one specific entry in a table based on specified conditions. If multiple entries are
   * found, only the first one will be returned.
   *
   * @param tableName       : The table name
   * @param queryParameters : All conditions as QueryParameter (columnName + value)
   * @param <T>             : The type of DTO to return.
   * @return a single DTO (T) containing the found entry.
   * @throws NotFoundException if no entry matches the given conditions.
   */
  protected <T> T findOneByConditions(String tableName, QueryParameter... queryParameters) {
    List<T> lst = findByConditions(tableName, queryParameters);
    if (lst.isEmpty()) {
      throw new NotFoundException("Error: Resource not found");
    }
    return lst.get(0);
  }


  /**
   * Updates one or multiple columns in an entry (given its table name and id).
   *
   * @param tableName        : The table name
   * @param idQueryParameter : id + id column-name (as a QueryParameter)
   * @param queryParameters  : All columns to update, and their column names (as QueryParameters)
   * @return boolean representing if the operation was a success or not.
   */
  protected void updateById(String tableName, QueryParameter idQueryParameter,
      QueryParameter... queryParameters) {
    if (queryParameters.length == 0) {
      throw new IllegalArgumentException("At least 1 QueryParameter should be provided");
    }
    String query = "UPDATE " + schema + "." + tableName + " SET ";
    //Generate SET
    int i = 0;
    for (QueryParameter qp : queryParameters) {
      query += qp.columnName + " = ?"; // add parameters to update
      i++;
      if (i < queryParameters.length) {
        query += ", "; // ',' between 2 values
      }
    }
    query += generateWhere(false, idQueryParameter);
    //Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.FINE, query);
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      int parameterIndex = 1;
      for (QueryParameter qp : queryParameters) {
        ps.setObject(parameterIndex, qp.value);
        parameterIndex++;
      }
      ps.setObject(parameterIndex, idQueryParameter.value);
      ps.execute();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
  }

  // -- Abstract Methods --

  /**
   * Instantiates and fills a DTO object using an entry from a ResultSet. Has to be implemented in
   * each class extending AbstractDAO.
   *
   * @param rs  A ResultSet.
   * @param <T> The type of DTO to return.
   * @return A DTO instance filled with information from the result set.
   * @throws SQLException if a problem occurs while reading the result set.
   */
  protected abstract <T> T toDTO(ResultSet rs) throws SQLException;

  // -- Private methods --

  /**
   * Generates the WHERE part of a query based on QueryParameter(s) (in the order of the
   * parameters). Can be used in SELECT and UPDATE statements. (for UPDATE statements, hasAlias =
   * false)
   *
   * @param hasAlias        : If the result should use the "t" alias or not.
   * @param queryParameters : All query parameters
   * @return WHERE part of a query (as String).
   */
  private String generateWhere(boolean hasAlias, QueryParameter... queryParameters) {
    String res = "";
    if (queryParameters.length != 0) {
      res += " WHERE ";
      int i = 0;
      for (QueryParameter qp : queryParameters) {
        if (hasAlias) {
          res += "t."; // no aliases in UPDATE (hasAlias = false)
        }
        res += qp.columnName + " = ?";
        i++;
        if (i < queryParameters.length) {
          res += " AND "; // AND between 2 conditions
        }
      }
    }
    return res;
  }
}
