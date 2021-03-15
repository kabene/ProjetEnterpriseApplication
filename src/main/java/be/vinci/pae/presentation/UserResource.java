package be.vinci.pae.presentation;

import be.vinci.pae.business.authentication.Authentication;
import be.vinci.pae.business.dto.UserDTO;
//import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.filters.Authorize;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.utils.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

@Singleton
@Path("/users")
public class UserResource {

  //@Inject
  //private UserFactory userFactory;

  @Inject
  private UserUCC userUCC;
  @Inject
  private Authentication authentication;
  private final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * Login via "remember me" token.
   *
   * @param request : the request context
   * @return user information and new jwt
   */
  @GET
  @Path("login")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response rememberMe(@Context ContainerRequest request) {
    UserDTO currentUser = Json
        .filterPublicJsonView((UserDTO) request.getProperty("user"), UserDTO.class);
    String token = authentication.createLongToken(currentUser);
    ObjectNode node = jsonMapper.createObjectNode().put("token", token)
        .putPOJO("user", currentUser);
    return Response.ok(node, MediaType.APPLICATION_JSON).build();
  }

  /**
   * POST users/login - Manages login requests.
   *
   * @param reqNode : containing request username, password and rememberMe flag
   * @return user information and jwt
   * @throws WebApplicationException to send a fail status
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response login(JsonNode reqNode) {
    String username = reqNode.get("username").asText();
    String password = reqNode.get("password").asText();
    if (username == null || password == null) { // invalid request
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Lacks mandatory info").type("text/plain")
              .build());
    }
    UserDTO userDTO = userUCC.login(username, password);
    if (userDTO == null) { // user not found
      throw new WebApplicationException(
          Response.status(Status.NOT_FOUND).entity("Invalid credentials").type("text/plain")
              .build());
    }
    userDTO = Json.filterPublicJsonView(userDTO, UserDTO.class);
    String token;
    boolean rememberMe = reqNode.get("rememberMe").asBoolean();
    if (rememberMe) {
      token = authentication.createLongToken(userDTO);
    } else {
      token = authentication.createToken(userDTO);
    }
    ObjectNode resNode = jsonMapper.createObjectNode().put("token", token).putPOJO("user", userDTO);
    return Response.ok(resNode, MediaType.APPLICATION_JSON).build();
  }

  /**
   * Returns the current user after checking the given jwt.
   *
   * @param request : the request context
   * @return the current user.
   */
  @GET
  @Path("me")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response getUser(@Context ContainerRequest request) {
    UserDTO currentUser = Json
        .filterPublicJsonView((UserDTO) request.getProperty("user"), UserDTO.class);
    return Response.ok(currentUser, MediaType.APPLICATION_JSON).build();
  }

  /**
   * POST users/signup - Manages signup requests.
   *
   * @param user : containing request with all datas
   * @return UserDTO user information
   * @throws WebApplicationException to send a fail status
   */
  @POST
  @Path("signup")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response signup(User user) {
    return null;
  }

  @GET
  @Path("customers")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response getCustomers() {
    List<UserDTO> users = userUCC.rechercherTousLesClients();
    ObjectNode resNode = jsonMapper.createObjectNode();
    for (UserDTO user : users)
      resNode.putPOJO("user", Json.filterPublicJsonView(user, UserDTO.class));
    return Response.ok(resNode, MediaType.APPLICATION_JSON).build();
  }

  @POST
  @Path("customers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response getCustomers(JsonNode jsonNode) {
    String filter = jsonNode.get("filter").asText();
    List<UserDTO> users = userUCC.rechercherClients(filter);
    ObjectNode resNode = jsonMapper.createObjectNode();
    for (UserDTO user : users)
      resNode.putPOJO("user", Json.filterPublicJsonView(user, UserDTO.class));
    return Response.ok(resNode, MediaType.APPLICATION_JSON).build();
  }
}
