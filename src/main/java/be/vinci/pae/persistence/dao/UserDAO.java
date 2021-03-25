package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

public interface UserDAO {

  /**
   * Executes a query to find a user having a specific username.
   *
   * @param username : the username to look for.
   * @return a UserDTO corresponding to the user found or null if no user has the given username.
   */
  UserDTO findByUsername(String username);

  /**
   * Find a user based on his id.
   *
   * @param id the id of the user.
   * @return User represented by UserDTO.
   */
  UserDTO findById(int id);

  /**
   * used to register a new user.
   *
   * @param user      UserDTO that describe the user.
   * @param addressID id of the address.
   */
  void register(UserDTO user, int addressID);

  /**
   * verify if email is already taken.
   *
   * @param email string email.
   * @return a boolean true if email is already taken and false in other case.
   */
  boolean emailAlreadyTaken(String email);

  /**
   * verify if the username is already taken.
   *
   * @param username string username.
   * @return a boolean true if username is already taken and false in other case.
   */
  boolean usernameAlreadyTaken(String username);

  boolean isAdmin(int id);

  List<UserDTO> getAllUsers();

  List<UserDTO> findBySearch(String customerSearch);
}
