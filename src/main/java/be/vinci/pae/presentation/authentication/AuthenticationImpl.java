package be.vinci.pae.presentation.authentication;

import be.vinci.pae.business.dto.UserDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import be.vinci.pae.utils.Configurate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class AuthenticationImpl implements Authentication {

  private final Algorithm jwtAlgorithm = Algorithm
      .HMAC256(Configurate.getConfiguration("JWTSecret"));


  @Override
  public String createToken(UserDTO user) {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    LocalDateTime end = now.plusHours(
        Integer.parseInt(Configurate.getConfiguration("lengthShortJWT"))
    );
    return getTokenFromExpirationDate(user, end);
  }

  @Override
  public String createLongToken(UserDTO user) {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    LocalDateTime end = now.plusMonths(
        Integer.parseInt(Configurate.getConfiguration("lengthLongJWT"))
    );
    return getTokenFromExpirationDate(user, end);
  }

  private String getTokenFromExpirationDate(UserDTO user, LocalDateTime end) {
    String token;
    Date date = Date.from(end.toInstant(ZoneOffset.UTC));
    try {
      token =
          JWT.create().withExpiresAt(date).withIssuer("auth0")
              .withClaim("user", user.getId())
              .sign(this.jwtAlgorithm);
    } catch (Exception e) {
      throw new InternalError("Error: Unable to create token");
    }
    return token;
  }
}


