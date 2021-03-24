package be.vinci.pae.presentation.filters;

import be.vinci.pae.business.ucc.UserUCC;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;


@Singleton
@Provider
@Admin
public class AdminRequestFilter implements ContainerRequestFilter {

  @Inject
  private UserUCC userUCC;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    try {
      DecodedJWT decodedToken = UtilsFilters.getDecodedToken(requestContext);

      int userId = decodedToken.getClaim("user").asInt();
      boolean isAdmin = userUCC.getOne(userId).getRole().equals("admin");
      if (!isAdmin) {
        requestContext
            .abortWith(Response.status(Status.UNAUTHORIZED).entity("Unauthorized").build());
      }
      requestContext.setProperty("userId", userId);
    } catch (Exception e) {
      requestContext
          .abortWith(Response.status(Status.UNAUTHORIZED).entity("Not connected").build());
    }
  }
}