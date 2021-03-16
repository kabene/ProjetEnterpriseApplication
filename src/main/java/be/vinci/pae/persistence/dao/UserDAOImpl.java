package be.vinci.pae.persistence.dao;


import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.exceptions.TakenException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

  /**
   * used to register a new user.
   * @param user UserDTO that describe the user.
   * @param adress_ID id of the adress.
   */
  @Override
  public void register(UserDTO user, int adress_ID) {

    String query =
        " INSERT INTO satchoFurniture.users VALUES(DEFAULT,?,?,?,?," + adress_ID
            + ",now(),?,?,0,0,'true')";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, user.getLast_name());
      ps.setString(2, user.getFirst_name());
      ps.setString(3, user.getUsername());
      ps.setString(4, user.getEmail());
      ps.setString(5, user.getRole());
      ps.setString(6, user.getPassword());
      ps.execute();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
  /**
   * verify if email is already taken.
   * @param email string email.
   * @return a boolean true if email is already taken and false in other case.
   */
  @Override
  public boolean emailAlreadyTaken(String email) {
    String query = "SELECT * FROM satchoFurniture.users WHERE email = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, email);
      ResultSet rs = ps.executeQuery();
      return rs.next();
    } catch (SQLException exception) {
      throw new TakenException();
    }
  }
  /**
   *  verify if the username is already taken.
   * @param username string username.
   * @return a boolean true if username is already taken and false in other case.
   */
  @Override
  public boolean usernameAlreadyTaken(String username) {
    String query = "SELECT * FROM satchoFurniture.users WHERE username = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      return rs.next();
    } catch (SQLException throwables) {
     throw new TakenException();
    }
  }


}
