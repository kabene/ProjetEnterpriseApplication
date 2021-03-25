package be.vinci.pae.exceptions;

public class UnauthorizedException extends BusinessException {

  /**
   * Exception generated if the user is trying to access resources without the required
   * authentication level.
   */
  UnauthorizedException() {
    super();
  }

  /**
   * Constructs a UnauthorizedException with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link #getMessage()} method.
   */
  UnauthorizedException(String message) {
    super(message);
  }

}
