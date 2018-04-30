package com.blossomproject.core.common.utils.mail;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, String[] mailTo, String[] mailCc, String[] mailBcc, boolean highPriority) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
    String... mailTo) throws Exception {
    if (LOGGER.isInfoEnabled()) {

      LOGGER.info(
        "A mail with recipient(s) '{}' and subject '{}' was not sent because no java mail sender is configured",
        Arrays.toString(mailTo), mailSubject);
    }
  }
}
