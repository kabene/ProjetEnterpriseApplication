package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.OptionUCC;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.main.Main;
import be.vinci.pae.presentation.filters.AllowTakeover;
import be.vinci.pae.presentation.filters.Authorize;
import be.vinci.pae.utils.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.server.ContainerRequest;


@Singleton
@Path("/options")
public class OptionResource {

  @Inject
  private OptionUCC optionUCC;
  private final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * GET all option resources.
   *
   * @return list of option as json.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response findAll() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /option/list");
    List<OptionDTO> optionDTOs = optionUCC.listOption();
    List<OptionDTO> res = new ArrayList<>();
    for (OptionDTO dto : optionDTOs) {
      OptionDTO filteredDTO = Json.filterPublicJsonView(dto, OptionDTO.class);
      res.add(filteredDTO);
    }
    return Response.ok(res).build();
  }

  /**
   * GET all option resources of the current user.
   *
   * @param request : the request context
   * @return list of user's options as json.
   */
  @GET
  @Path("me")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @AllowTakeover
  public Response getAllUserOptions(@Context ContainerRequest request) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /option/me");
    UserDTO currentUser = (UserDTO) request.getProperty("user");
    List<OptionDTO> res = optionUCC.myOptions(currentUser);
    return Response.ok(Json.filterAdminOnlyJsonView(res, List.class)).build();
  }

  /**
   * POST a new option resource.
   *
   * @param reqNode body of the request.
   * @param request request context.
   * @return created resource as json.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @AllowTakeover
  public Response introduce(JsonNode reqNode, @Context ContainerRequest request) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "POST /option/introduce");
    UserDTO currentUser = (UserDTO) request.getProperty("user");
    JsonNode nodeFurnitureId = reqNode.get("furnitureId");
    JsonNode nodeDurationId = reqNode.get("duration");
    if (nodeFurnitureId == null || nodeDurationId == null) {
      throw new BadRequestException("Error: Malformed request");
    }
    int furnitureId = nodeFurnitureId.asInt();
    int duration = nodeDurationId.asInt();
    if (duration <= 0 || duration > 5) {
      throw new BadRequestException("Error: Malformed request");
    }
    OptionDTO optionDTO = optionUCC.introduceOption(currentUser, furnitureId, duration);
    ObjectNode resNode = jsonMapper.createObjectNode().putPOJO("option", optionDTO);
    return Response.ok(resNode, MediaType.APPLICATION_JSON).build();
  }

  /**
   * PATCH (cancels) an option by id.
   *
   * @param optionId the id of the option.
   * @param request  the request context.
   * @return modified resource as json.
   */
  @PATCH
  @Path("/cancel/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  @AllowTakeover
  public Response cancel(@PathParam("id") int optionId, @Context ContainerRequest request) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "PATCH /option/cancel");
    UserDTO currentUser = (UserDTO) request.getProperty("user");
    OptionDTO optionDTO = optionUCC.cancelOption(currentUser, optionId);
    optionDTO = Json.filterPublicJsonView(optionDTO, OptionDTO.class);
    return Response.ok(optionDTO, MediaType.APPLICATION_JSON).build();
  }
}
