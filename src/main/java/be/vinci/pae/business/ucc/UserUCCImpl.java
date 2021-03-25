package be.vinci.pae.business.ucc;


import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.dto.UserDTO;
//import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.exceptions.TakenException;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.AddressDAO;
import be.vinci.pae.persistence.dao.UserDAO;
import jakarta.inject.Inject;
import java.util.List;


public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO userDAO;

  @Inject
  private AddressDAO addressDAO;

  @Inject
  private ConnectionDalServices dalServices;


  /**
   * Logs in after checking the given credentials.
   *
   * @param username : the given username
   * @param password : the given password
   * @return the corresponding UserDTO or null if invalid credentials
   */
  @Override
  public UserDTO login(String username, String password) {
    try {
      dalServices.startTransaction();

      User userFound = (User) userDAO.findByUsername(username);
      if (userFound == null) {
        dalServices.rollbackTransaction();
        return null; // invalid username
      }
      if (!userFound.checkPassword(password)) {
        dalServices.rollbackTransaction();
        return null; // invalid password
      }
      userFound.setAddress(addressDAO.findById(userFound.getAddressId()));
      dalServices.commitTransaction();
      return userFound;
    } catch (Exception exception) {
      dalServices.rollbackTransaction();
      //  exception.printStackTrace();
      throw exception;
    }
  }

  /**
   * used to register a new user.
   *
   * @param userDTO UserDTO that describe the user.
   * @param address id of the address.
   */
  @Override
  public UserDTO register(UserDTO userDTO, AddressDTO address) {
    try {
      dalServices.startTransaction();
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
      dalServices.commitTransaction();
      return userDTO;
    } catch (Exception exception) {
      dalServices.rollbackTransaction();
      // exception.printStackTrace();
      throw exception;
    }
  }

  @Override
  public List<UserDTO> getAll() {
    List<UserDTO> list;
    try {
      dalServices.startTransaction();
      list = userDAO.getAllCustomers(); //todo: get addresses ?
      dalServices.commitTransaction();
    } catch (Exception exception) {
      dalServices.rollbackTransaction();
      throw exception;
    }
    return list;
  }

  @Override
  public List<UserDTO> getSearchResult(String userSearch) {
    List<UserDTO> list;
    try {
      dalServices.startTransaction();
      list = userDAO.findBySearch(userSearch);
      dalServices.commitTransaction();
    } catch (Exception exception) {
      dalServices.rollbackTransaction();
      throw exception;
    }
    return list;
  }

  @Override
  public UserDTO getOne(int userId) {
    UserDTO res = null;
    try {
      dalServices.startTransaction();
      res = userDAO.findById(userId);
      res.setAddress(addressDAO.findById(res.getAddressId()));
      dalServices.commitTransaction();
    } catch (Exception e) {
      e.printStackTrace();
      dalServices.rollbackTransaction();
    }
    return res;
  }


}
