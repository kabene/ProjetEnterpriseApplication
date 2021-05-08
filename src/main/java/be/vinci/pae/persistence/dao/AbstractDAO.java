package be.vinci.pae.persistence.dao;

import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import jakarta.inject.Inject;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO {

  @Inject
  protected ConnectionBackendDalServices dalServices;

  // -- QueryParameter --

  protected class QueryParameter {

    private String columnName;
    private Object value;

    public QueryParameter(String columnName, Object value) {
      this.columnName = columnName;
      this.value = value;
    }
  }

  // -- Protected Methods --

  /**
   * Finds all entries in a specific table. This method is inherited by all classes extending
   * AbstractDAO and is intended to be used to implement findAll() methods.
   *
   * @param tableName : the name of the table (db)
   * @param <T>       : The class of DTO to use in the result List
   * @return a List of 'Dto' containing all found entries
   */
  protected <T> List<T> findAll(String tableName) {
    String query = "SELECT t.* FROM satchoFurniture." + tableName + " t";
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      return executeSelectMultipleResults(ps);
    } catch (SQLException e) {
      throw new InternalError(e);
    }
  }

  /**
   * Finds all entries in a specific table.
   *
   * @param tableName : table name.
   * @param orderBy   : all column names for order-by (in order)
   * @param <T>       : The class of DTO to use in the result List
   * @return a List of 'Dto' containing all found entries
   */
  protected <T> List<T> findAll(String tableName, String... orderBy) {
    String query = "SELECT t.* FROM satchoFurniture." + tableName + " t" + generateOrderBy(orderBy);
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      return executeSelectMultipleResults(ps);
    } catch (SQLException e) {
      throw new InternalError(e);
    }
  }

  /**
   * Finds a specific entry in a table based on its id.
   *
   * @param id        : The id
   * @param tableName : The table name
   * @param idName    : The id column name
   * @param <T>       : The class of DTO to use in the result List
   * @return a DTO (T) containing the found entry.
   * @throws NotFoundException if no entry matches the given id.
   */
  protected <T> T findById(int id, String tableName, String idName) {
    T res;
    String query = "SELECT t.* FROM satchofurniture." + tableName + " t WHERE t." + idName + " = ?";
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        res = toDTO(rs);
      } else {
        throw new NotFoundException("Error: piece of furniture not found");
      }
      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e.getMessage());
    }
    return res;
  }

  /**
   * Finds all the entries in a table that shares the same FK.
   *
   * @param tableName : The table name
   * @param fkName    : The column name of the FK
   * @param fk        : The FK (int)
   * @param <T>       : The type of DTO to return.
   * @return a List of DTOs (T)
   */
  protected <T> List<T> findByFK(String tableName, String fkName, int fk) {
    return findByConditions(tableName, new QueryParameter(fkName, fk));
  }

  /**
   * Finds all the entries in a table that matches all the given conditions.
   *
   * @param tableName       : The table name
   * @param queryParameters : All conditions as QueryParameter (columnName + value)
   * @param <T>             : The type of DTO to return.
   * @return a List of DTOs (T)
   */
  protected <T> List<T> findByConditions(String tableName, QueryParameter... queryParameters) {
    List<T> res;
    String query = "SELECT t.* FROM satchofurniture." + tableName + " t";
    query += generateWhere(queryParameters);
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      int parameterIndex = 1;
      for (QueryParameter qp : queryParameters) {
        ps.setObject(parameterIndex, qp.value);
        parameterIndex++;
      }
      res = executeSelectMultipleResults(ps);
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return res;
  }

  protected boolean alreadyExists(String tableName, QueryParameter qp) {
    return true; //TODO
  }

  protected void update(String tableName, QueryParameter id, QueryParameter... queryParameters) {
    //TODO
  }

  // -- Abstract Methods --

  /**
   * Instantiates and fills a DTO object using an entry from a ResultSet.
   *
   * @param rs  A ResultSet.
   * @param <T> The type of DTO to return.
   * @return A DTO instance filled with information from the result set.
   * @throws SQLException if a problem occurs while reading the result set.
   */
  protected abstract <T> T toDTO(ResultSet rs) throws SQLException;

  // -- Private methods --

  /**
   * Executes a "SELECT" PreparedStatement and converts its result set into a List of DTO.
   *
   * @param ps  a PreparedStatement ready to be executed
   * @param <T> The type of DTO to return
   * @return A list containing all results of the SELECT query as DTOs
   * @throws SQLException if a problem occurs while executing ps or filling the List of DTOs.
   */
  private <T> List<T> executeSelectMultipleResults(PreparedStatement ps) throws SQLException {
    List<T> dtoList = new ArrayList<>();
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      dtoList.add(toDTO(rs));
    }
    rs.close();
    return dtoList;
  }

  /**
   * Dynamically generates the ORDER BY part of a query.
   *
   * @param orderBy all table names for order by, in order.
   * @return ORDER BY part of a SELECT query (as String).
   */
  private String generateOrderBy(String... orderBy) {
    if (orderBy.length == 0) {
      return "";
    }
    String res = " ORDER BY ";
    int i = 0;
    for (String column : orderBy) {
      res += column;
      i++;
      if (i < orderBy.length) {
        res += ", ";
      }
    }
    return res;
  }

  /**
   * Generates the WHERE part of a query based on query parameters (in order).
   *
   * @param queryParameters : all query parameters
   * @return WHERE part of a query (as String).
   */
  private String generateWhere(QueryParameter... queryParameters) {
    String res = "";
    if (queryParameters.length != 0) {
      res += " WHERE ";
      int i = 0;
      for (QueryParameter qp : queryParameters) {
        res += "t." + qp.columnName + " = ?";
        i++;
        if (i < queryParameters.length) {
          res += " AND ";
        }
      }
    }
    return res;
  }
}
