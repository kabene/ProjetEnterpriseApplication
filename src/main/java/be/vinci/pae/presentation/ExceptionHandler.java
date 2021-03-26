package be.vinci.pae.presentation;

import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.exceptions.ForbiddenException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.main.Main;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ExceptionHandler implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    logThrowable(exception);
    return Response.status(getStatusCode(exception)).entity(getEntity(exception)).build();
  }

  private int getStatusCode(Throwable e) {
    if (e instanceof BadRequestException) {
      return 400;
    }
    if (e instanceof UnauthorizedException) {
      return 401;
    }
    if (e instanceof ForbiddenException) {
      return 403;
    }
    if (e instanceof NotFoundException) {
      return 404;
    }
    if (e instanceof ConflictException) {
      return 409;
    }

    return 500;
  }

  private Object getEntity(Throwable e) {
    if (e instanceof BusinessException) {
      return e.getMessage();
    }
    // internal error -> hide message
    return "Server Internal Error";
  }

  private void logThrowable(Throwable e) {
    Logger logger = Logger.getLogger(Main.CONSOLE_LOGGER_NAME);
    if (e instanceof InternalError) {
      logger.log(Level.SEVERE, "InternalError: {0}", e.getStackTrace());
    } else {
      logger.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
    }
  }
}