package be.vinci.pae.business.dto;

import be.vinci.pae.business.pojos.UserImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  int getID();

  void setID(int id);

  String getUsername();

  void setUsername(String username);

  //implement address + other attributes

}
