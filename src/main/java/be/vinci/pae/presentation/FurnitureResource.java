package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.ucc.FurnitureUCC;
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

@Singleton
@Path("/furniture")
public class FurnitureResource {

  @Inject
  private FurnitureUCC furnitureUCC;

  /**
   * GET a specific piece of furniture's public details (no authentication required).
   *
   * @param id : the furniture id from the request path
   * @return http response containing a piece of furniture (or relevant status code in if error)
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getById(@PathParam("id") int id) {
    FurnitureDTO furnitureDTO = furnitureUCC.getOne(id);

    furnitureDTO = Json.filterPublicJsonView(furnitureDTO, FurnitureDTO.class);
    return Response.ok(furnitureDTO).build();
  }

  @GET
  @Path("/detail")
  @Admin
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDetailAll() {
    List<FurnitureDTO> furnitureDTOs = furnitureUCC.getAll();

    List<FurnitureDTO> res = new ArrayList<>();
    for (FurnitureDTO dto : furnitureDTOs) {
      FurnitureDTO filteredDTO = Json.filterAdminOnlyJsonView(dto, FurnitureDTO.class);
      res.add(filteredDTO);
    }
    return Response.ok(res).build();
  }
}
