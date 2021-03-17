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


@Singleton
@Provider
@Authorize
public class AdminRequestFilter implements ContainerRequestFilter {

  @Inject
  UserDAO userDAO;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    int user_id;
    try {
      user_id = Integer.parseInt(requestContext.getHeaderString("user_id"));
      if (!this.userDAO.isAdmin(user_id))
        requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Unauthorized").build());
    } catch(Exception e) {
      requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Not connected").build());
    }
  }
}