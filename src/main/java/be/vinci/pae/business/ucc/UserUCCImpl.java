package be.vinci.pae.business.ucc;


import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.dto.UserDTO;
//import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.ForbiddenException;
import be.vinci.pae.exceptions.NotFoundException;
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
      if (!userFound.checkPassword(password)) {
        throw new ForbiddenException("Error: invalid credentials");
      }
      userFound.setAddress(addressDAO.findById(userFound.getAddressId()));
      dalServices.commitTransaction();
      return userFound;
    } catch (NotFoundException e) {
      dalServices.rollbackTransaction();
      throw new ForbiddenException("Error: invalid credentials"); // no user found with given username
    } catch (Exception exception) {
      dalServices.rollbackTransaction();
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
        throw new ConflictException("Error: username already taken");
      }
      if (userDAO.emailAlreadyTaken(userDTO.getEmail())) {
        throw new ConflictException("Error: email already taken");
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
      throw exception;
    }
  }

  @Override
  public List<UserDTO> showAllCustomers() {
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
  public List<UserDTO> showCustomersResult(String customerSearch) {
    List<UserDTO> list;
    try {
      dalServices.startTransaction();
      list = userDAO.findBySearch(customerSearch);
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
