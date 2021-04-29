package be.vinci.pae.presentation.filters;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.main.Main;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Provider
@Priority(1)
@AllowTakeover
public class TakeoverRequestFilter implements ContainerRequestFilter {

  @Inject
  private UserUCC userUCC;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    DecodedJWT decodedToken = UtilsFilters.getDecodedToken(requestContext);
    if (UtilsFilters.isTakeover(decodedToken)) {
      int adminId = decodedToken.getClaim("user").asInt();
      int takeoverId = decodedToken.getClaim("takeover").asInt();
      Logger.getLogger(Main.CONSOLE_LOGGER_NAME).log(Level.INFO,
          "Takeover request detected. admin id: " + adminId + ", takeover id: " + takeoverId);
      //verify admin
      UserDTO adminDTO = userUCC.getOne(adminId);
      if (adminDTO.isWaiting()) {
        throw new UnauthorizedException("Your account is not yet validated");
      }
      boolean isAdmin = adminDTO.getRole().equals("admin");
      if (!isAdmin) {
        throw new UnauthorizedException("Error: Authorisation refused");
      }
      //verify takeover user account
      UserDTO takeoverDTO = userUCC.getOne(takeoverId);
      if (takeoverDTO.isWaiting()) {
        throw new UnauthorizedException("The taken over account is not yet validated");
      }
      boolean isTakingAdminOver = takeoverDTO.getRole().equals("admin");
      if (isTakingAdminOver) {
        throw new UnauthorizedException("Error: Cannot take over an admin account");
      }
      requestContext.setProperty("user", takeoverDTO);
      requestContext.setProperty("taken_over", true);
    }
  }
}
