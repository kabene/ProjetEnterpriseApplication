package be.vinci.pae.exceptions;

public class ForbiddenException extends BusinessException {

  /**
   * Exception generated if the client asks to be granted access using invalid credentials.
   */
  public ForbiddenException() {
    super();
  }

  /**
   * Constructs a new ForbiddenException with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link #getMessage()} method.
   */
  public ForbiddenException(String message) {
    super(message);
  }

}
