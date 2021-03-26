package be.vinci.pae.exceptions;

public abstract class BusinessException extends RuntimeException {


  public BusinessException() {
    super();
  }

  public BusinessException(String message) {
    super(message);
  }

}
