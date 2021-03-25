package be.vinci.pae.exceptions;

public class BadRequestException extends BusinessException {

  /**
   * Exception generated if a client sends a malformed request.
   */
  public BadRequestException() {
    super();
  }

  /**
   * Constructs a BadRequestException with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link #getMessage()} method.
   */
  public BadRequestException(String message) {
    super(message);
  }

}
