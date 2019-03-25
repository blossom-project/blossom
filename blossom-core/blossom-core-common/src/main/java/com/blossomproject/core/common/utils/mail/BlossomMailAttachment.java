package com.blossomproject.core.common.utils.mail;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;

/**
 * An attachment before being added to a sent mail.
 * @see MimeMessageHelper#addAttachment
 *
 * @author rlejolivet
 */
public interface BlossomMailAttachment {
  void appendTo(MimeMessageHelper mimeMessageHelper) throws MessagingException;
}
