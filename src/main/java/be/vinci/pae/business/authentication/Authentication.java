package be.vinci.pae.business.authentication;

import be.vinci.pae.business.dto.UserDTO;


public interface Authentication {

  String createToken(UserDTO user);

  String createLongToken(UserDTO user);
}
