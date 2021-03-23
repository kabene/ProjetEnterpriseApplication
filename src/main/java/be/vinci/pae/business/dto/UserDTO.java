package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.UserImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  Integer getID();

  void setID(Integer id);

  String getUsername();

  void setUsername(String username);

  String getPassword();

  void setPassword(String password);

  String getLastName();

  void setLastName(String lastName);

  String getFirstName();

  void setFirstName(String firstName);

  String getEmail();

  void setEmail(String email);

  String getRole();

  void setRole(String role);

  AddressDTO getAddress();

  void setAddress(AddressDTO addressDTO);

  //implement address + other attributes


}
