package be.vinci.pae.presentation;

import be.vinci.pae.business.authentication.Authentication;
import be.vinci.pae.business.dto.UserDTO;
//import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.business.ucc.UserUCC;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
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

  //@Inject
  //private UserFactory userFactory;

  @Inject
  private UserUCC userUCC;
  @Inject
  private Authentication authentication;
  private final ObjectMapper jsonMapper = new ObjectMapper();

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
    String token = authentication.createToken(userDTO);
    ObjectNode node = jsonMapper.createObjectNode().put("token", token).putPOJO("user", userDTO);
    return Response.ok(node, MediaType.APPLICATION_JSON).build();
  }

}
