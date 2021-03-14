package be.vinci.pae.business.pojos;

import be.vinci.pae.business.dto.UserDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface  User extends UserDTO {

  String getLast_name();

  void setLast_name(String last_name);

  String getFirst_name();

  void setFirst_name(String first_name);

  String getEmail();

  void setEmail(String email);

  String getRole();

  void setRole(String role);

  boolean checkPassword(String plainText);

  String hashPassword(String password);



}
