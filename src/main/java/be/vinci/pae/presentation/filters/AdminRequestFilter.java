package be.vinci.pae.presentation.filters;

import be.vinci.pae.persistence.dao.UserDAO;
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

  @Override
  public void filter(ContainerRequestContext requestContext) {
    int userId;
    try {
      userId = Integer.parseInt(requestContext.getHeaderString("user_id"));
      if (!this.userDAO.isAdmin(userId)) {
        requestContext
            .abortWith(Response.status(Status.UNAUTHORIZED).entity("Unauthorized").build());
      }
    } catch (Exception e) {
      requestContext
          .abortWith(Response.status(Status.UNAUTHORIZED).entity("Not connected").build());
    }
  }
}