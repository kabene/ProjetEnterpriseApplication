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

  /**
   * create a takeover JWT that expires in a short time.
   * The token's lifetime is the same as a shortToken.
   * The token contains the Admin id in the 'user' claim,
   * and the takeover user id in the 'takeover' claim.
   *
   * @param adminDTO : UserDTO containing the admin account information.
   * @param takeoverUserDTO : UserDTO containing the taken over account information.
   * @return generated takeover JWT
   */
  String createTakeoverToken(UserDTO adminDTO, UserDTO takeoverUserDTO);
}
