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
  private AddressDAO addressDAO;


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
   * @param userDTO    UserDTO that describe the user.
   * @param address id of the address.
   */
  @Override
  public UserDTO register(UserDTO userDTO, AddressDTO address) {
    if (userDAO.usernameAlreadyTaken(userDTO.getUsername())) {
      throw new TakenException("username already taken");
    }
    if (userDAO.emailAlreadyTaken(userDTO.getEmail())) {
      throw new TakenException("email already taken");
    }
    addressDAO.addAddress(address);
    int id = addressDAO.getId(address);
    User user = (User) userDTO;
    String hashed = user.hashPassword(userDTO.getPassword());
    user.setPassword(hashed);
    userDAO.register(user, id);
    userDTO = userDAO.findByUsername(userDTO.getUsername());
    return userDTO;
  }
}
