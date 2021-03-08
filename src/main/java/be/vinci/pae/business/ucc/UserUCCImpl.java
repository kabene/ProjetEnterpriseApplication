package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.persistence.dao.UserDAO;
import jakarta.inject.Inject;

public class UserUCCImpl implements UserUCC {

  @Inject
  UserDAO userDAO;
  @Inject
  UserFactory userFactory;

  /**
   * Logs in after checking the given credentials.
   *
   * @param username : the given username
   * @param password : the given password
   * @return the corresponding UserDTO or null if invalid credentials
   */
  @Override
  public UserDTO login(String username, String password) {
    User userFound = userDAO.findByUsername(username);
    if (userFound == null) {
      return null; // invalid username
    }
    if (!userFound.checkPassword(password)) {
      return null; // invalid password
    }

    UserDTO res = userFactory.getUserDTO();
    res.setID(userFound.getID());
    res.setUsername(userFound.getUsername());
    return res;
  }
}
