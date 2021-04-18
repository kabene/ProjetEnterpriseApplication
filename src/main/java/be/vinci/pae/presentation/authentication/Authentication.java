package be.vinci.pae.presentation.authentication;

import be.vinci.pae.business.dto.UserDTO;


public interface Authentication {

  /**
   * create a JWT that expires in a short time.
   * the token's lifetime is specified in the prod.properties file.
   *
   * @param user the user who tries to authenticate.
   * @return generated JWT.
   */
  String createShortToken(UserDTO user);

  /**
   * create a JWT that expires in a long time.
   * the token's lifetime is specified in the prod.properties file.
   *
   * @param user the user who tries to authenticate.
   * @return generated JWT.
   */
  String createLongToken(UserDTO user);
}
