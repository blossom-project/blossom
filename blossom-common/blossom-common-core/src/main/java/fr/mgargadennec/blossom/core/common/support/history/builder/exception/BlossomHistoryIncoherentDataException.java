package fr.mgargadennec.blossom.core.common.support.history.builder.exception;

import fr.mgargadennec.blossom.core.common.support.exception.BlossomException;

@SuppressWarnings("serial")
public class BlossomHistoryIncoherentDataException extends BlossomException {

  public BlossomHistoryIncoherentDataException(String message) {
    super(message);
  }

  public BlossomHistoryIncoherentDataException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
