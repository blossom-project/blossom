package fr.mgargadennec.blossom.core.common.support.exception;

public class BlossomException extends Exception {

  private static final long serialVersionUID = -1L;

  private final String message;
  private final Throwable cause;

  public BlossomException() {
    this.message = "An error has occured";
    this.cause = null;
  }

  public BlossomException(String message) {
    this.message = message;
    this.cause = null;
  }

  public BlossomException(String message, Throwable cause) {
    this.message = message;
    this.cause = cause;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Throwable#getCause()
   */
  @Override
  public synchronized Throwable getCause() {
    return this.cause;
  }

  // GETTERS ET SETTERS
  @Override
  public String getMessage() {
    return message;
  }
}
