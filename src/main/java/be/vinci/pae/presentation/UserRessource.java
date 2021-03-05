package be.vinci.pae.presentation;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factories.UserFactory;
import be.vinci.pae.business.pojos.User;
import be.vinci.pae.business.ucc.UserUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/users")
public class UserRessource {

  @Inject
  private UserFactory userFactory;

  @Inject
  private UserUCC userUCC;

  @POST
  @Path("login")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO login(User user) {
    if (user.getUsername() == null || user.getPassword() == null)
      return null;
    UserDTO res = userUCC.login(user.getUsername(), user.getPassword());
    return res;
  }

}
