package be.vinci.pae.business.filters;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.utils.Configurate;
import be.vinci.pae.persistence.dao.UserDAO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm JWT_ALGORITHM = Algorithm
      .HMAC256(Configurate.getConfiguration("JWTSecret"));
  private final JWTVerifier JWT_VERIFIER = JWT.require(this.JWT_ALGORITHM).withIssuer("auth0")
      .build();

  @Inject
  UserDAO userDAO;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext
          .abortWith(Response.status(Status.UNAUTHORIZED).entity("Missing token").build());
    } else {
      DecodedJWT decodedToken ;
      try {
        decodedToken = this.JWT_VERIFIER.verify(token);
      } catch (Exception e) {
        throw new WebApplicationException("Malformed token", e, Status.UNAUTHORIZED);
      }
      UserDTO user = this.userDAO.findById(decodedToken.getClaim("user").asInt());
      if (user == null) {
        throw new WebApplicationException("Malformed token", Status.UNAUTHORIZED);
      }
      requestContext.setProperty("user", user);
    }
  }
}
