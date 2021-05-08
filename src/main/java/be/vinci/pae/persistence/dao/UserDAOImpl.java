package be.vinci.pae.persistence.dao;

import org.apache.commons.text.StringEscapeUtils;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factories.UserFactory;
import jakarta.inject.Inject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl extends AbstractDAO implements UserDAO {

  @Inject
  private UserFactory userFactory;

  /**
   * Executes a query to find a user having a specific username.
   *
   * @param username : the username to look for.
   * @return a UserDTO corresponding to the user found
   */
  @Override
  public UserDTO findByUsername(String username) {
    return findOneByConditions("users",
        new QueryParameter("username", username));
  }

  /**
   * find a user based on his id.
   *
   * @param userId the id of the user.
   * @return User represented by UserDTO.
   */
  @Override
  public UserDTO findById(int userId) {
    return findById(userId, "users", "user_id");
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
            + ",now(),?,?,0,0,?)";

    PreparedStatement ps = dalServices.makeStatement(query);
    try {
      ps.setString(1, StringEscapeUtils.escapeHtml4(user.getLastName()));
      ps.setString(2, StringEscapeUtils.escapeHtml4(user.getFirstName()));
      ps.setString(3, StringEscapeUtils.escapeHtml4(user.getUsername()));
      ps.setString(4, StringEscapeUtils.escapeHtml4(user.getEmail()));
      ps.setString(5, StringEscapeUtils.escapeHtml4(user.getRole()));
      ps.setString(6, StringEscapeUtils.escapeHtml4(user.getPassword()));
      ps.setBoolean(7, user.isWaiting());
      ps.execute();
      ps.close();
    } catch (SQLException e) {
      throw new InternalError(e);
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
    return alreadyExists("users",
        new QueryParameter("email", email));
  }

  /**
   * verify if the username is already taken.
   *
   * @param username string username.
   * @return a boolean true if username is already taken and false in other case.
   */
  @Override
  public boolean usernameAlreadyTaken(String username) {
    return alreadyExists("users",
        new QueryParameter("username", username));
  }

  /**
   * find all users of the db.
   *
   * @return list contains the users of the db.
   */
  @Override
  public List<UserDTO> findAll() {
    return findAll("users");
  }

  /**
   * get all user waiting for registration validation of the db.
   *
   * @return list contains the waiting users of the db.
   */
  @Override
  public List<UserDTO> getAllWaitingUsers() {
    return findByConditions("users",
        new QueryParameter[]{new QueryParameter("is_waiting", true)}, //WHERE
        "last_name", "first_name"); //ORDER BY

  }

  /**
   * get all user with confirmed registration validation of the db.
   *
   * @return list contains the confirmed users of the db.
   */
  @Override
  public List<UserDTO> getAllConfirmedUsers() {
    return findByConditions("users",
        new QueryParameter[]{new QueryParameter("is_waiting", false)}, //WHERE
        "last_name", "first_name"); //ORDER BY
  }

  /**
   * Update the role and set wait.
   *
   * @param id    userId.
   * @param value value if the user is confirmed.
   */
  @Override
  public void updateRole(int id, boolean value) {
    if (value) {
      updateById("users",
          new QueryParameter("user_id", id),
          new QueryParameter("is_waiting", false));
      //query = "UPDATE  satchoFurniture.users u SET is_waiting = false WHERE  u.user_id = ?";
    } else {
      updateById("users",
          new QueryParameter("user_id", id),
          new QueryParameter("is_waiting", false),
          new QueryParameter("role", "customer"));
    }
  }


  /**
   * Update the number of purchased furniture from user.
   *
   * @param userDTO userDTO to update
   */
  @Override
  public UserDTO updatePurchasedFurnitureNbr(UserDTO userDTO) {
    updateById("users",
        new QueryParameter("user_id", userDTO.getId()),
        new QueryParameter("purchased_furniture_nbr", userDTO.getPurchasedFurnitureNbr()));
    return userDTO;
  }

  /**
   * Update the number of sold furniture from user.
   *
   * @param userDTO userDTO to update
   */
  @Override
  public UserDTO updateSoldFurnitureNbr(UserDTO userDTO) {
    updateById("users",
        new QueryParameter("user_id", userDTO.getId()), //WHERE
        new QueryParameter("sold_furniture_nbr", userDTO.getSoldFurnitureNbr())); //SET
    return userDTO;
  }

  /**
   * Creates and fills a UserDTO object using a ResultSet.
   *
   * @param rs : the ResultSet containing the information
   * @throws SQLException in case of problem during access to the ResultSet
   */
  @Override
  protected UserDTO toDTO(ResultSet rs) throws SQLException {
    UserDTO userFound = userFactory.getUserDTO();
    userFound.setId(rs.getInt("user_id"));
    userFound.setLastName(rs.getString("last_name"));
    userFound.setFirstName(rs.getString("first_name"));
    userFound.setUsername(rs.getString("username"));
    userFound.setEmail(rs.getString("email"));
    userFound.setAddressId(rs.getInt("address_id"));
    userFound.setRegistrationDate(rs.getString("registration_date"));
    userFound.setRole(rs.getString("role"));
    userFound.setPassword(rs.getString("password"));
    userFound.setPurchasedFurnitureNbr(rs.getInt("purchased_furniture_nbr"));
    userFound.setSoldFurnitureNbr(rs.getInt("sold_furniture_nbr"));
    userFound.setWaiting(rs.getBoolean("is_waiting"));
    return userFound;
  }


}