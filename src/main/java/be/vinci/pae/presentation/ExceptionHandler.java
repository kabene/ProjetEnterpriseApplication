package be.vinci.pae.presentation;

import be.vinci.pae.exceptions.BadRequestException;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.exceptions.ForbiddenException;
import be.vinci.pae.exceptions.NotFoundException;
import be.vinci.pae.exceptions.ConflictException;
import be.vinci.pae.exceptions.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    //TODO: Log
    System.err.println(exception.getMessage());
    exception.printStackTrace();

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
}