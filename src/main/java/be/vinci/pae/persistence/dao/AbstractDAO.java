package be.vinci.pae.persistence.dao;

import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.persistence.dal.ConnectionBackendDalServices;
import be.vinci.pae.utils.Configurate;
import jakarta.inject.Inject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

  /**
   * Enum representing the two possible ORDER BY directions (ASC, and DESC).
   */
  protected enum OrderByDirection {
    ASC("ASC"),
    DESC("DESC");

    public final String value;

    OrderByDirection(String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }
  }

  /**
   * Class representing an ORDER BY column and its direction.
   */
  protected class OrderBy {

    private String columnName;
    private OrderByDirection direction;

    public OrderBy(String columnName, OrderByDirection direction) {
      this.columnName = columnName;
      this.direction = direction;
    }
  }

  // -- Protected (Available in DAOs) Methods --

  // find all:

  /**
   * Finds all entries in a specific table. This method is inherited by all classes extending
   * AbstractDAO and is intended to be used to implement findAll() methods.
   *
   * @param tableName : the name of the table (db)
   * @param <T>       : The class of DTO to use in the result List
   * @return a List of 'Dto' containing all found entries
   */
  protected <T> List<T> findAll(String tableName) {
    String query = "SELECT t.* FROM " + schema + "." + tableName + " t";
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
   * @param orderBy   : all column names for order-by (in order of importance), they are all ASC.
   * @param <T>       : The class of DTO to use in the result List.
   * @return a List of 'Dto' containing all found entries.
   */
  protected <T> List<T> findAll(String tableName, String... orderBy) {
    OrderBy[] obArray = (OrderBy[]) Arrays.stream(orderBy)
        .map((s) -> new OrderBy(s, OrderByDirection.ASC))
        .toArray();
    return findAll(tableName, obArray);
  }

  protected <T> List<T> findAll(String tableName, OrderBy... orderBy) {
    String query = "SELECT t.* FROM " + schema + "." + tableName + " t" + generateOrderBy(orderBy);
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      return executeSelectMultipleResults(ps);
    } catch (SQLException e) {
      throw new InternalError(e);
    }
  }

  // find all by conditions:

  /**
   * Finds all the entries in a table that matches all the given conditions.
   *
   * @param tableName       : The table name
   * @param queryParameters : All conditions as QueryParameter (columnName + value)
   * @param <T>             : The type of DTO to return.
   * @return a List of DTOs (T)
   */
  protected <T> List<T> findByConditions(String tableName, QueryParameter... queryParameters) {
    String query = "SELECT t.* FROM " + schema + "." + tableName + " t";
    query += generateWhere(queryParameters);
    return executeSelectMultipleResultsWhere(queryParameters, query);
  }

  /**
   * Finds all the entries in a table that matches all the given conditions. The resulting List is
   * ordered by the specified column names.
   *
   * @param tableName       : The table name
   * @param queryParameters : Array of QueryParameter representing the WHERE conditions
   * @param orderBy         : All ORDER BY column names in order of apparition
   * @param <T>             : The type of DTO to return.
   * @return a List of DTOs (T)
   */
  protected <T> List<T> findByConditions(String tableName, QueryParameter[] queryParameters,
      String... orderBy) {
    OrderBy[] obArray = (OrderBy[]) Arrays.stream(orderBy)
        .map((s) -> new OrderBy(s, OrderByDirection.ASC))
        .toArray();
    return findByConditions(tableName, queryParameters, obArray);
  }

  protected <T> List<T> findByConditions(String tableName, QueryParameter[] queryParameters,
      OrderBy... orderBy) {
    String query = "SELECT t.* FROM " + schema + "." + tableName + " t";
    query += generateWhere(queryParameters);
    query += generateOrderBy(orderBy);
    return executeSelectMultipleResultsWhere(queryParameters, query);
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
   * Finds all the entries in a table that shares the same FK. The resulting List is ordered
   * following the given ORDER BY column names.
   *
   * @param tableName : The table name
   * @param fkName    : The column name of the FK
   * @param fk        : The FK (int)
   * @param orderBy   : All ORDER BY column names (in order)
   * @param <T>       : The type of DTO to return.
   * @return a List of DTOs (T)
   */
  protected <T> List<T> findByFK(String tableName, String fkName, int fk, String... orderBy) {
    OrderBy[] obArray = (OrderBy[]) Arrays.stream(orderBy)
        .map((s) -> new OrderBy(s, OrderByDirection.ASC))
        .toArray();
    return findByFK(tableName, fkName, fk, obArray);
  }

  protected <T> List<T> findByFK(String tableName, String fkName, int fk, OrderBy... orderBy) {
    return findByConditions(tableName, new QueryParameter[]{new QueryParameter(fkName, fk)},
        orderBy);
  }

  // find one by conditions:

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
    return findOneByConditions(tableName, new QueryParameter(idName, id));
  }

  // already exists:

  /**
   * Verifies if an entry that follows all given conditions already exists in the specified table.
   *
   * @param tableName       : The table name.
   * @param queryParameters : All the conditions.
   * @return a boolean representing if such an entry exists or not.
   */
  protected boolean alreadyExists(String tableName, QueryParameter... queryParameters) {
    return !findByConditions(tableName, queryParameters).isEmpty();
  }

  // update:

  /**
   * Updates one or multiple columns in an entry (given its table name and id).
   *
   * @param tableName       : The table name
   * @param id              : id + id column-name (as a QueryParameter)
   * @param queryParameters : All columns to update, and their column names (as QueryParameters)
   * @return boolean representing if the operation was a success or not.
   */
  protected boolean updateById(String tableName, QueryParameter id,
      QueryParameter... queryParameters) {
    boolean res;
    if (queryParameters.length == 0) {
      throw new IllegalArgumentException("At least 1 QueryParameter should be provided");
    }
    String query = "UPDATE " + schema + "." + tableName;
    query += generateSet(queryParameters);
    query += generateWhere(false, id);
    try {
      PreparedStatement ps = dalServices.makeStatement(query);
      int parameterIndex = 1;
      for (QueryParameter qp : queryParameters) {
        ps.setObject(parameterIndex, qp.value);
        parameterIndex++;
      }
      ps.setObject(parameterIndex, id.value);
      res = ps.execute();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
    }
    return res;
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
   * Executes a "SELECT" PreparedStatement and converts its result set into a List of DTO. Then,
   * closes both the ResultSet, and the provided PreparedStatement.
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
    ps.close();
    return dtoList;
  }

  /**
   * Executes a "SELECT" PreparedStatement after setting its WHERE parameters and converts its
   * result set into a List of DTO.
   *
   * @param queryParameters : Array of QueryParameters representing the ordered WHERE parameters
   * @param query           : Query to execute
   * @param <T>             The type of DTO to return
   * @return A list containing all results of the SELECT query as DTOs
   * @throws InternalError if a problem occurs while accessing the db.
   */
  private <T> List<T> executeSelectMultipleResultsWhere(QueryParameter[] queryParameters,
      String query) {
    List<T> res;
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

  /**
   * Generates the ORDER BY part of a query.
   *
   * @param orderBIES all table names for order by, in order.
   * @return ORDER BY part of a SELECT query (as String).
   */
  private String generateOrderBy(OrderBy... orderBIES) {
    if (orderBIES.length == 0) {
      return "";
    }
    String res = " ORDER BY ";
    int i = 0;
    for (OrderBy orderBy : orderBIES) {
      res += orderBy.columnName + " " + orderBy.direction.getValue();
      i++;
      if (i < orderBIES.length) {
        res += ", ";
      }
    }
    return res;
  }

  /**
   * Generates the WHERE part of a query based on QueryParameter(s) (in the order of the
   * parameters). The generated String (query part) contains the t alias for the table.
   *
   * @param queryParameters : All query parameters
   * @return WHERE part of a query (as String).
   */
  private String generateWhere(QueryParameter... queryParameters) {
    return generateWhere(true, queryParameters);
  }

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
          res += "t.";
        }
        res += qp.columnName + " = ?";
        i++;
        if (i < queryParameters.length) {
          res += " AND ";
        }
      }
    }
    return res;
  }

  /**
   * Generates the SET part of an UPDATE statement.
   *
   * @param queryParameters : All QueryParameters to update.
   * @return SET part of the query.
   */
  private String generateSet(QueryParameter... queryParameters) {
    String res = " SET ";
    if (queryParameters.length == 0) {
      throw new IllegalArgumentException("At least 1 QueryParameter should be provided");
    }
    int i = 0;
    for (QueryParameter qp : queryParameters) {
      res += qp.columnName + " = ?";
      i++;
      if (i < queryParameters.length) {
        res += ", ";
      }
    }
    return res;
  }
}
