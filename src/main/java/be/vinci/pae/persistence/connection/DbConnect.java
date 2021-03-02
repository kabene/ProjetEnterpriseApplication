package be.vinci.pae.persistence.connection;

import be.vinci.pae.main.Configurate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class DbConnect implements ConnectionBackendServices {

  private DataSource ds;
  Connection conn = null;

  /**
   * make the connection to the DB
   * @pre: no input required
   * @post: establish a connection between postgresql database and the system
   * first @author: karim
   */
  public DbConnect() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver PostgreSQL manquant !");
      e.printStackTrace();
    }
    try {
      conn = ds.getConnection();  // shouldnt be there but i dont know where I'll do it later Karim#TODO
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le server !");
      System.exit(1);
    }
  }


  /**
   * Set data from .properties
   *
   * @pre: setup data sources
   * @post: return DataSource object with the every information from the properties
   * first @author: karim
   */
  private DataSource setupDataSource() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUrl(Configurate.getConfiguration("url"));
    ds.setUsername(Configurate.getConfiguration("user"));
    ds.setPassword(Configurate.getConfiguration("password"));
    return ds;
  }

  /**
   * Crée un PreparedStatement
   *
   * @param query
   * @pre query : to execute
   * @post send preparedStatement filled
   * first @author: karim
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
}
