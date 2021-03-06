package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.business.ucc.UserUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/users")
public class UserRessource {

  @Inject
  private UserFactory userFactory;

  @Inject
  private UserUCC userUCC;

  @POST
  @Path("login")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO login(User user) {
    if (user.getUsername() == null || user.getPassword() == null) { // invalid request
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Lacks mandatory info").type("text/plain")
              .build());
    }
    UserDTO res = userUCC.login(user.getUsername(), user.getPassword());
    if (res == null) { // user not found
      throw new WebApplicationException(
          Response.status(Status.NOT_FOUND).entity("Resource not found").type("text/plain")
              .build());
    }
    return res;
  }

}
