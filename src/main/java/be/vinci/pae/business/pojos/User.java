package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.UserDTO;

public interface User extends UserDTO {

  String getPassword();

  void setPassword(String password);

  boolean checkPassword(String password);

  String hashPassword(String password);

}
