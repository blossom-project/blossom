package com.blossomproject.core.common.utils.mail;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.mail.internet.InternetAddress;
import org.springframework.core.io.InputStreamSource;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Deprecated
public interface DeprecatedMailSender {

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String... mailTo) throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String... mailTo)
    throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
    List<File> attachedFiles, String... mailTo) throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String[] mailTo,  String[] mailCc, String[] mailBcc)
    throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
    List<File> attachedFiles, String[] mailTo, String[] mailCc, String[] mailBcc) throws Exception;

  @Deprecated
  void sendMail (String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
    List<File> attachedFiles, String[] mailTo, String[] mailCc, String[] mailBcc, boolean highPriority) throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String attachmentName, InputStreamSource attachmentInputStreamSource,
    String attachmentContentType, String[] mailTo, String[] mailCc, String[] mailBcc, boolean highPriority ) throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, InternetAddress... mailTo) throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, InternetAddress... mailTo)
    throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
    List<File> attachedFiles, InternetAddress... mailTo) throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, InternetAddress[] mailTo,  InternetAddress[] mailCc, InternetAddress[] mailBcc)
    throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
    List<File> attachedFiles, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc) throws Exception;

  @Deprecated
  void sendMail (String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
    List<File> attachedFiles, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc, boolean highPriority) throws Exception;

  @Deprecated
  void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String attachmentName, InputStreamSource attachmentInputStreamSource,
    String attachmentContentType, InternetAddress[] mailTo, InternetAddress[] mailCc, InternetAddress[] mailBcc, boolean highPriority ) throws Exception;

}
