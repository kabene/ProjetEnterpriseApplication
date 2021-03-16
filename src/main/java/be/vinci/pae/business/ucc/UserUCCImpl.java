package be.vinci.pae.business.ucc;


import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.dto.UserDTO;
//import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.exceptions.TakenException;
import be.vinci.pae.persistence.dao.AddressDAO;
import be.vinci.pae.persistence.dao.UserDAO;
import jakarta.inject.Inject;

public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO userDAO;

  @Inject
  private AddressDAO addresseDAO;


  /**
   * Logs in after checking the given credentials.
   *
   * @param username : the given username
   * @param password : the given password
   * @return the corresponding UserDTO or null if invalid credentials
   */
  @Override
  public UserDTO login(String username, String password) {
    User userFound = (User) userDAO.findByUsername(username);
    if (userFound == null) {
      return null; // invalid username
    }
    if (!userFound.checkPassword(password)) {
      return null; // invalid password
    }
    return userFound;
  }

  /**
   * used to register a new user.
   *
   * @param user    UserDTO that describe the user.
   * @param address id of the adress.
   */
  @Override
  public UserDTO register(UserDTO user, AddressDTO address) {
    if (userDAO.usernameAlreadyTaken(user.getUsername())) {
      throw new TakenException("username already taken");
    }
    if (userDAO.emailAlreadyTaken(user.getEmail())) {
      throw new TakenException("email already taken");
    }
    addresseDAO.newAdresse(address);
    int id = addresseDAO.getId(address);
    User use = (User) user;
    String Hashed = use.hashPassword(user.getPassword());
    use.setPassword(Hashed);
    userDAO.register(use, id);
    return user;
  }
}
