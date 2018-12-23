package com.blossomproject.core.common.utils.mail;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Deprecated filter applied to a MimeMessageHelper just before sending it in
 * MailSender.
 * <p>
 * For current recipient filtering functionality, use the BlossomMailBuilder methods
 *
 * @see BlossomMailBuilder#addFilter
 */
@Deprecated
public interface MailFilter {
  MimeMessage filter(MimeMessageHelper mimeMessageHelper) throws MessagingException;
}
