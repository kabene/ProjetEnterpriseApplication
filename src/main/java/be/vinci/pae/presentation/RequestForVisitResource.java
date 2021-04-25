package be.vinci.pae.presentation;

import be.vinci.pae.business.ucc.RequestForVisitUCC;
import be.vinci.pae.presentation.filters.Admin;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Singleton
@Path("/requestForVisit")
public class RequestForVisitResource {

  @Inject
  RequestForVisitUCC requestForVisitUCC;

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Admin
  public Response getAll() {

    return null;
  }
}
