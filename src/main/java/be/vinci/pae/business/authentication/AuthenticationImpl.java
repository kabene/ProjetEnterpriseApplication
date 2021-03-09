package be.vinci.pae.business.authentication;

import be.vinci.pae.business.dto.UserDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import be.vinci.pae.main.Configurate;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class AuthenticationImpl implements Authentication {

  private final Algorithm jwtAlgorithm = Algorithm
      .HMAC256(Configurate.getConfiguration("JWTSecret"));


  @Override
  public String createToken(UserDTO user) {
    String token;
    try {
      token =
          JWT.create().withIssuer("auth0").withClaim("user", user.getID()).sign(this.jwtAlgorithm);
    } catch (Exception e) {
      throw new WebApplicationException("Unable to create token", e, Status.INTERNAL_SERVER_ERROR);
    }
    return token;

  }
}


