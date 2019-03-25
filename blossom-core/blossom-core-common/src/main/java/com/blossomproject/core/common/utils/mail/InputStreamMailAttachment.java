package com.blossomproject.core.common.utils.mail;

import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;

public class InputStreamMailAttachment implements BlossomMailAttachment {

  private final String filename;
  private final InputStreamSource source;
  private final String contentType;

  public InputStreamMailAttachment(String filename, InputStreamSource source, String contentType) {
    this.filename = filename;
    this.source = source;
    this.contentType = contentType;
  }

  @Override
  public void appendTo(MimeMessageHelper mimeMessageHelper) throws MessagingException {
    mimeMessageHelper.addAttachment(filename, source, contentType);
  }
}
