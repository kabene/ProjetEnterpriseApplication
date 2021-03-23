package be.vinci.pae.presentation.filters;

import be.vinci.pae.persistence.dal.ConnectionDalServices;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.persistence.dao.UserDAO;
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
  UserDAO userDAO;
  @Inject
  ConnectionDalServices dalServices;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    UserDTO user = null;
    try {
      dalServices.startTransaction();
      DecodedJWT decodedToken = UtilsFilters.getDecodedToken(requestContext);
      int userId = decodedToken.getClaim("user").asInt();
      user = this.userDAO.findById(userId);
      boolean isAdmin = this.userDAO.isAdmin(userId);
      dalServices.commitTransaction();
      if (!isAdmin) {
        requestContext
            .abortWith(Response.status(Status.UNAUTHORIZED).entity("Unauthorized").build());
      }
      requestContext.setProperty("user", user);
    } catch (Exception e) {
      requestContext
          .abortWith(Response.status(Status.UNAUTHORIZED).entity("Not connected").build());
    }
  }
}