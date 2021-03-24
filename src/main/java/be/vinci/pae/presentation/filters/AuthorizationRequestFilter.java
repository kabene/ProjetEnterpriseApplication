package be.vinci.pae.presentation.filters;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.persistence.dao.UserDAO;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;


@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  @Inject
  UserDAO userDAO;
  @Inject
  ConnectionDalServices dalServices;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    DecodedJWT decodedToken = UtilsFilters.getDecodedToken(requestContext);
    int userId = decodedToken.getClaim("user").asInt();
    requestContext.setProperty("userId", userId);
  }
}
