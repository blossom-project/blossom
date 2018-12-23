package com.blossomproject.core.common.utils.mail;

import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

public interface AsyncMailSender {

  @Async
  ListenableFuture<BlossomMail> asyncSend(BlossomMail mail);

}
