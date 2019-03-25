package com.blossomproject.core.common.utils.mail;

import javax.mail.internet.InternetAddress;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class BlossomMailImpl implements BlossomMail {

  private final String textTemplate;
  private final String textBody;
  private final String htmlTemplate;
  private final String htmlBody;
  private final String mailSubject;
  private final Locale locale;
  private final InternetAddress from;
  private final InternetAddress replyTo;
  private final Boolean highPriority;

  private final Map<String, Object> ctx;
  private final List<BlossomMailAttachment> attachments;

  private final Set<InternetAddress> to;
  private final Set<InternetAddress> cc;
  private final Set<InternetAddress> bcc;

  protected BlossomMailImpl(String textTemplate,
                            String textBody,
                            String htmlTemplate,
                            String htmlBody,
                            String mailSubject,
                            Locale locale,
                            InternetAddress from,
                            InternetAddress replyTo,
                            Boolean highPriority,
                            Map<String, Object> ctx,
                            List<BlossomMailAttachment> attachments,
                            Set<InternetAddress> to,
                            Set<InternetAddress> cc,
                            Set<InternetAddress> bcc) {
    this.textTemplate = textTemplate;
    this.textBody = textBody;
    this.htmlTemplate = htmlTemplate;
    this.htmlBody = htmlBody;
    this.mailSubject = mailSubject;
    this.locale = locale;
    this.from = from;
    this.replyTo = replyTo;
    this.highPriority = highPriority;
    this.ctx = ctx;
    this.attachments = attachments;
    this.to = to;
    this.cc = cc;
    this.bcc = bcc;
  }

  public String getTextTemplate() {
    return textTemplate;
  }

  public String getTextBody() {
    return textBody;
  }

  public String getHtmlTemplate() {
    return htmlTemplate;
  }

  public String getHtmlBody() {
    return htmlBody;
  }

  public String getMailSubject() {
    return mailSubject;
  }

  public Locale getLocale() {
    return locale;
  }

  public InternetAddress getFrom() {
    return from;
  }

  public InternetAddress getReplyTo() {
    return replyTo;
  }

  public Boolean getHighPriority() {
    return highPriority;
  }

  public Map<String, Object> getCtx() {
    return ctx;
  }

  public List<BlossomMailAttachment> getAttachments() {
    return attachments;
  }

  public Set<InternetAddress> getTo() {
    return to;
  }

  public Set<InternetAddress> getCc() {
    return cc;
  }

  public Set<InternetAddress> getBcc() {
    return bcc;
  }
}
