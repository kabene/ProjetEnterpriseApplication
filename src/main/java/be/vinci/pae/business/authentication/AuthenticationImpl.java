package be.vinci.pae.business.authentication;

import be.vinci.pae.business.dto.UserDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import be.vinci.pae.utils.Configurate;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class AuthenticationImpl implements Authentication {

  private final Algorithm jwtAlgorithm = Algorithm
      .HMAC256(Configurate.getConfiguration("JWTSecret"));


  @Override
  public String createToken(UserDTO user) {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    LocalDateTime end = now
        .plusHours(12); //expires in 12 hours -> to store as cookie on client side
    return getTokenFromExpirationDate(user, end);

  }

  @Override
  public String createLongToken(UserDTO user) {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    LocalDateTime end = now.plusMonths(1); //expires in 1 month -> to store as cookie on client side
    return getTokenFromExpirationDate(user, end);
  }

  private String getTokenFromExpirationDate(UserDTO user, LocalDateTime end) {
    String token;
    Date date = Date.from(end.toInstant(ZoneOffset.UTC));
    try {
      token =
          JWT.create().withExpiresAt(date).withIssuer("auth0").withClaim("user", user.getID())
              .sign(this.jwtAlgorithm);
    } catch (Exception e) {
      throw new WebApplicationException("Unable to create token", e, Status.INTERNAL_SERVER_ERROR);
    }
    return token;
  }
}


