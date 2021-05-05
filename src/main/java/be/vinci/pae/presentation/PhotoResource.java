package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.PhotoUCC;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.main.Main;
import be.vinci.pae.presentation.filters.Admin;
import be.vinci.pae.presentation.filters.AllowTakeover;
import be.vinci.pae.presentation.filters.Authorize;
import be.vinci.pae.utils.Json;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.stream.Collectors;
import org.glassfish.jersey.server.ContainerRequest;

@Singleton
@Path("/photos")
public class PhotoResource {

  @Inject
  PhotoUCC photoUCC;

  /**
   * GET all photos that are visible in the home page's carousel.
   *
   * @return http response containing a list full of all the home page photos in json format.
   */
  @GET
  @Path("/homePage")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPresentHomePage() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /photos/homePage");
    List<PhotoDTO> photoDTOS = photoUCC.getAllHomePageVisiblePhotos();
    List<PhotoDTO> res = new ArrayList<>(photoDTOS);
    return Response.ok(Json.filterPublicJsonView(res, List.class)).build();
  }

  /**
   * POST Add photo to the db.
   *
   * @param photo photo input.
   * @return status code.
   */
  @POST
  @Admin
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response add(PhotoDTO photo) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "POST /users/register");
    if (photo == null || photo.getFurnitureId() == null || photo.getSource() == null) {
      throw new BadRequestException("Error: Malformed request");
    }
    PhotoDTO photoDTO = photoUCC.add(photo.getFurnitureId(), photo.getSource());
    return Response.ok(Json.filterAdminOnlyJsonView(photoDTO, PhotoDTO.class)).build();
  }


  /**
   * PATCH one photo's isVisible and isOnHomePage flags.
   *
   * @param id      : the photo's id
   * @param reqNode : request body as JsonNode
   * @return http response containing modified resource
   */
  @PATCH
  @Path("/displayFlags/{id}")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response patchDisplayFlagsById(@PathParam("id") int id, JsonNode reqNode) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "PATCH /photos/visibility/" + id);
    if (reqNode == null || reqNode.get("isVisible") == null) {
      throw new BadRequestException("Error: malformed request");
    }
    if (reqNode == null || reqNode.get("isOnHomePage") == null) {
      throw new BadRequestException("Error: malformed request");
    }
    boolean isVisible = reqNode.get("isVisible").asBoolean();
    boolean isOnHomePage = reqNode.get("isOnHomePage").asBoolean();

    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "isVisible: "
        + isVisible + "  -  isOnHomePage: " + isOnHomePage);

    PhotoDTO dto = photoUCC.patchDisplayFlags(id, isVisible, isOnHomePage);
    return Response.ok(Json.filterAdminOnlyJsonView(dto, PhotoDTO.class)).build();
  }

  /**
   * GET the favouritePhoto for a specific piece of furniture.
   * @param furnitureId : furniture id
   * @return http response containing a photoDTO
   */
  @GET
  @Path("/favourite/{furnitureId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFavByFurnitureId(@PathParam("furnitureId") int furnitureId) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO,
        "GET /photos/favourite/" + furnitureId);
    PhotoDTO photoDTO = photoUCC.getFavourite(furnitureId);
    return Response.ok(Json.filterPublicJsonView(photoDTO, PhotoDTO.class)).build();
  }

  /**
   * GET all visible photos for a specific furnitureId.
   * @param furnitureId : furniture id
   * @return http response containing an array of photoDTO
   */
  @GET
  @Path("/byFurniture/{furnitureId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getByFurnitureId(@PathParam("furnitureId") int furnitureId) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO,
        "GET /photos/byFurniture/" + furnitureId);
    List<PhotoDTO> photoDTOList = photoUCC.getAllForFurniture(furnitureId)
        .stream().filter(PhotoDTO::isVisible).collect(Collectors.toList());
    return Response.ok(Json.filterPublicJsonView(photoDTOList, List.class)).build();
  }

  /**
   * GET all photos for a specific furnitureId.
   * @param furnitureId : furniture id
   * @return http response containing an array of photoDTO
   */
  @GET
  @Path("/byFurniture/all/{furnitureId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getByFurnitureIdAll(@PathParam("furnitureId") int furnitureId) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO,
        "GET /photos/byFurniture/all/" + furnitureId);
    List<PhotoDTO> photoDTOList = photoUCC.getAllForFurniture(furnitureId);
    return Response.ok(Json.filterPublicJsonView(photoDTOList, List.class)).build();
  }

  /**
   * GET all request photos for a specific furniture id.
   * @param furnitureId : furniture id
   * @param request : request context
   * @return http response containing an array of photoDTO
   */
  @GET
  @Authorize
  @AllowTakeover
  @Path("/byFurniture/request/{furnitureId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRequestPhotosByFurnitureId(@PathParam("furnitureId") int furnitureId,
      @Context ContainerRequest request) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO,
        "GET /photos/byFurniture/request/" + furnitureId);
    UserDTO currentUser = (UserDTO) request.getProperty("user");
    List<PhotoDTO> photoDTOList = photoUCC.getRequestPhotos(currentUser, furnitureId);
    return Response.ok(Json.filterPublicJsonView(photoDTOList, List.class)).build();
  }

}
