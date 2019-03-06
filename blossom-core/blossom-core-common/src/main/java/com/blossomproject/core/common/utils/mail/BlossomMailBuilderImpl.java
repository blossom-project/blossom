package com.blossomproject.core.common.utils.mail;

import org.springframework.core.io.InputStreamSource;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class BlossomMailBuilderImpl implements BlossomMailBuilder {

  protected String textTemplate;
  protected String textBody;
  protected String htmlTemplate;
  protected String htmlBody;
  protected String mailSubject;
  protected Locale locale = Locale.getDefault();
  protected InternetAddress from;
  protected InternetAddress replyTo;
  protected Boolean highPriority = false;

  protected Map<String, Object> ctx = new HashMap<>();
  protected List<BlossomMailAttachment> attachments = new ArrayList<>();
  protected Set<Pattern> filters = new HashSet<>();

  protected Set<InternetAddress> to = new HashSet<>();
  protected Set<InternetAddress> cc = new HashSet<>();
  protected Set<InternetAddress> bcc = new HashSet<>();

  /*
   * Utils methods
   */

  protected Set<InternetAddress> filter(Set<InternetAddress> recipients) {
    if (filters.isEmpty() || recipients == null) {
      return recipients;
    }
    return recipients.stream()
      .filter(address -> filters.stream().anyMatch(pattern -> pattern.matcher(address.getAddress()).find()))
      .collect(Collectors.toSet());
  }

  /*
   * Builder interface
   */

  @Override
  public BlossomMailBuilder textTemplate(String textTemplate) {
    this.textTemplate = textTemplate;
    return this;
  }

  @Override
  public BlossomMailBuilder textBody(String textBody) {
    this.textBody = textBody;
    return this;
  }

  @Override
  public BlossomMailBuilder htmlTemplate(String htmlTemplate) {
    this.htmlTemplate = htmlTemplate;
    return this;
  }

  @Override
  public BlossomMailBuilder htmlBody(String htmlBody) {
    this.htmlBody = htmlBody;
    return this;
  }

  @Override
  public BlossomMailBuilder mailSubject(String mailSubject) {
    this.mailSubject = mailSubject;
    return this;
  }

  @Override
  public BlossomMailBuilder addContext(String key, Object value) {
    this.ctx.put(key, value);
    return this;
  }

  @Override
  public BlossomMailBuilder addContext(Map<String, Object> context) {
    this.ctx.putAll(context);
    return this;
  }

  @Override
  public BlossomMailBuilder locale(Locale locale) {
    this.locale = locale;
    return this;
  }

  @Override
  public BlossomMailBuilder highPriority(Boolean highPriority) {
    this.highPriority = highPriority;
    return this;
  }

  @Override
  public BlossomMailBuilder from(InternetAddress from) {
    this.from = from;
    return this;
  }

  @Override
  public BlossomMailBuilder from(String address) throws AddressException {
    this.from = new InternetAddress(address);
    return this;
  }

  @Override
  public BlossomMailBuilder from(String address, String name) throws UnsupportedEncodingException {
    this.from = new InternetAddress(address, name);
    return this;
  }

  @Override
  public BlossomMailBuilder replyTo(InternetAddress replyTo) {
    this.replyTo = replyTo;
    return this;
  }

  @Override
  public BlossomMailBuilder replyTo(String address) throws AddressException {
    this.replyTo = new InternetAddress(address);
    return this;
  }

  @Override
  public BlossomMailBuilder replyTo(String address, String name) throws UnsupportedEncodingException {
    this.replyTo = new InternetAddress(address, name);
    return this;
  }

  @Override
  public BlossomMailBuilder addAttachment(File file) {
    this.attachments.add(new FileMailAttachment(file));
    return this;
  }

  @Override
  public BlossomMailBuilder addAttachment(String filename, InputStreamSource inputStreamSource, String contentType) {
    this.attachments.add(new InputStreamMailAttachment(filename, inputStreamSource, contentType));
    return this;
  }

  @Override
  public BlossomMailBuilder addAttachment(BlossomMailAttachment attachment) {
    this.attachments.add(attachment);
    return this;
  }

  @Override
  public BlossomMailBuilder addFilter(String filter) {
    this.filters.add(Pattern.compile(filter));
    return this;
  }

  @Override
  public BlossomMailBuilder addFilter(Pattern regex) {
    this.filters.add(regex);
    return this;
  }

  @Override
  public BlossomMailBuilder addTo(InternetAddress address) {
    this.to.add(address);
    return this;
  }

  @Override
  public BlossomMailBuilder addTo(String address) throws AddressException {
    this.to.add(new InternetAddress(address));
    return this;
  }

  @Override
  public BlossomMailBuilder addTo(String address, String name) throws UnsupportedEncodingException {
    this.to.add(new InternetAddress(address, name));
    return this;
  }

  @Override
  public BlossomMailBuilder addCc(InternetAddress address) {
    this.cc.add(address);
    return this;
  }

  @Override
  public BlossomMailBuilder addCc(String address) throws AddressException {
    this.cc.add(new InternetAddress(address));
    return this;
  }

  @Override
  public BlossomMailBuilder addCc(String address, String name) throws UnsupportedEncodingException {
    this.cc.add(new InternetAddress(address, name));
    return this;
  }

  @Override
  public BlossomMailBuilder addBcc(InternetAddress address) {
    this.bcc.add(address);
    return this;
  }

  @Override
  public BlossomMailBuilder addBcc(String address) throws AddressException {
    this.bcc.add(new InternetAddress(address));
    return this;
  }

  @Override
  public BlossomMailBuilder addBcc(String address, String name) throws UnsupportedEncodingException {
    this.bcc.add(new InternetAddress(address, name));
    return this;
  }
}
