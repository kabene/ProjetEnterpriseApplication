package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.ucc.PhotoUCC;
import be.vinci.pae.exceptions.BadRequestException;
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

    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "isVisible: " + isVisible + "  -  isOnHomePage: " + isOnHomePage);

    PhotoDTO dto = photoUCC.patchDisplayFlags(id, isVisible, isOnHomePage);
    return Response.ok(Json.filterAdminOnlyJsonView(dto, PhotoDTO.class)).build();
  }
}
