package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.UserDTO;

public class UserUCCImpl implements UserUCC {

  /**
   * Logs in after checking the given credentials.
   *
   * @param username : the given username
   * @param password : the given password
   * @return the corresponding UserUCC or null if invalid credentials
   */
  @Override
  public UserDTO login(String username, String password) {
    return null; //stub
  }
}
