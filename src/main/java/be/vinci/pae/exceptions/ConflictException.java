package be.vinci.pae.exceptions;

public class ConflictException extends BusinessException {

  /**
   * Exception generated when a field is filled with some unavailable content.
   */
  public ConflictException() {
    super();
  }

  /**
   * Constructs a new ConflictException with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link #getMessage()} method.
   */
  public ConflictException(String message) {
    super(message);
  }
}
