package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.UserDTO;
//import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.filters.Authorize;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.business.ucc.UserUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.glassfish.jersey.server.ContainerRequest;

@Singleton
@Path("/users")
public class UserRessource {

  //@Inject
  //private UserFactory userFactory;

  @Inject
  private UserUCC userUCC;

  /**
   * POST users/login - Manages login requests.
   *
   * @param user : containing request username and password
   * @return UserDTO user information
   * @throws WebApplicationException to send a fail status
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response login(User user) { //TODO return Response
    if (user.getUsername() == null || user.getPassword() == null) { // invalid request
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Lacks mandatory info").type("text/plain")
              .build());
    }
    UserDTO userDTO = userUCC.login(user.getUsername(), user.getPassword());
    if (userDTO == null) { // user not found
      throw new WebApplicationException(
          Response.status(Status.NOT_FOUND).entity("Invalid credentials").type("text/plain")
              .build());
    }
    return Response.ok(userDTO, MediaType.APPLICATION_JSON).build();
  }

  @GET
  @Path("me")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response getUser(@Context ContainerRequest request) {
    UserDTO currentUser = (UserDTO) request.getProperty("user");
    return Response.ok(currentUser, MediaType.APPLICATION_JSON).build();
  }
}
