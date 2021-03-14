package be.vinci.pae.persistence.dao;

import be.vinci.pae.business.dto.AdresseDTO;
import be.vinci.pae.business.dto.UserDTO;

public interface UserDAO {

  /**
   * Executes a query to find a user having a specific username.
   *
   * @param username : the username to look for
   * @return a UserDTO corresponding to the user found or null if no user has the given username.
   */
  UserDTO findByUsername(String username);

  /**
   * find the userById.
   * @param id id of the user.
   * @return return the user with the id.
   */
  UserDTO findById(int id);

  /**
   * used to register a new user.
   * @param user UserDTO that describe the user.
   * @param adress_ID id of the adress.
   */
  void register(UserDTO user, int adress_ID);


  boolean emailAlreadyTaken(String email);

  boolean usernameAlreadyTaken(String username);
}
