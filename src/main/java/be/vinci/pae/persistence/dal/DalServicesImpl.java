package be.vinci.pae.persistence.dal;

import be.vinci.pae.exceptions.DeadlyException;
import be.vinci.pae.utils.Configurate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class DalServicesImpl implements ConnectionDalServices, ConnectionBackendDalServices {

  private DataSource ds;
  private ThreadLocal<Connection> connect;


  /**
   * establish a connection between postgresql database and the system first.
   */
  public DalServicesImpl() {
    connect = new ThreadLocal<>();
    this.ds = setupDataSource();
    // connect = ds.getConnection(); // find an alternative because  it's running on dual task
  }

  /**
   * create a prepared statement based on the String query on param.
   *
   * @param query String query represent query
   * @return PreparedStatement.
   */
  @Override
  public PreparedStatement makeStatement(String query) {
    PreparedStatement prep;
    Connection co = connect
        .get();//Returns the value in the current thread's copy of this thread-local variable.
    try {
      prep = co.prepareStatement(query);
    } catch (SQLException throwables) {
      //throwables.printStackTrace();
      throw new DeadlyException();
    }
    return prep;
  }

  /**
   * startTransaction transaction.
   */
  @Override
  public void startTransaction() {
    try {
      if (connect.get() != null) {
        connect.remove();
        throw new InternalError("startTransaction already started ");
      }
      Connection conn = ds.getConnection();
      conn.setAutoCommit(false);
      connect.set(
          conn); // Sets the current thread's copy of this thread-local variable to the specified value.
    } catch (SQLException throwables) {
      //throwables.printStackTrace();
      throw new DeadlyException(throwables.getMessage());
    }

  }

  /**
   * commitTransaction Transaction.
   */
  @Override
  public void commitTransaction() {
    try {
      Connection conn;
      if ((conn = connect.get()) == null) {
        throw new DeadlyException("no connection");
      }
      conn.commit();
      conn.close();
      this.connect.set(null);
    } catch (SQLException throwables) {
      //throwables.printStackTrace();
      throw new DeadlyException(throwables.getMessage());
    }

  }

  /**
   * RollbackThe Transaction.
   */
  @Override
  public void rollbackTransaction() {
    try {
      Connection conn;
      if ((conn = connect.get()) == null) {
        throw new DeadlyException("no start");
      }
      conn.rollback();
      conn.close();
      this.connect.set(null);
    } catch (SQLException throwables) {
      //throwables.printStackTrace();
      throw new DeadlyException(throwables.getMessage());
    }
  }

  /**
   * set the data structure and source of the Db connection 42.
   *
   * @return DataSource filled with structure of data.
   */
  private DataSource setupDataSource() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUrl(Configurate.getConfiguration("url"));
    ds.setUsername(Configurate.getConfiguration("user"));
    ds.setPassword(Configurate.getConfiguration("password"));
    ds.setMaxIdle(
        0); // 0 for killing every idle connections to leave more place for active connections
    ds.setMaxTotal(1); // initiate only one connection for the datasource
    return ds;
  }

}
