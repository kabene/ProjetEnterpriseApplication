package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.AddressDTO;
import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

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
   * @param address address DTO that describe the address of the user.
   */
  UserDTO register(UserDTO user, AddressDTO address);

  /**
   * get all users.
   *
   * @return list contains the users.
   */
  List<UserDTO> getAll();

  /**
   * get all users waiting for registration validation.
   *
   * @return list contains the waiting users.
   */
  List<UserDTO> getAllWaiting();

  /**
   * get the users that correspond with the string.
   *
   * @param userSearch reg of the search.
   * @return list contains the users researched.
   */
  List<UserDTO> getSearchResult(String userSearch);

  /**
   * get the user searched by his id.
   *
   * @param userId the id of the user.
   * @return User represented by UserDTO.
   */
  UserDTO getOne(int userId);

  /**
   * validate or refuse the user status and set the waiting flag.
   * @param userId id of the user.
   * @param value boolean value if the user is validate or refused.
   */
  UserDTO validateUser(int userId, boolean value);
}
