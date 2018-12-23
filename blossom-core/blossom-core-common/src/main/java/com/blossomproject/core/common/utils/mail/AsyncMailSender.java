package com.blossomproject.core.common.utils.mail;

import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Enable sending a BlossomMail asynchronously for a standard MailSender.
 * This is to circumvent the limitation that AOP cannot be applied inside the
 * calling service, and calling methods on BlossomMail are not Spring-managed
 *
 * @author rlejolivet
 */
public interface AsyncMailSender {

  @Async
  ListenableFuture<BlossomMail> asyncSend(BlossomMail mail);

}
