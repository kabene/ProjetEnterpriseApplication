package be.vinci.pae.presentation;

import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.main.Main;
import be.vinci.pae.presentation.authentication.Authentication;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.presentation.filters.AllowTakeover;
import be.vinci.pae.presentation.filters.Authorize;
import be.vinci.pae.presentation.filters.Admin;
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
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.server.ContainerRequest;

@Singleton
@Path("/users")
public class UserResource {


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
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /users/login");
    UserDTO user = (UserDTO) request.getProperty("user");
    UserDTO currentUser = Json
        .filterPublicJsonView(user, UserDTO.class);
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
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "POST /users/login");
    JsonNode usernameNode = reqNode.get("username");//.asText();
    JsonNode passwordNode = reqNode.get("password");
    if (usernameNode == null || passwordNode == null) { // invalid request
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Lacks mandatory info").type("text/plain")
              .build());
    }
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.FINE, "Valid request body");
    String username = usernameNode.asText();
    String password = passwordNode.asText();
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
      token = authentication.createShortToken(userDTO);
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
  @AllowTakeover
  public Response getUser(@Context ContainerRequest request) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /users/me");
    UserDTO userFound = (UserDTO) request.getProperty("user");
    UserDTO currentUser = Json.filterAdminOnlyJsonView(userFound, UserDTO.class);
    return Response.ok(currentUser, MediaType.APPLICATION_JSON).build();
  }


  /**
   * GET a specific user's ,admin only details.
   *
   * @param id : the user id from the request path
   * @return http response containing a user in json format
   */
  @GET
  @Path("/detail/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDetailById(@PathParam("id") int id) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /users/detail/" + id);
    UserDTO userDTO = userUCC.getOne(id);
    userDTO = Json.filterAdminOnlyJsonView(userDTO, UserDTO.class);
    return Response.ok(userDTO).build();
  }

  /**
   * POST users/signup - Manages signup requests.
   *
   * @return UserDTO user information
   * @throws WebApplicationException to send a fail status
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response register(UserDTO user) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "POST /users/register");
    if (user == null || user.getPassword() == null || user.getAddress() == null
        || user.getEmail() == null || user.getUsername() == null || user.getFirstName() == null
        || user.getLastName() == null || user.getRole() == null
        || user.getAddress().getBuildingNumber() == null
        || user.getAddress().getCommune() == null || user.getAddress().getCountry() == null
        || user.getAddress().getPostcode() == 0) {
      throw new BadRequestException("Error: Malformed request");
    }
    if (!user.getRole().equals("customer") && !user.getRole().equals("antique_dealer") && !user
        .getRole().equals("admin")) {
      throw new BadRequestException("Error: Malformed request (unrecognized role)");
    }

    UserDTO userDTO = userUCC.register(user, user.getAddress());
    userDTO = Json.filterPublicJsonView(userDTO, UserDTO.class);
    String token;
    token = authentication.createShortToken(userDTO);
    ObjectNode resNode = jsonMapper.createObjectNode().put("token", token).putPOJO("user", userDTO);
    return Response.ok(resNode, MediaType.APPLICATION_JSON).build();

  }

  /**
   * GET users/detail - Get all users.
   *
   * @return the list of users
   * @throws WebApplicationException to send a fail status
   */
  @GET
  @Path("/detail")
  @Produces(MediaType.APPLICATION_JSON)
  @Admin
  public Response getUsers() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /users/detail");
    List<UserDTO> users = userUCC.getAll();
    return createNodeFromUserList(users);
  }

  /**
   * GET users/detail/waiting - Get all waiting users.
   *
   * @return the list of users
   * @throws WebApplicationException to send a fail status
   */
  @GET
  @Path("/detail/waiting")
  @Produces(MediaType.APPLICATION_JSON)
  @Admin
  public Response getWaitingUsers() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /users/detail/waiting");
    List<UserDTO> users = userUCC.getAllWaiting();
    return createNodeFromUserList(users);
  }

  /**
   * GET users/detail/confirmed - Get all confirmed users.
   *
   * @return the list of users
   * @throws WebApplicationException to send a fail status
   */
  @GET
  @Path("/detail/confirmed")
  @Produces(MediaType.APPLICATION_JSON)
  @Admin
  public Response getConfirmedUsers() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /users/detail/confirmed");
    List<UserDTO> users = userUCC.getAllConfirmed();
    return createNodeFromUserList(users);
  }

  /**
   * GET a specific user's ,admin only details.
   *
   * @param id : the user id from the request path
   * @return http response containing a user in json format
   */
  @PATCH
  @Path("/validate/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response validateUser(@PathParam("id") int id, JsonNode reqNode) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "PATCH /users/validate/" + id);
    JsonNode valueNode = reqNode.get("value");
    if (valueNode == null) {
      throw new BadRequestException("Error: malformed request");
    }
    boolean value = valueNode.asBoolean();
    UserDTO userDTO = userUCC.validateUser(id, value);
    userDTO = Json.filterAdminOnlyJsonView(userDTO, UserDTO.class);
    return Response.ok(userDTO).build();
  }

  /**
   * GET a take over jwt for a specific user account.
   *
   * @param takeoverId : user id to take over
   * @return take over jwt + UserDTO containing user information.
   */
  @GET
  @Path("/takeover/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response takeOver(@Context ContainerRequest request, @PathParam("id") int takeoverId) {
    UserDTO currentAdminDTO = (UserDTO) request.getProperty("user");
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO,
        "GET /users/takeover/" + takeoverId + " (admin: " + currentAdminDTO.getId() + ")");
    UserDTO takeoverUserDTO = userUCC.getOne(takeoverId);
    if (takeoverUserDTO.getRole().equals("admin")) {
      throw new ConflictException("Error: cannot takeover an admin account");
    }
    String takeoverToken = authentication.createTakeoverToken(currentAdminDTO, takeoverUserDTO);
    ObjectNode resNode = jsonMapper.createObjectNode().put("token", takeoverToken)
        .putPOJO("user", takeoverUserDTO);
    return Response.ok(resNode, MediaType.APPLICATION_JSON).build();
  }

  /**
   * create a Node from the userList.
   *
   * @param users list user.
   * @return response.
   */
  private Response createNodeFromUserList(List<UserDTO> users) {
    ObjectNode resNode = jsonMapper.createObjectNode();
    ArrayNode arrayNode = resNode.putArray("users");
    for (UserDTO user : users) {
      user = Json.filterAdminOnlyJsonView(user, UserDTO.class);
      arrayNode.addPOJO(user);
    }
    return Response.ok(resNode, MediaType.APPLICATION_JSON).build();
  }

}
