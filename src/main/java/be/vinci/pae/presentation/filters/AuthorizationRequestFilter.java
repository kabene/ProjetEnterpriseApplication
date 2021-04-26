package be.vinci.pae.presentation.filters;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.exceptions.UnauthorizedException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;


@Singleton
@Provider
@Priority(2)
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  @Inject
  private UserUCC userUCC;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    if (requestContext.getProperty("taken_over") == null || !requestContext
        .getProperty("taken_over").equals(Boolean.TRUE)) {
      DecodedJWT decodedToken = UtilsFilters.getDecodedToken(requestContext);
      int userId = decodedToken.getClaim("user").asInt();
      UserDTO currentUser = userUCC.getOne(userId);
      if (currentUser.isWaiting()) {
        throw new UnauthorizedException("Your account is not yet validated");
      }
      requestContext.setProperty("user", currentUser);
    }
  }
}
