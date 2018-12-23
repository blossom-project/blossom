package com.blossomproject.core.common.utils.mail;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;

public interface BlossomMailAttachment {
  void appendTo(MimeMessageHelper mimeMessageHelper) throws MessagingException;
}
