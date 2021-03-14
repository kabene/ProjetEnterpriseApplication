package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.UserImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  int getID();

  void setID(int id);

  String getUsername();

  void setUsername(String username);

  String getPassword();

  void setPassword(String password);

  String getLast_name();

  void setLast_name(String last_name);

  String getFirst_name();

  void setFirst_name(String first_name);

  String getEmail();

  void setEmail(String email);

  String getRole();

  void setRole(String role);

  //implement address + other attributes


}
