package com.blossomproject.core.common.utils.mail;

/**
 * Produce builders able to send emails
 *
 * @author rlejolivet
 * @see BlossomMailBuilder
 */
public interface MailSender extends DeprecatedMailSender {

  BlossomMailBuilder builder();

}
