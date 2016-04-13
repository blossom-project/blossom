package fr.mgargadennec.blossom.core.common.support.history.builder.exception;

import fr.mgargadennec.blossom.core.common.support.exception.BlossomException;

@SuppressWarnings("serial")
public class BlossomHistoryTechnicalException extends BlossomException {

  public BlossomHistoryTechnicalException(String message) {
    super(message);
  }

  public BlossomHistoryTechnicalException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
