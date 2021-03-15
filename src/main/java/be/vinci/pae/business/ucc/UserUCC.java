package be.vinci.pae.business.ucc;

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

  List<UserDTO> rechercherTousLesClients();

  List<UserDTO> rechercherClients(String filtre);
}
