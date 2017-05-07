package fr.mgargadennec.blossom.core.common.utils.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
  private final String from;
  private final String basePath;

  public MailSenderImpl(JavaMailSender javaMailSender, Configuration freemarkerConfiguration, Set<String> filters, String from, String basePath) {
    this.javaMailSender = javaMailSender;
    this.freemarkerConfiguration = freemarkerConfiguration;
    this.filters = filters;
    this.from = from;
    this.basePath = basePath;
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String... mailTo) throws Exception {

    this.enrichContext(ctx);

    final Template template = this.freemarkerConfiguration.getTemplate("mail/" + htmlTemplate + ".ftl");
    final String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, ctx);

    String[] tos = this.filterMails(mailTo);
    if (tos != null && tos.length > 0) {
      final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
      final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      message.setSubject(mailSubject);
      message.setText(htmlContent, true);
      message.setFrom(this.from);
      message.setTo(tos);
      this.javaMailSender.send(mimeMessage);
    } else {
      LOGGER.info("A mail whose subject was {} was not sent because the recipient adresses were filtered by mailfilters {}",
        Arrays.toString(mailTo),
        mailSubject,
        this.filters);
    }
  }

  private void enrichContext(Map<String, Object> ctx) {
    ctx.put("basePath", this.basePath);
  }

  private String[] filterMails(String[] mailTo) {
    if (mailTo != null && this.filters != null && !this.filters.isEmpty()) {
      List<String> matched = Stream.of(mailTo).filter(s -> this.filters.stream().anyMatch(filter -> s.matches(filter))).collect(Collectors.toList());
      return matched.toArray(new String[matched.size()]);
    } else {
      return mailTo;
    }
  }
}
