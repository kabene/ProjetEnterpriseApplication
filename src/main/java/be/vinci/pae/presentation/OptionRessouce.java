package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.OptionDTO;
import be.vinci.pae.business.ucc.OptionUCC;
import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.main.Main;
import be.vinci.pae.utils.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;



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
  public Response introduce(OptionDTO option){
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "POST /option/introduce");
    if(option==null||option.getDuree()==0||option.getDateOption() ==null||option.getClientId()==0||option.getFurnitureId()==0){
      throw new BadRequestException("Error :Malformed request");
    }
    OptionDTO optionDTO = optionUCC.introduceOption(option);
    //SHOULD I FILTER THIS?
    ObjectNode resNode = jsonMapper.createObjectNode().putPOJO("option",optionDTO);
    return Response.ok(resNode,MediaType.APPLICATION_JSON).build();
  }

  @GET
  @Path("/option/cancel/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response cancel(@PathParam("id") int id){
    Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO, "GET /cancel/detail/"+id);
    OptionDTO optionDTO=optionUCC.cancelOption(id);
    //FILTERS ???
    return Response.ok(optionDTO).build();
  }


}
