package be.vinci.pae.exceptions;

public class NotFoundException extends BusinessException{
  /**
   * Exception generated when accessing an non existing resource.
   */
  public NotFoundException() {
    super();
  }

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link #getMessage()} method.
   */
  public NotFoundException(String message) {
    super(message);
  }
}
