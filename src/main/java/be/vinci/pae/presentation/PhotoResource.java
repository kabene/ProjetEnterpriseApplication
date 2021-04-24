package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.PhotoUCC;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.main.Main;
import be.vinci.pae.presentation.filters.Admin;
import be.vinci.pae.utils.Json;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPresentHomePage(UserDTO user) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /photo/homePage");
    List<PhotoDTO> photoDTOS = photoUCC.getAllHomePageVisiblePhotos();
    List<PhotoDTO> res = new ArrayList<>(photoDTOS);
    return Response.ok(res).build();
  }

  /**
   * POST Add photo to the db.
   *
   * @param photo photo input.
   * @return status code.
   */
  @POST
  @Path("/add")
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


}
