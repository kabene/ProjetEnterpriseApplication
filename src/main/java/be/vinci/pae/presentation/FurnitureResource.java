package be.vinci.pae.presentation;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Singleton
@Path("/furniture")
public class FurnitureResource {

  /**
   * GET a specific piece of furniture's public details (no authentication required).
   *
   * @param id : the furniture id from the request path
   * @return http response containing a piece of furniture (or relevant status code in case of error)
   */
  @GET
  @Path("/{id}")
  public Response getById(@PathParam("id") int id) {
    System.out.println("get " + id);
    return Response.ok().build();
  }

}
