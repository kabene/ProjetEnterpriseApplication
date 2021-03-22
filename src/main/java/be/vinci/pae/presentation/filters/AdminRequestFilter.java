package be.vinci.pae.presentation.filters;

import be.vinci.pae.persistence.dal.ConnectionDalServices;
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
  @Inject
  ConnectionDalServices dalServices;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    int userId;
    try {
      userId = Integer.parseInt(requestContext.getHeaderString("user_id"));
      dalServices.startTransaction();
      if (!this.userDAO.isAdmin(userId)) {
        dalServices.commitTransaction();
        requestContext
            .abortWith(Response.status(Status.UNAUTHORIZED).entity("Unauthorized").build());
      }
      dalServices.commitTransaction();
    } catch (Exception e) {
      requestContext
          .abortWith(Response.status(Status.UNAUTHORIZED).entity("Not connected").build());
    }
  }
}