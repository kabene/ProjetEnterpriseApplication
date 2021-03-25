package be.vinci.pae.presentation;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    System.out.println("exception handler visited");
    exception.printStackTrace();
    return Response.status(getStatusCode(exception)).entity(getEntity(exception)).build();
  }

  private int getStatusCode(Throwable e) {
    return 400; // TODO: stub
  }

  private Object getEntity(Throwable e) {
    return e.getMessage();
  }
}