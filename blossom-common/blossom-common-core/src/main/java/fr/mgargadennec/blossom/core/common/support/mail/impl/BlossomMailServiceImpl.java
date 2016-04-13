package fr.mgargadennec.blossom.core.common.support.mail.impl;

import java.util.Arrays;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import fr.mgargadennec.blossom.core.common.support.mail.IBlossomMailService;

public class BlossomMailServiceImpl implements IBlossomMailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(BlossomMailServiceImpl.class);

  private JavaMailSenderImpl mailSender;
  private SpringTemplateEngine templateEngine;
  private String mailFrom;
  private String[] mailFilters;
  private String basePath;

  public BlossomMailServiceImpl(JavaMailSenderImpl mailSender, SpringTemplateEngine templateEngine, String mailFrom,
      String[] someMailFilters, String basePath) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
    this.mailFrom = mailFrom;
    if (someMailFilters == null) {
      this.mailFilters = null;
    } else {
      this.mailFilters = Arrays.copyOf(someMailFilters, someMailFilters.length);
    }
    this.basePath = basePath;
  }

  public void sendMail(String htmlTemplate, Context ctx, String mailSubject, String... mailTo)
      throws MessagingException {
    enrichContext(ctx);
    final String htmlContent = this.templateEngine.process(htmlTemplate, ctx);

    final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    // Contenu du message
    message.setSubject(mailSubject);
    message.setText(htmlContent, true);

    // Exp�diteur
    message.setFrom(mailFrom);

    // Filtrage Destinataire
    String[] tos = filterMails(mailTo);

    if (tos != null && tos.length > 0) {
      message.setTo(tos);
      this.mailSender.send(mimeMessage);
    } else {
      LOGGER
          .info(
              "Un mail n'a pas �t� envoy� � {} avec comme sujet : '{}' car les destinataires ne sont pas param�tr�s dans les mailFilters {}",
              Arrays.toString(mailTo), mailSubject, Arrays.toString(mailFilters));
    }
  }

  String[] filterMails(String[] mailTo) {
    if (mailTo != null && mailFilters != null && mailFilters.length > 0) {
      Iterable<String> filteredIte = Iterables.filter(Arrays.asList(mailTo), new Predicate<String>() {
        public boolean apply(String input) {
          for (String filter : mailFilters) {
            if (input != null && filter != null && Pattern.matches(filter, input)) {
              return true;
            }
          }
          return false;
        }
      });

      return Iterables.toArray(filteredIte, String.class);
    } else {
      return mailTo;
    }
  }

  private void enrichContext(Context ctx) {
    ctx.setVariable("basePath", basePath);
  }

}
