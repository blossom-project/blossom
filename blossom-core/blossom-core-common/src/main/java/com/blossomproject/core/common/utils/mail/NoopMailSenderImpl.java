package com.blossomproject.core.common.utils.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamSource;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
public class NoopMailSenderImpl implements MailSender {
  private final static Logger LOGGER = LoggerFactory.getLogger(NoopMailSenderImpl.class);

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String... mailTo)
    throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
                       List<File> attachedFiles, String... mailTo) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), convertToInternetAddress(mailTo));
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), convertToInternetAddress(mailTo));
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), convertToInternetAddress(mailTo));
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), convertToInternetAddress(mailTo));
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, String[] mailTo, String[] mailCc, String[] mailBcc, boolean highPriority) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), convertToInternetAddress(mailTo));
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String attachmentName, InputStreamSource attachmentInputStreamSource, String attachmentContentType, String[] mailTo, String[] mailCc, String[] mailBcc, boolean highPriority) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), convertToInternetAddress(mailTo));
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
                       String... mailTo) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), convertToInternetAddress(mailTo));
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, InternetAddress... mailTo)
    throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
                       List<File> attachedFiles, InternetAddress... mailTo) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc, boolean highPriority) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String attachmentName, InputStreamSource attachmentInputStreamSource, String attachmentContentType, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc, boolean highPriority) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }


  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
                       InternetAddress... mailTo) throws Exception {
    LOGGER.warn(
      "A mail with recipient(s) '{}' and subject '{}' was not sent because no java mail sender is configured",
      Arrays.toString(mailTo), mailSubject);
  }

  private InternetAddress[] convertToInternetAddress(String[] mails) throws AddressException {
    if (mails == null) {
      return null;
    }

    int i = 0;
    InternetAddress[] addresses = new InternetAddress[mails.length];
    for (String mail : mails) {
      addresses[i++] = new InternetAddress(mail);
    }

    return addresses;
  }

}
