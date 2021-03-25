package be.vinci.pae.presentation;

import be.vinci.pae.exceptions.DeadlyException;
import be.vinci.pae.exceptions.TakenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    System.err.println(exception.getMessage());
    exception.printStackTrace();
    return Response.status(getStatusCode(exception)).entity(getEntity(exception)).build();
  }

  private int getStatusCode(Throwable e) {
    if(e instanceof TakenException) {
      return 409;
    }
    if(e instanceof DeadlyException) {
      return 500;
    }
    if(e instanceof InternalError) {
      return 500;
    }
    return 500;
  }

  private Object getEntity(Throwable e) {
    return e.getMessage();
  }
}