package be.vinci.pae.presentation.filters;

import be.vinci.pae.exceptions.ForbiddenException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.utils.Configurate;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.NullClaim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.ws.rs.container.ContainerRequestContext;

public class UtilsFilters {

  private static final Algorithm jwtAlgorithm = Algorithm
      .HMAC256(Configurate.getConfiguration("JWTSecret"));
  private static final JWTVerifier jwtVerifier = JWT.require(jwtAlgorithm).withIssuer("auth0")
      .build();

  static boolean isTakeover(DecodedJWT decodedJWT) {
    return !(decodedJWT.getClaim("takeover") instanceof NullClaim);
  }

  protected static DecodedJWT getDecodedToken(ContainerRequestContext requestContext) {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      throw new UnauthorizedException("Error: missing token");
    }
    DecodedJWT decodedToken;
    try {
      decodedToken = jwtVerifier.verify(token);
    } catch (Exception e) {
      throw new ForbiddenException("Error: malformed token");
    }
    return decodedToken;
  }

}
