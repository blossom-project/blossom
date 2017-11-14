package fr.blossom.core.common.utils.mail;

import com.google.common.base.Preconditions;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
public class MailSenderImpl implements MailSender {
  private final static Logger LOGGER = LoggerFactory.getLogger(MailSenderImpl.class);

  private final JavaMailSender javaMailSender;
  private final Configuration freemarkerConfiguration;
  private final Set<String> filters;
  private final MessageSource messageSource;
  private final String from;
  private final String basePath;

  public MailSenderImpl(JavaMailSender javaMailSender, Configuration freemarkerConfiguration, Set<String> filters,
    MessageSource messageSource, String from, String basePath) {
    this.javaMailSender = javaMailSender;
    this.freemarkerConfiguration = freemarkerConfiguration;
    this.filters = filters;
    this.messageSource = messageSource;
    this.from = from;
    this.basePath = basePath;
  }

  @Override public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String... mailTo)
    throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale,
    String... mailTo) throws Exception {
    this.enrichContext(ctx, locale);

    final Template template = this.freemarkerConfiguration.getTemplate("mail/" + htmlTemplate + ".ftl");
    final String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, ctx);
    final String subject = this.messageSource.getMessage(mailSubject, new Object[]{}, mailSubject, locale);

    String[] tos = this.filterMails(mailTo);
    if (tos != null && tos.length > 0) {
      final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
      final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      message.setSubject(subject);
      message.setText(htmlContent, true);
      message.setFrom(this.from);
      message.setTo(tos);
      this.javaMailSender.send(mimeMessage);
    } else {
      LOGGER
        .info("A mail whose subject was {} was not sent because the recipient adresses were filtered by mailfilters {}",
          Arrays.toString(mailTo), mailSubject, this.filters);
    }
  }

  private void enrichContext(Map<String, Object> ctx, Locale locale) {
    Preconditions.checkNotNull(locale);
    Preconditions.checkNotNull(ctx);

    ctx.put("basePath", this.basePath);
    ctx.put("message", new MessageResolverMethod(this.messageSource, locale));
    ctx.put("lang", locale);
  }

  private String[] filterMails(String[] mailTo) {
    if (mailTo != null && this.filters != null && !this.filters.isEmpty()) {
      List<String> matched = Stream.of(mailTo).filter(s -> this.filters.stream().anyMatch(filter -> s.matches(filter)))
        .collect(Collectors.toList());
      return matched.toArray(new String[matched.size()]);
    } else {
      return mailTo;
    }
  }

  private class MessageResolverMethod implements TemplateMethodModelEx {

    private MessageSource messageSource;
    private Locale locale;

    public MessageResolverMethod(MessageSource messageSource, Locale locale) {
      this.messageSource = messageSource;
      this.locale = locale;
    }

    @Override public Object exec(List arguments) throws TemplateModelException {
      if (arguments.size() != 1) {
        throw new TemplateModelException("Wrong number of arguments");
      }
      String code = ((SimpleScalar) arguments.get(0)).getAsString();
      if (code == null || code.isEmpty()) {
        throw new TemplateModelException("Invalid code value '" + code + "'");
      }
      return messageSource.getMessage(code, null, locale);
    }
  }
}
