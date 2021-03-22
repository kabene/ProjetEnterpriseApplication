package be.vinci.pae.presentation.filters;

import be.vinci.pae.utils.Configurate;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class UtilsFilters {

  private static final Algorithm jwtAlgorithm = Algorithm
      .HMAC256(Configurate.getConfiguration("JWTSecret"));
  private static final JWTVerifier jwtVerifier = JWT.require(jwtAlgorithm).withIssuer("auth0")
      .build();

  protected static DecodedJWT getDecodedToken(ContainerRequestContext requestContext) {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext
          .abortWith(Response.status(Status.UNAUTHORIZED).entity("Missing token").build());
    }
    DecodedJWT decodedToken;
    try {
      decodedToken = jwtVerifier.verify(token);
    } catch (Exception e) {
      throw new WebApplicationException("Malformed token", e, Status.UNAUTHORIZED);
    }
    return decodedToken;
  }

}
