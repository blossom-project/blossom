package com.blossomproject.core.common.utils.mail;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.concurrent.ListenableFuture;

public class AsyncMailSenderImpl implements AsyncMailSender {

  @Override
  @Async
  public ListenableFuture<BlossomMail> asyncSend(BlossomMail mail) {
    try {
      mail.send();
    } catch (Exception e) {
      return AsyncResult.forExecutionException(e);
    }
    return new AsyncResult<>(mail);
  }

}
