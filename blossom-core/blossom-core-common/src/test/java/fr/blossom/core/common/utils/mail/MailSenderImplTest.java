package fr.blossom.core.common.utils.mail;

import static org.mockito.Mockito.mock;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import freemarker.template.Configuration;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;

@RunWith(MockitoJUnitRunner.class)
public class MailSenderImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private MailSenderImpl mailSender;
  private JavaMailSender javaMailSender;
  private Configuration configuration;
  private MessageSource messageSource;
  private String from;
  private String basePath;
  private Set<String> filters;
  private Locale locale;

  @Before
  public void setUp() throws Exception {
    this.javaMailSender = mock(JavaMailSender.class);
    this.configuration = mock(Configuration.class);
    this.messageSource = mock(MessageSource.class);
    this.from = "blossom-test@test.fr";
    this.basePath = "basePath";
    this.filters = Sets.newHashSet("*@blossom-project.com");
    this.locale = Locale.ENGLISH;
    this.mailSender = new MailSenderImpl(this.javaMailSender, this.configuration, this.filters,
      this.messageSource,
      this.from, this.basePath, this.locale);
  }

  @Test
  public void should_succeed_instanciate() throws Exception {
    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), Sets.newHashSet(),
      mock(MessageSource.class), "from", "basePath", this.locale);
  }

  @Test
  public void should_fail_instanciate_when_javamailsender_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(null, mock(Configuration.class), Sets.newHashSet(),
      mock(MessageSource.class), "from", "basePath", this.locale);
  }

  @Test
  public void should_fail_instanciate_when_configuration_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), null, Sets.newHashSet(),
      mock(MessageSource.class), "from", "basePath", this.locale);
  }

  @Test
  public void should_succeed_instanciate_when_filters_is_null() throws Exception {
    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), null,
      mock(MessageSource.class), "from", "basePath", this.locale);
  }

  @Test
  public void should_fail_instanciate_when_messagesource_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), Sets.newHashSet(),
      null, "from", "basePath", this.locale);
  }

  @Test
  public void should_fail_instanciate_when_from_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), Sets.newHashSet(),
      mock(MessageSource.class), null, "basePath", this.locale);
  }

  @Test
  public void should_fail_instanciate_when_basepath_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), Sets.newHashSet(),
      mock(MessageSource.class), "from", null, this.locale);
  }

  @Test
  public void should_fail_instanciate_when_locale_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class), Sets.newHashSet(),
      mock(MessageSource.class), "from", "basePath", null);
  }


  @Test
  public void should_not_send_mail_without_mailtos() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = "subject";

    this.mailSender.sendMail(htmlTemplate, parameters, subject);
  }

  @Test
  public void should_not_send_mail_without_context() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";

    this.mailSender.sendMail(htmlTemplate, parameters, subject);
  }


  @Test
  public void should_not_send_mail_without_subject() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";

    this.mailSender.sendMail(htmlTemplate, parameters, subject);
  }
}
