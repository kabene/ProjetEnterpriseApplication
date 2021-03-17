package be.vinci.pae.exceptions;

public class TakenException extends RuntimeException {

  /**
   * Exception generated when a field is filled with some unavailable content.
   */
  public TakenException() {
    super();
  }

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link #getMessage()} method.
   */
  public TakenException(String message) {
    super(message);
    System.err.println(message);
  }
}
