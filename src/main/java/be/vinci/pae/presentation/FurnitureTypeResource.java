package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.FurnitureTypeDTO;
import be.vinci.pae.business.ucc.FurnitureTypeUCC;
import be.vinci.pae.main.Main;
import be.vinci.pae.utils.Json;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Path("/furnitureTypes")
public class FurnitureTypeResource {

  @Inject
  FurnitureTypeUCC furnitureTypeUCC;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response findAll() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /furnitureTypes/");
    List<FurnitureTypeDTO> furnitureTypeDTOList = furnitureTypeUCC.findAll();
    return Response.ok(Json.filterAdminOnlyJsonView(furnitureTypeDTOList, List.class)).build();
  }
}
