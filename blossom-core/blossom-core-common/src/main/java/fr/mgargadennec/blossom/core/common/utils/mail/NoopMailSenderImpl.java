package fr.mgargadennec.blossom.core.common.utils.mail;

import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
public class NoopMailSenderImpl implements MailSender {
  private final static Logger LOGGER = LoggerFactory.getLogger(NoopMailSenderImpl.class);
  private final Configuration freemarkerConfiguration;
  private final String basePath;

  public NoopMailSenderImpl(Configuration freemarkerConfiguration, String basePath) {
    this.freemarkerConfiguration = freemarkerConfiguration;
    this.basePath = basePath;
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, String... mailTo) throws Exception {
    this.sendMail(htmlTemplate, ctx, mailSubject, Locale.getDefault(), mailTo);
  }

  @Override
  public void sendMail(String htmlTemplate, Map<String, Object> ctx, String mailSubject, Locale locale, String... mailTo) throws Exception {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("A mail whose subject was {} was not sent because no java mail sender is configured {}", Arrays.toString(mailTo), mailSubject);
    }
  }
}
