package be.vinci.pae.persistence.dal;

import be.vinci.pae.main.Configurate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class DalServicesImpl implements ConnectionDalServices {

  private DataSource ds;
  private Connection conn = null;

  /**
   * establish a connection between postgresql database and the system first.
   */
  public DalServicesImpl() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver PostgreSQL manquant !");
      e.printStackTrace();
    }
    try {
      conn = ds.getConnection(); // find an alternative because  it's running on dual task
      this.ds = setupDataSource();
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le server !");
      System.exit(1);
    }
  }

  /**
   * create a prepared statement based on the String query on param.
   *
   * @param query String query represent query
   * @return PreparedStatement.
   */
  @Override
  public PreparedStatement makeStatement(String query) {
    PreparedStatement prep = null;
    Connection co = conn;
    try {
      prep = co.prepareStatement(query);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return prep;
  }

  /**
   * @return DataSource.
   */
  private DataSource setupDataSource() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUrl(Configurate.getConfiguration("url"));
    ds.setUsername(Configurate.getConfiguration("user"));
    ds.setPassword(Configurate.getConfiguration("password"));
    return ds;
  }

}
