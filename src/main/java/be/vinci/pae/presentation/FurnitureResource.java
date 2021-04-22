package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.pojos.Status;
import be.vinci.pae.business.ucc.FurnitureUCC;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.main.Main;
import be.vinci.pae.presentation.filters.Admin;
import be.vinci.pae.utils.Json;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
@Path("/furniture")
public class FurnitureResource {

  @Inject
  private FurnitureUCC furnitureUCC;

  /**
   * GET a specific piece of furniture's public details (no authentication required).
   *
   * @param id : the furniture id from the request path
   * @return http response containing a piece of furniture in json format
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getById(@PathParam("id") int id) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /furniture/" + id);
    FurnitureDTO furnitureDTO = furnitureUCC.getOne(id);
    if (!furnitureDTO.getStatus().equals(Status.AVAILABLE_FOR_SALE) &&
        !furnitureDTO.getStatus().equals(Status.SOLD)) {
      throw new ConflictException("Unavailable resource (inaccessible status)");
    }
    furnitureDTO = Json.filterPublicJsonView(furnitureDTO, FurnitureDTO.class);
    return Response.ok(furnitureDTO).build();
  }

  /**
   * GET a specific piece of furniture's admin only details.
   *
   * @param id : the furniture id from the request path
   * @return http response containing a piece of furniture in json format
   */
  @GET
  @Path("/detail/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDetailById(@PathParam("id") int id) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /furniture/detail/" + id);
    FurnitureDTO furnitureDTO = furnitureUCC.getOne(id);
    furnitureDTO = Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class);
    return Response.ok(furnitureDTO).build();
  }

  /**
   * GET all pieces of furniture with public details.
   *
   * @return http response containing a list of pieces of furniture in json format
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAll() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /furniture/");
    List<FurnitureDTO> furnitureDTOs = furnitureUCC.getAll();
    List<FurnitureDTO> res = furnitureDTOs.parallelStream()
        .filter((dto) -> dto.getStatus().equals(Status.AVAILABLE_FOR_SALE)
            || dto.getStatus().equals(Status.SOLD)
            || dto.getStatus().equals(Status.UNDER_OPTION))
        .map((dto) -> Json.filterPublicJsonView(dto, FurnitureDTO.class))
        .collect(Collectors.toList());
    return Response.ok(res).build();
  }

  /**
   * GET all pieces of furniture with admin-only details.
   *
   * @return http response containing a list of pieces of furniture in json format
   */
  @GET
  @Path("/detail")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDetailAll() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /furniture/detail");
    List<FurnitureDTO> furnitureDTOs = furnitureUCC.getAll();
    List<FurnitureDTO> res = new ArrayList<>();
    for (FurnitureDTO dto : furnitureDTOs) {
      FurnitureDTO filteredDTO = Json.filterAdminOnlyJsonView(dto, FurnitureDTO.class);
      res.add(filteredDTO);
    }
    return Response.ok(res).build();
  }

  // split state transitions into multiple requests for authentication level management
  // (some are admin only, others are not)

  /**
   * PATCH one piece of furniture to the 'in_restoration' state.
   *
   * @param id : the furniture id
   * @return : the updated piece of furniture
   */
  @PATCH
  @Path("/restoration/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response toRestoration(@PathParam("id") int id) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME)
        .log(Level.INFO, "PATCH /furniture/restoration/" + id);
    FurnitureDTO furnitureDTO = furnitureUCC.toRestoration(id);
    return Response.ok(Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class)).build();
  }

  /**
   * PATCH one piece of furniture to the 'available_for_sale' state.
   *
   * @param id      : the furniture id
   * @param reqNode : the request body
   * @return : updated piece of furniture
   */
  @PATCH
  @Path("/available/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response toAvailable(@PathParam("id") int id, JsonNode reqNode) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "PATCH /furniture/available/" + id);
    if (reqNode == null || reqNode.get("selling_price") == null) {
      throw new BadRequestException("Error: malformed request");
    }
    double sellingPrice = reqNode.get("selling_price").asDouble();
    if (sellingPrice <= 0) {
      throw new BadRequestException("Error: malformed request");
    }
    FurnitureDTO furnitureDTO = furnitureUCC.toAvailable(id, sellingPrice);
    return Response.ok(Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class)).build();
  }

  /**
   * PATCH one piece of furniture to the 'withdrawn' state.
   *
   * @param id : the furniture id
   * @return : the updated piece of furniture
   */
  @PATCH
  @Path("/withdraw/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response withdraw(@PathParam("id") int id) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "PATCH /furniture/withdraw/" + id);
    FurnitureDTO furnitureDTO = furnitureUCC.withdraw(id);
    return Response.ok(Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class)).build();
  }
}

