package be.vinci.pae.business.pojos;


import be.vinci.pae.business.dto.UserDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface User extends UserDTO {


  boolean checkPassword(String plainText);

  String hashPassword(String password);


}
