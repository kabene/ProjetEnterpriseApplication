package be.vinci.pae.persistence.dao;


import org.apache.commons.text.StringEscapeUtils;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.exceptions.TakenException;
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
   * @param username : the username to look for.
   * @return a UserDTO corresponding to the user found or null if no user has the given username.
   */
  @Override
  public UserDTO findByUsername(String username) {
    UserDTO userFound = userFactory.getUserDTO();
    try {
      String query = "SELECT u.* FROM satchofurniture.users u WHERE u.username = ?";
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setString(1, StringEscapeUtils.escapeHtml4(username));
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

  /**
   * find a user based on his id.
   *
   * @param userId the id of the user.
   * @return User represented by UserDTO.
   */
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
  public boolean isAdmin(int id) {
    try {
      String query = "SELECT u.* FROM satchofurniture.users u WHERE u.user_id = ? "
                    + "AND u.role = 'admin'";
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (SQLException e) {
      return false;
    }
    return false;
  }

  @Override
  public List<UserDTO> getAllCustomers() {
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
    } catch (SQLException e) {
      return null;
    }
    return users;
  }

  @Override
  public List<UserDTO> findBySearch(String customerSearch) {
    List<UserDTO> users = new ArrayList<UserDTO>();
    try {
      String query = "SELECT u.* FROM satchofurniture.users u "
                    + "INNER JOIN satchofurniture.addresses a ON u.address_id=a.address_id "
                    + "WHERE lower(a.commune) LIKE lower('%?%') "
                    + "OR lower(a.postcode) LIKE lower('%?%') "
                    + "OR lower(u.first_name) LIKE lower('%?%')";
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setString(1, customerSearch);
      ps.setString(2, customerSearch);
      ps.setString(3, customerSearch);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        UserDTO user = userFactory.getUserDTO();
        user.setID(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        users.add(user);
      }
    } catch (SQLException e) {
      return null;
    }
    return users;
  }

  /**
   * used to register a new user.
   *
   * @param user      UserDTO that describe the user.
   * @param addressId id of the address.
   */
  @Override
  public void register(UserDTO user, int addressId) {

    String query =
        " INSERT INTO satchoFurniture.users VALUES(DEFAULT,?,?,?,?," + addressId
            + ",now(),?,?,0,0,'true')";
    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, StringEscapeUtils.escapeHtml4(user.getLastName()));
      ps.setString(2, StringEscapeUtils.escapeHtml4(user.getFirstName()));
      ps.setString(3, StringEscapeUtils.escapeHtml4(user.getUsername()));
      ps.setString(4, StringEscapeUtils.escapeHtml4(user.getEmail()));
      ps.setString(5, StringEscapeUtils.escapeHtml4(user.getRole()));
      ps.setString(6, StringEscapeUtils.escapeHtml4(user.getPassword()));
      ps.execute();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

  }

  /**
   * verify if email is already taken.
   *
   * @param email string email.
   * @return a boolean true if email is already taken and false in other case.
   */
  @Override
  public boolean emailAlreadyTaken(String email) {
    String query = "SELECT * FROM satchoFurniture.users WHERE email = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    boolean res = false;
    try {
      ps.setString(1, StringEscapeUtils.escapeHtml4(email));
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        res = true;
      }
      rs.close();
      ps.close();
      return res;
    } catch (SQLException exception) {
      throw new TakenException();
    }
  }

  /**
   * verify if the username is already taken.
   *
   * @param username string username.
   * @return a boolean true if username is already taken and false in other case.
   */
  @Override
  public boolean usernameAlreadyTaken(String username) {
    String query = "SELECT * FROM satchoFurniture.users WHERE username = ?";
    PreparedStatement ps = dalServices.makeStatement(query);
    boolean res = false;
    try {
      ps.setString(1, StringEscapeUtils.escapeHtml4(username));
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        res = true;
      }
      rs.close();
      ps.close();
      return res;
    } catch (SQLException throwables) {
      throw new TakenException();
    }
  }


}
