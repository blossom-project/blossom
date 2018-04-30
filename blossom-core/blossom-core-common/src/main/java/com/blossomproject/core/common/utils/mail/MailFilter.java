package com.blossomproject.core.common.utils.mail;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MailFilter {
    MimeMessage filter(MimeMessageHelper mimeMessageHelper) throws MessagingException;
}
