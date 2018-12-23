package com.blossomproject.core.common.utils.mail;

import org.springframework.core.io.InputStreamSource;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public interface BlossomMailBuilder {

  /**
   * Add text body as a freemarker template
   *
   * @param textTemplate Name of a freemarker template relative to classpath:/templates/mail
   */
  BlossomMailBuilder textTemplate(String textTemplate);

  /**
   * Add text body as is
   *
   * @param textBody Content of the text body
   */
  BlossomMailBuilder textBody(String textBody);

  /**
   * Add text body as a freemarker template
   *
   * @param htmlTemplate Name of a freemarker template relative to classpath:/templates/mail
   */
  BlossomMailBuilder htmlTemplate(String htmlTemplate);

  /**
   * Add HTML body as is
   *
   * @param htmlBody Content of the HTML body
   */
  BlossomMailBuilder htmlBody(String htmlBody);

  /**
   * Set mail's subject
   *
   * @param mailSubject Mail subject
   */
  BlossomMailBuilder mailSubject(String mailSubject);

  /**
   * Add key/value pair to the Freemarker context for body rendering
   *
   * @param key   Key in the freemarker template
   * @param value Value to render in the template
   */
  BlossomMailBuilder addContext(String key, Object value);

  /**
   * Add a map of key/value pairs to the Freemarker context for body rendering
   *
   * @param context Context to get key/value pairs from
   */
  BlossomMailBuilder addContext(Map<String, Object> context);

  /**
   * Set the locale for freemarker templates traductions
   *
   * @param locale Locale to use in templates
   */
  BlossomMailBuilder locale(Locale locale);

  /**
   * Set the email as high priority
   *
   * @param highPriority true for high priority
   */
  BlossomMailBuilder highPriority(Boolean highPriority);

  /**
   * Set mail from
   *
   * @param from Address to send mail as
   */
  BlossomMailBuilder from(InternetAddress from);

  /**
   * Set mail from
   *
   * @param address Address to send mail as
   * @throws AddressException if the parse failed
   */
  BlossomMailBuilder from(String address) throws AddressException;

  /**
   * Set mail from
   *
   * @param address Address to send mail as
   * @param name    Name to send mail as
   * @throws UnsupportedEncodingException if the personal name can't be encoded in the default charset
   */
  BlossomMailBuilder from(String address, String name) throws UnsupportedEncodingException;

  /**
   * Set mail reply to
   *
   * @param replyTo Address to reply to
   */
  BlossomMailBuilder replyTo(InternetAddress replyTo);

  /**
   * Set mail reply to
   *
   * @param address Address to reply to
   * @throws AddressException if the parse failed
   */
  BlossomMailBuilder replyTo(String address) throws AddressException;

  /**
   * Set mail reply to
   *
   * @param address Address to reply to
   * @param name    Name to send reply to
   * @throws UnsupportedEncodingException if the personal name can't be encoded in the default charset
   */
  BlossomMailBuilder replyTo(String address, String name) throws UnsupportedEncodingException;

  /**
   * Add attachment as file
   *
   * @param file File to add as attachment
   */
  BlossomMailBuilder addAttachment(File file);

  /**
   * Add attachment as Spring's InputStreamSource
   *
   * @param filename          Filename to use for the attachment
   * @param inputStreamSource InputStreamSource for the attachment content
   * @param contentType       Attachment content type
   */
  BlossomMailBuilder addAttachment(String filename, InputStreamSource inputStreamSource, String contentType);

  /**
   * Add attachment as Blossom's BlossomMailAttachment
   *
   * @param attachment Attachment to add
   */
  BlossomMailBuilder addAttachment(BlossomMailAttachment attachment);

  /**
   * Add regex filter for the mail's recipients
   * Recipients's email addresses that do not match at least one filter will not receive the email
   *
   * @param filter Filter regex
   */
  BlossomMailBuilder addFilter(String filter);

  /**
   * Add regex filter for the mail's recipients
   * Recipients's email addresses that do not match at least one filter will not receive the email
   *
   * @param regex Filter regex
   */
  BlossomMailBuilder addFilter(Pattern regex);

  /**
   * Add mail TO recipient
   *
   * @param address TO recipient address
   */
  BlossomMailBuilder addTo(InternetAddress address);

  /**
   * Add mail TO recipient
   *
   * @param address TO recipient address
   * @throws AddressException if the parse failed
   */
  BlossomMailBuilder addTo(String address) throws AddressException;

  /**
   * Add mail TO recipient
   *
   * @param address TO recipient address
   * @param name    TO recipient name
   * @throws UnsupportedEncodingException if the personal name can't be encoded in the default charset
   */
  BlossomMailBuilder addTo(String address, String name) throws UnsupportedEncodingException;

  /**
   * Add mail CC recipient
   *
   * @param address CC recipient address
   */
  BlossomMailBuilder addCc(InternetAddress address);

  /**
   * Add mail CC recipient
   *
   * @param address CC recipient address
   * @throws AddressException if the parse failed
   */
  BlossomMailBuilder addCc(String address) throws AddressException;

  /**
   * Add mail CC recipient
   *
   * @param address CC recipient address
   * @param name    CC recipient name
   * @throws UnsupportedEncodingException if the personal name can't be encoded in the default charset
   */
  BlossomMailBuilder addCc(String address, String name) throws UnsupportedEncodingException;

  /**
   * Add mail BCC recipient
   *
   * @param address BCC recipient address
   */
  BlossomMailBuilder addBcc(InternetAddress address);

  /**
   * Add mail BCC recipient
   *
   * @param address BCC recipient address
   * @throws AddressException if the parse failed
   */
  BlossomMailBuilder addBcc(String address) throws AddressException;

  /**
   * Add mail BCC recipient
   *
   * @param address BCC recipient address
   * @param name    CC recipient name
   * @throws UnsupportedEncodingException if the personal name can't be encoded in the default charset
   */
  BlossomMailBuilder addBcc(String address, String name) throws UnsupportedEncodingException;

  /**
   * Build BlossomMail from this builder
   */
  BlossomMail build();

}
