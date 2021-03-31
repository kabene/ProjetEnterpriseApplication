package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.OptionUCC;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.main.Main;
import be.vinci.pae.persistence.dao.UserDAO;
import be.vinci.pae.presentation.filters.Authorize;
import be.vinci.pae.utils.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.server.ContainerRequest;


@Singleton
@Path("/option")
public class OptionRessouce {

  @Inject
  private OptionUCC optionUCC;
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @POST
  @Path("/introduce")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public Response introduce(JsonNode reqNode,@Context ContainerRequest request){
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "POST /option/introduce");
    UserDTO currentUser= (UserDTO) request.getProperty("user");
    JsonNode furniture_Id = reqNode.get("furnitureId");
    if(furniture_Id==null){
      throw new BadRequestException("Error: Malformed request");
    }
    int furnitureId=furniture_Id.asInt();
    OptionDTO optionDTO = optionUCC.introduceOption(currentUser,furnitureId);
    ObjectNode resNode = jsonMapper.createObjectNode().putPOJO("option",optionDTO);
    return Response.ok(resNode,MediaType.APPLICATION_JSON).build();
  }

  @PATCH
  @Path("/cancel")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public Response cancel(JsonNode reqNode,@Context ContainerRequest request){
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "PATCH /option/cancel");
    UserDTO currentUser= (UserDTO) request.getProperty("user");
    JsonNode option_Id = reqNode.get("optionId");
    if(option_Id==null){
      throw new BadRequestException("Error: Malformed request");
    }
    int optionId=option_Id.asInt();
    OptionDTO optionDTO=optionUCC.cancelOption(currentUser,optionId);
    optionDTO=Json.filterPublicJsonView(optionDTO,OptionDTO.class);
    return Response.ok(optionDTO,MediaType.APPLICATION_JSON).build();
  }


}
