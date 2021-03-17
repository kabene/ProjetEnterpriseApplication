package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

  @Inject
  private UserFactory userFactory;
  @Inject
  private ConnectionDalServices dalServices;

  /**
   * Executes a query to find a user having a specific username.
   *
   * @param username : the username to look for
   * @return a UserDTO corresponding to the user found or null if no user has the given username.
   */
  @Override
  public UserDTO findByUsername(String username) {
    UserDTO userFound = userFactory.getUserDTO();
    try {
      String query = "SELECT u.* FROM satchofurniture.users u WHERE u.username = ?";
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        userFound.setID(rs.getInt("user_id"));
        userFound.setUsername(rs.getString("username"));
        userFound.setPassword(rs.getString("password"));
      } else {
        userFound = null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      userFound = null;
    }
    return userFound;
  }

  @Override
  public UserDTO findById(int userId) {
    UserDTO userFound = userFactory.getUserDTO();
    try {
      String query = "SELECT u.* FROM satchofurniture.users u WHERE u.user_id = ?";
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        userFound.setID(rs.getInt("user_id"));
        userFound.setUsername(rs.getString("username"));
      } else {
        userFound = null;
      }
    } catch (SQLException throwables) {
      userFound = null;
    }
    return userFound;
  }

  @Override
  public List<UserDTO> getAllClients() {
    List<UserDTO> users = new ArrayList<UserDTO>();
    try {
      String query = "SELECT u.* FROM satchofurniture.users u";
      PreparedStatement ps = dalServices.makeStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        UserDTO user = userFactory.getUserDTO();
        user.setID(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        users.add(user);
      }
    } catch (SQLException e) {}
    return users;
  }

  @Override
  public List<UserDTO> findBySearch(String filter) {
    List<UserDTO> users = new ArrayList<UserDTO>();
    try {
      String query = "SELECT u.* FROM satchofurniture.users u INNER JOIN satchofurniture.addreses a ON u.address_id=a.address_id WHERE a.commune=? OR a.postalcode=? OR u.username=?";
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setString(1, filter);
      ps.setString(2, filter);
      ps.setString(3, filter);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        UserDTO user = userFactory.getUserDTO();
        user.setID(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        users.add(user);
      }
    } catch (SQLException e) {}
    return users;
  }
}
