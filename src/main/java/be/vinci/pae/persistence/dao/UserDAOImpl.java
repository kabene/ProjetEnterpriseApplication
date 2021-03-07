package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.pojos.User;
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
  public User findByUsername(String username) {
    User userFound = userFactory.getUser();
    try {
      String query = "SELECT u.* FROM utilisateurs u WHERE u.pseudo = '?'";
      PreparedStatement ps = dalServices.makeStatement(query);
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if(rs.next()) {
        userFound.setUsername(rs.getString("pseudo"));
        userFound.setPassword(rs.getString("mot_de_passe"));
      } else {
        userFound = null;
      }
    } catch (SQLException e) {
      userFound = null;
    }
    return userFound;
  }
}
