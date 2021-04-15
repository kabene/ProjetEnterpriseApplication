package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.PhotoDTO;
import be.vinci.pae.business.ucc.PhotoUCC;
import be.vinci.pae.main.Main;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
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
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPresentHomePage() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /photo/homePage");
    List<PhotoDTO> photoDTOS = photoUCC.getAllHomePageVisiblePhotos();
    List<PhotoDTO> res = new ArrayList<>(photoDTOS);
    return Response.ok(res).build();
  }

}
