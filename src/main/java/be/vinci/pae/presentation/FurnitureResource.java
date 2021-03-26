package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.ucc.FurnitureUCC;
import be.vinci.pae.main.Main;
import be.vinci.pae.presentation.filters.Admin;
import be.vinci.pae.utils.Json;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    List<FurnitureDTO> res = new ArrayList<>();
    for (FurnitureDTO dto : furnitureDTOs) {
      FurnitureDTO filteredDTO = Json.filterPublicJsonView(dto, FurnitureDTO.class);
      res.add(filteredDTO);
    }
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
}
