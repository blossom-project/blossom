package fr.mgargadennec.blossom.core.common.support.history.builder.exception;

import fr.mgargadennec.blossom.core.common.support.exception.BlossomException;

@SuppressWarnings("serial")
public class BlossomHistoryIncompleteDataException extends BlossomException {

  public BlossomHistoryIncompleteDataException(String message) {
    super(message);
  }

  public BlossomHistoryIncompleteDataException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
