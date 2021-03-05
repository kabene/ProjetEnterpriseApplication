package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.pojos.User;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/users")
public class UserRessource {

  /*@Inject
  private UserFactory userFactory;*/

  @POST
  @Path("login")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO login(User user) {
    System.out.println("login from UserResource"); //stub
    return (UserDTO) user;
  }

}
