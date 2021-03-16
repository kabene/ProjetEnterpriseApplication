package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.dto.UserDTO;

public interface UserUCC {

  /**
   * Logs in after checking the given credentials.
   *
   * @param username : the given username
   * @param password : the given password
   * @return the corresponding UserDTO or null if invalid credentials
   */
  UserDTO login(String username, String password);

  /**
   * register a user into the system.
   *
   * @param user   userDTO that  describe the user.
   * @param adress adress DTO that describe the adress of the user.
   */
  UserDTO register(UserDTO user, AddressDTO adress);
}
