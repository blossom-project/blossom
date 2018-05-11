package com.blossomproject.core.common.utils.mail;

import static org.mockito.Mockito.mock;

import com.google.common.collect.Lists;
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
import org.mockito.junit.MockitoJUnitRunner;
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
  private MailFilter mailFilter;

  @Before
  public void setUp() throws Exception {
    this.javaMailSender = mock(JavaMailSender.class);
    this.configuration = mock(Configuration.class);
    this.messageSource = mock(MessageSource.class);
    this.mailFilter = mock(MailFilterImpl.class);
    this.from = "blossom-test@test.fr";
    this.basePath = "basePath";
    this.filters = Sets.newHashSet("*@blossom-project.com");
    this.locale = Locale.ENGLISH;
    this.mailSender = new MailSenderImpl(this.javaMailSender, this.configuration,
      this.messageSource,
      this.basePath, this.locale, this.mailFilter);
  }

  @Test
  public void should_succeed_instanciate() throws Exception {
    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class),
      mock(MessageSource.class), "basePath", this.locale, this.mailFilter);
  }

  @Test
  public void should_fail_instanciate_when_javamailsender_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(null, mock(Configuration.class),
      mock(MessageSource.class), "basePath", this.locale, this.mailFilter);
  }

  @Test
  public void should_fail_instanciate_when_configuration_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), null,
      mock(MessageSource.class), "basePath", this.locale, this.mailFilter);
  }


  @Test
  public void should_fail_instanciate_when_messagesource_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class),
      null,  "basePath", this.locale, this.mailFilter);
  }

  @Test
  public void should_fail_instanciate_when_basepath_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class),
      mock(MessageSource.class),  null, this.locale, this.mailFilter);
  }

  @Test
  public void should_fail_instanciate_when_locale_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class),
      mock(MessageSource.class),  "basePath", null, this.mailFilter);
  }

  @Test
  public void should_fail_instanciate_when_mailFilter_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class),
            mock(MessageSource.class), "basePath", this.locale,null );
  }


  @Test
  public void should_not_send_mail_without_mailtos() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = "subject";

    this.mailSender.sendMail(htmlTemplate, parameters, subject, null);
  }

  @Test
  public void should_not_send_mail_without_context() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    String[] mailTo = {"test@test.test"};

    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo);
  }


  @Test
  public void should_not_send_mail_without_subject() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters =Maps.newHashMap();
    String subject =null ;
    String[] mailTo = {"test@test.test"};

    this.mailSender.sendMail(htmlTemplate, parameters, subject,mailTo);
  }



  @Test
  public void should_not_send_mail_without_context_with_highpriority() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    String[] mailTo = {"test@test.test"};

    this.mailSender.sendMail(htmlTemplate, parameters, subject,this.locale, Lists.newArrayList(), mailTo, null,null,true);
  }

  @Test
  public void should_not_send_mail_without_context_with_cc_bcc() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    String[] mailTo = {"test@test.test"};
    this.mailSender.sendMail(htmlTemplate,parameters,subject,this.locale,mailTo, mailTo, mailTo);
  }

  @Test
  public void should_not_send_mail_without_context_with_cc_bcc_without_locale() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    String[] mailTo = {"test@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo, mailTo, null);
  }

  @Test
  public void should_not_send_mail_without_context_with_inputstream_attachment() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    String[] mailTo = {"test@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject,this.locale,null,null,null, mailTo, mailTo, null,true);
  }
}
