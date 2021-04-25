package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.RequestForVisitDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.RequestForVisitUCC;
import be.vinci.pae.main.Main;
import be.vinci.pae.presentation.filters.Admin;
import be.vinci.pae.presentation.filters.Authorize;
import be.vinci.pae.utils.Json;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ContainerRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Path("/requestForVisit")
public class RequestForVisitResource {

  @Inject
  RequestForVisitUCC requestForVisitUCC;


  /**
   * GET all requestForVisit resources.
   *
   * @return list of requestForVisit as json.
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Admin
  public Response getAll() {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /requestForVisit");
    List<RequestForVisitDTO> requestDTOs = requestForVisitUCC.listRequest();
    return Response.ok(getFilteredList(requestDTOs)).build();
  }

  /**
   * GET all requestForVisit resources belongign to a user.
   *
   * @param request the request context
   * @return list of requestForVisit as json.
   */
  @GET
  @Path("/me")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response getAllByUserId(@Context ContainerRequest request) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /requestForVisit/user/me");
    UserDTO currentUser = (UserDTO) request.getProperty("user");
    List<RequestForVisitDTO> requestDTOs = requestForVisitUCC
        .listRequestByUserId(currentUser.getId());
    return Response.ok(getFilteredList(requestDTOs)).build();
  }

  /**
   * PATCH (cancels) a request_for_visit by id.
   *
   * @param requestId the id of the request for visit.
   * @param request   the request context.
   * @return the modified resource as json.
   */
  @PATCH
  @Path("/cancel/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public Response cancel(@PathParam("id") int requestId, @Context ContainerRequest request) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(
        Level.INFO, "GET /requestForVisit/cancel/" + requestId
    );
    UserDTO currentUser = (UserDTO) request.getProperty("user");
    RequestForVisitDTO requestForVisitDTO = requestForVisitUCC
        .cancelRequest(requestId, currentUser.getId());
    requestForVisitDTO = Json.filterAdminOnlyJsonView(requestForVisitDTO, RequestForVisitDTO.class);
    return Response.ok(requestForVisitDTO, MediaType.APPLICATION_JSON).build();
  }

  /**
   * PATCH (accept) a request_for_visit by id.
   *
   * @param requestId the id of the request for visit.
   * @param request   the request context.
   * @return the modified resource as json.
   */
  @PATCH
  @Path("/accept/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public Response accept(@PathParam("id") int requestId, @Context ContainerRequest request) {
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(
        Level.INFO, "GET /requestForVisit/accept/" + requestId
    );
    UserDTO currentUser = (UserDTO) request.getProperty("user");
    RequestForVisitDTO requestForVisitDTO = requestForVisitUCC
        .acceptRequest(requestId, currentUser.getId());
    requestForVisitDTO = Json.filterAdminOnlyJsonView(requestForVisitDTO, RequestForVisitDTO.class);
    return Response.ok(requestForVisitDTO, MediaType.APPLICATION_JSON).build();
  }


  /**
   * filter all the RequestForVisitDTO with Admin view and return a similar list.
   *
   * @param requestDTOs the list of dtos to filter.
   * @return a similar list than the one given in parameter filtered with Admin view.
   */
  private List<RequestForVisitDTO> getFilteredList(List<RequestForVisitDTO> requestDTOs) {
    List<RequestForVisitDTO> res = new ArrayList<RequestForVisitDTO>();
    for (RequestForVisitDTO dto : requestDTOs) {
      RequestForVisitDTO filteredDTO = Json.filterAdminOnlyJsonView(dto, RequestForVisitDTO.class);
      res.add(filteredDTO);
    }
    return res;
  }
}
