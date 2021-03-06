package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.ucc.FurnitureUCC;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.main.Main;
import be.vinci.pae.presentation.filters.Admin;
import be.vinci.pae.utils.Json;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
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
    if (!furnitureDTO.getStatus().isPubliclyAvailable()) {
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
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAll() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /furniture/");
    List<FurnitureDTO> furnitureDTOs = furnitureUCC.getAll();
    List<FurnitureDTO> res = furnitureDTOs.parallelStream()
        .filter((dto) -> dto.getStatus().isPubliclyAvailable())
        .map((dto) ->
            Json.filterPublicJsonView(dto, FurnitureDTO.class))
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
   * PATCH one piece of furniture to the 'accepted' status.
   *
   * @param furnitureId : the furniture id
   * @return http response containing the modified resource
   */
  @PATCH
  @Path("/accepted/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response toAccepted(@PathParam("id") int furnitureId, JsonNode reqNode) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME)
        .log(Level.INFO, "/furniture/accepted/" + furnitureId);
    if (reqNode.get("purchasePrice") == null) {
      throw new BadRequestException("Error: malformed request");
    }
    double purchasePrice = reqNode.get("purchasePrice").asDouble();
    if (purchasePrice <= 0) {
      throw new BadRequestException("Error: malformed request (negative price)");
    }
    FurnitureDTO furnitureDTO = furnitureUCC.toAccepted(furnitureId, purchasePrice);
    return Response.ok(Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class)).build();
  }

  /**
   * PATCH one piece of furniture to the 'refused' status.
   *
   * @param furnitureId : the furniture id
   * @return http response containing the modified resource
   */
  @PATCH
  @Path("/refused/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response toRefused(@PathParam("id") int furnitureId) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "/furniture/refused/" + furnitureId);
    FurnitureDTO furnitureDTO = furnitureUCC.toRefused(furnitureId);
    FurnitureDTO res = Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class);
    return Response.ok(res).build();
  }

  /**
   * PATCH one piece of furniture to the 'in_restoration' status.
   *
   * @param furnitureId : the furniture id
   * @return : the updated piece of furniture
   */
  @PATCH
  @Path("/restoration/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response toRestoration(@PathParam("id") int furnitureId) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME)
        .log(Level.INFO, "PATCH /furniture/restoration/" + furnitureId);
    FurnitureDTO furnitureDTO = furnitureUCC.toRestoration(furnitureId);
    return Response.ok(Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class)).build();
  }

  /**
   * PATCH one piece of furniture to the 'available_for_sale' status.
   *
   * @param furnitureId : the furniture id
   * @param reqNode     : the request body
   * @return : updated piece of furniture
   */
  @PATCH
  @Path("/available/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response toAvailable(@PathParam("id") int furnitureId, JsonNode reqNode) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME)
        .log(Level.INFO, "PATCH /furniture/available/" + furnitureId);
    if (reqNode == null || reqNode.get("selling_price") == null) {
      throw new BadRequestException("Error: malformed request");
    }
    double sellingPrice = reqNode.get("selling_price").asDouble();
    if (sellingPrice <= 0) {
      throw new BadRequestException("Error: malformed request");
    }
    FurnitureDTO furnitureDTO = furnitureUCC.toAvailable(furnitureId, sellingPrice);
    return Response.ok(Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class)).build();
  }

  /**
   * PATCH one piece of furniture to the 'withdrawn' status.
   *
   * @param furnitureId : the furniture id
   * @return : the updated piece of furniture
   */
  @PATCH
  @Path("/withdraw/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response withdraw(@PathParam("id") int furnitureId) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME)
        .log(Level.INFO, "PATCH /furniture/withdraw/" + furnitureId);
    FurnitureDTO furnitureDTO = furnitureUCC.withdraw(furnitureId);
    return Response.ok(Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class)).build();
  }

  /**
   * PATCH one piece of furniture to the 'sold' status. (specialSalePrice is optional in the request
   * body)
   *
   * @param furnitureId : the furniture id.
   * @param reqNode     : the request body as JsonNode
   * @return http response containing the modified resource
   */
  @PATCH
  @Path("/sold/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response toSold(@PathParam("id") int furnitureId, JsonNode reqNode) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME)
        .log(Level.INFO, "PATCH /furniture/sold/" + furnitureId);
    if (reqNode == null || reqNode.get("buyerUsername") == null) {
      throw new BadRequestException("Error: malformed request");
    }
    String buyerUsername = reqNode.get("buyerUsername").asText();
    Double specialSalePrice = null;
    if (reqNode.get("specialSalePrice") != null) {
      specialSalePrice = reqNode.get("specialSalePrice").asDouble();
    }
    FurnitureDTO furnitureDTO = furnitureUCC.toSold(furnitureId, buyerUsername, specialSalePrice);
    return Response.ok(Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class)).build();
  }

  /**
   * PATCH one piece of furniture's favourite photo.
   *
   * @param furnitureId : the furniture id
   * @param reqNode     : request body
   * @return http response containing updated furniture resource
   */
  @PATCH
  @Path("/favouritePhoto/{furnitureId}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateFavouritePhoto(@PathParam("furnitureId") int furnitureId,
      JsonNode reqNode) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME)
        .log(Level.INFO, "PATCH /furniture/favouritePhoto/" + furnitureId);
    if (reqNode == null || reqNode.get("photoId") == null) {
      throw new BadRequestException("Error: malformed request");
    }
    int photoId = reqNode.get("photoId").asInt();
    FurnitureDTO furnitureDTO = furnitureUCC.updateFavouritePhoto(furnitureId, photoId);
    furnitureDTO = Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class);
    return Response.ok(furnitureDTO).build();
  }

  /**
   * PATCH one piece of furniture's information. No values are mandatory in the request body, but at
   * least on has to be not null.
   *
   * @param id      : furniture id
   * @param bodyDTO : request body as FurnitureDTO
   * @return http response containing modified resource
   */
  @PATCH
  @Path("/infos/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateInfos(@PathParam("id") int id, FurnitureDTO bodyDTO) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "PATCH furniture/infos/" + id);
    if (bodyDTO.getDescription() == null && bodyDTO.getTypeId() == null
        && bodyDTO.getSellingPrice() == null) {
      throw new BadRequestException("Error: lack of at least one non-null body value");
    }
    bodyDTO.setFurnitureId(id);
    FurnitureDTO furnitureDTO = furnitureUCC.updateInfos(bodyDTO);
    return Response.ok(Json.filterAdminOnlyJsonView(furnitureDTO, FurnitureDTO.class)).build();
  }
}

