package com.blossomproject.core.common.utils.mail;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import java.io.File;

public class FileMailAttachment implements BlossomMailAttachment {

  private final File file;

  public FileMailAttachment(File file) {
    this.file = file;
  }

  @Override
  public void appendTo(MimeMessageHelper mimeMessageHelper) throws MessagingException {
    mimeMessageHelper.addAttachment(file.getName(), file);
  }
}
