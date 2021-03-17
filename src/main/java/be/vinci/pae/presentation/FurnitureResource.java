package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.FurnitureDTO;
import be.vinci.pae.business.ucc.FurnitureUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Singleton
@Path("/furniture")
public class FurnitureResource {

  @Inject
  private FurnitureUCC furnitureUCC;

  /**
   * GET a specific piece of furniture's public details (no authentication required).
   *
   * @param id : the furniture id from the request path
   * @return http response containing a piece of furniture (or relevant status code in case of error)
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getById(@PathParam("id") int id) {
    System.out.println("get " + id);
    FurnitureDTO furnitureDTO = furnitureUCC.getOne(id);
    furnitureDTO.setFurnitureId(id);
    return Response.ok(furnitureDTO).build();
  }

}
