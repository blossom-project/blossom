package com.blossomproject.core.common.utils.mail;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.Async;

import javax.mail.internet.InternetAddress;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Async
public abstract class DeprecatedMailSenderImpl implements DeprecatedMailSender {

  protected abstract BlossomMailBuilder builder();

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String... mailTo) throws Exception {
    BlossomMailBuilder builder = defaultArgs(htmlTemplate, ctx, mailSubject);
    recipients(builder, mailTo).build().send();
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String... mailTo) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, locale, Lists.newArrayList(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, String... mailTo) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, locale, attachedFiles, mailTo, null, null);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
    BlossomMailBuilder builder = defaultArgs(htmlTemplate, ctx, mailSubject);
    builder = recipients(builder, mailTo);
    recipients(builder, mailCc, mailBcc).build().send();
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, locale, Lists.newArrayList(), mailTo, mailCc, mailBcc);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, locale, attachedFiles, mailTo, mailCc, mailBcc, false);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, String[] mailTo, String[] mailCc, String[] mailBcc, boolean highPriority) throws Exception {
    BlossomMailBuilder builder = defaultArgs(htmlTemplate, ctx, mailSubject);
    builder = recipients(builder, mailTo);
    builder = recipients(builder, mailCc, mailBcc);
    attachments(builder, attachedFiles)
      .locale(locale)
      .highPriority(highPriority)
      .build().send();
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String attachmentName, InputStreamSource attachmentInputStreamSource, String attachmentContentType, String[] mailTo, String[] mailCc, String[] mailBcc, boolean highPriority) throws Exception {
    BlossomMailBuilder builder = defaultArgs(htmlTemplate, ctx, mailSubject);
    builder = recipients(builder, mailTo);
    builder = recipients(builder, mailCc, mailBcc);
    attachments(builder, attachmentName, attachmentInputStreamSource, attachmentContentType)
      .locale(locale)
      .highPriority(highPriority)
      .build().send();
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, InternetAddress... mailTo)
    throws Exception {
    BlossomMailBuilder builder = defaultArgs(htmlTemplate, ctx, mailSubject);
    recipients(builder, mailTo).build().send();
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, InternetAddress... mailTo) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, locale, Lists.newArrayList(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, InternetAddress... mailTo) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, locale, attachedFiles, mailTo, null, null);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception {
    BlossomMailBuilder builder = defaultArgs(htmlTemplate, ctx, mailSubject);
    builder = recipients(builder, mailTo);
    recipients(builder, mailCc, mailBcc).build().send();
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, locale, Lists.newArrayList(), mailTo, mailCc, mailBcc);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, locale, attachedFiles, mailTo, mailCc, mailBcc, false);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, List<File> attachedFiles, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc, boolean highPriority) throws Exception {
    BlossomMailBuilder builder = defaultArgs(htmlTemplate, ctx, mailSubject);
    builder = recipients(builder, mailTo);
    builder = recipients(builder, mailCc, mailBcc);
    attachments(builder, attachedFiles)
      .locale(locale)
      .highPriority(highPriority)
      .build().send();
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String attachmentName, InputStreamSource attachmentInputStreamSource, String attachmentContentType, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc, boolean highPriority) throws Exception {
    BlossomMailBuilder builder = defaultArgs(htmlTemplate, ctx, mailSubject);
    builder = recipients(builder, mailTo);
    builder = recipients(builder, mailCc, mailBcc);
    attachments(builder, attachmentName, attachmentInputStreamSource, attachmentContentType)
      .locale(locale)
      .highPriority(highPriority)
      .build().send();
  }

  private BlossomMailBuilder defaultArgs(String htmlTemplate, Map<String, Object> ctx, String mailSubject) {
    Preconditions.checkArgument(ctx != null);
    return builder()
      .htmlTemplate(htmlTemplate)
      .addContext(ctx)
      .mailSubject(mailSubject);
  }

  private BlossomMailBuilder recipients(BlossomMailBuilder builder, String[] mailTo) throws Exception {
    if (mailTo != null) {
      for (String to : mailTo) {
        builder = builder.addTo(to);
      }
    }
    return builder;
  }

  private BlossomMailBuilder recipients(BlossomMailBuilder builder, String[] mailCc, String[] mailBcc) throws Exception {
    if (mailCc != null) {
      for (String cc : mailCc) {
        builder = builder.addCc(cc);
      }
    }
    if (mailBcc != null) {
      for (String bcc : mailBcc) {
        builder = builder.addBcc(bcc);
      }
    }
    return builder;
  }

  private BlossomMailBuilder recipients(BlossomMailBuilder builder, InternetAddress[] mailTo) {
    if (mailTo != null) {
      for (InternetAddress to : mailTo) {
        builder = builder.addTo(to);
      }
    }
    return builder;
  }

  private BlossomMailBuilder recipients(BlossomMailBuilder builder, InternetAddress[] mailCc, InternetAddress[] mailBcc) {
    if (mailCc != null) {
      for (InternetAddress cc : mailCc) {
        builder = builder.addCc(cc);
      }
    }
    if (mailBcc != null) {
      for (InternetAddress bcc : mailBcc) {
        builder = builder.addBcc(bcc);
      }
    }
    return builder;
  }

  private BlossomMailBuilder attachments(BlossomMailBuilder builder, List<File> attachedFiles) {
    if (attachedFiles != null) {
      for (File file : attachedFiles) {
        builder = builder.addAttachment(file);
      }
    }
    return builder;
  }

  private BlossomMailBuilder attachments(BlossomMailBuilder builder, String attachmentName, InputStreamSource attachmentInputStreamSource, String attachmentContentType) {
    return builder.addAttachment(attachmentName, attachmentInputStreamSource, attachmentContentType);
  }

}
