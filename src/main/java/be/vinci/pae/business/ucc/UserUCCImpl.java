package be.vinci.pae.business.ucc;


import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.ForbiddenException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.exceptions.UnauthorizedException;
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
      if (userFound.isWaiting()) {
        throw new UnauthorizedException("Your account is not yet validated");
      }
      if (!userFound.checkPassword(password)) {
        throw new ForbiddenException("Error: invalid credentials");
      }
      completeUserDTO(userFound);
      dalServices.commitTransaction();
      return userFound;
    } catch (NotFoundException e) {
      dalServices.rollbackTransaction();
      throw new ForbiddenException("Error: invalid credentials");
      // no user found with given username
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
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
      userDTO.setWaiting(!userDTO.getRole().equals("customer"));
      userDAO.register(user, id);
      userDTO = userDAO.findByUsername(userDTO.getUsername());
      completeUserDTO(userDTO);
      dalServices.commitTransaction();
      return userDTO;
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
  }

  /**
   * get all users.
   *
   * @return list contains the users.
   */
  @Override
  public List<UserDTO> getAll() {
    List<UserDTO> list;
    try {
      dalServices.startTransaction();
      list = userDAO.findAll();
      for (UserDTO user : list) {
        completeUserDTO(user);
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return list;
  }

  /**
   * get all users waiting for registration validation.
   *
   * @return list contains the waiting users.
   */
  @Override
  public List<UserDTO> getAllWaiting() {
    List<UserDTO> list;
    try {
      dalServices.startTransaction();
      list = userDAO.getAllWaitingUsers();
      for (UserDTO user : list) {
        completeUserDTO(user);
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return list;
  }

  /**
   * get all users with confirmed registration validation.
   *
   * @return list contains the confirmed users.
   */
  @Override
  public List<UserDTO> getAllConfirmed() {
    List<UserDTO> list;
    try {
      dalServices.startTransaction();
      list = userDAO.getAllConfirmedUsers();
      for (UserDTO user : list) {
        completeUserDTO(user);
      }
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return list;
  }

  /**
   * get the user searched by his id.
   *
   * @param userId the id of the user.
   * @return User represented by UserDTO.
   */
  @Override
  public UserDTO getOne(int userId) {
    UserDTO res;
    try {
      dalServices.startTransaction();
      res = userDAO.findById(userId);
      completeUserDTO(res);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }

  /**
   * validate or refuse the user status and set the waiting flag.
   *
   * @param userId id of the user.
   * @param value  refused or validated.
   * @return the user modified.
   */
  @Override
  public UserDTO validateUser(int userId, boolean value) {
    UserDTO res;
    try {
      dalServices.startTransaction();
      userDAO.updateRole(userId, value);
      res = userDAO.findById(userId);
      completeUserDTO(res);
      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return res;
  }


  /**
   * Completes the UserDTO given as an argument with it's references in the db.
   *
   * @param dto : the UserDTO to complete
   */
  private void completeUserDTO(UserDTO dto) {
    dto.setAddress(addressDAO.findById(dto.getAddressId()));
  }
}
