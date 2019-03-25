package com.blossomproject.core.common.utils.mail;

import org.springframework.util.concurrent.ListenableFuture;

/**
 * An email ready to be sent
 *
 * @author rlejolivet
 */
public interface BlossomMail {

  /**
   * Send the email as is, synchronously.
   */
  void send() throws Exception;

  /**
   * Send the email asynchronously, using an AsyncMailSender
   *
   * @return
   */
  ListenableFuture<BlossomMail> asyncSend();

}
