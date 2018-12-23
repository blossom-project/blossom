package com.blossomproject.core.common.utils.mail;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.mail.smtp.SMTPMessage;
import freemarker.template.Configuration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeprecatedMailSenderImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private MailSenderImpl mailSender;
  private JavaMailSender javaMailSender;
  private Configuration configuration;
  private MessageSource messageSource;
  private InternetAddress from;
  private String basePath;
  private Set<String> filters;
  private Locale locale;
  private MailFilter mailFilter;
  private AsyncMailSender asyncMailSender;

  @Before
  public void setUp() throws Exception {
    this.javaMailSender = mock(JavaMailSender.class);
    when(javaMailSender.createMimeMessage()).thenReturn(new SMTPMessage((Session) null));
    doAnswer(invocationOnMock -> {
      MimeMessage mimeMessage = invocationOnMock.getArgument(0);
      for (Address address: mimeMessage.getAllRecipients()) {
        assertNotNull(address);
      }
      return null;
    }).when(javaMailSender).send(any(MimeMessage.class));

    FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
    factory.setTemplateLoaderPath("/templates");
    this.configuration = factory.createConfiguration();

    this.messageSource = mock(MessageSource.class);
    when(messageSource.getMessage(any(), any(), any(), any())).thenReturn("Subject");

    this.mailFilter = mock(MailFilterImpl.class);
    when(mailFilter.filter(any())).thenReturn(new SMTPMessage((Session) null));

    this.from = new InternetAddress("blossom-test@test.fr");
    this.basePath = "basePath";
    this.filters = Sets.newHashSet("*@blossom-project.com");
    this.locale = Locale.ENGLISH;
    this.asyncMailSender = mock(AsyncMailSender.class);
    this.mailSender = new MailSenderImpl(this.javaMailSender, this.configuration,
      this.messageSource,
      this.basePath, this.locale, this.mailFilter, asyncMailSender, from);
  }

  @Test
  public void should_succeed_instanciate() throws Exception {
    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class),
      mock(MessageSource.class), "basePath", this.locale, this.mailFilter, asyncMailSender, from);
  }

  @Test
  public void should_fail_instanciate_when_javamailsender_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(null, mock(Configuration.class),
      mock(MessageSource.class), "basePath", this.locale, this.mailFilter, asyncMailSender, from);
  }

  @Test
  public void should_fail_instanciate_when_configuration_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), null,
      mock(MessageSource.class), "basePath", this.locale, this.mailFilter, asyncMailSender, from);
  }


  @Test
  public void should_fail_instanciate_when_messagesource_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class),
      null, "basePath", this.locale, this.mailFilter, asyncMailSender, from);
  }

  @Test
  public void should_fail_instanciate_when_basepath_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class),
      mock(MessageSource.class), null, this.locale, this.mailFilter, asyncMailSender, from);
  }

  @Test
  public void should_fail_instanciate_when_locale_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class),
      mock(MessageSource.class), "basePath", null, this.mailFilter, asyncMailSender, from);
  }

  @Test
  public void should_fail_instanciate_when_mailFilter_is_null() throws Exception {
    thrown.expect(NullPointerException.class);

    new MailSenderImpl(mock(JavaMailSender.class), mock(Configuration.class),
      mock(MessageSource.class), "basePath", this.locale, null, asyncMailSender, from);
  }


  @Test
  public void should_not_send_mail_without_mailtos() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = "subject";

    this.mailSender.sendMail(htmlTemplate, parameters, subject, new InternetAddress[]{});
  }

  @Test
  public void should_not_send_mail_without_context() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    InternetAddress[] mailTo = {new InternetAddress("test@test.test")};

    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo);
  }


  @Test
  public void should_not_send_mail_without_subject() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = null;
    InternetAddress[] mailTo = {new InternetAddress("test@test.test")};

    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo);
  }


  @Test
  public void should_not_send_mail_without_context_with_highpriority() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    InternetAddress[] mailTo = {new InternetAddress("test@test.test")};

    this.mailSender.sendMail(htmlTemplate, parameters, subject, this.locale, Lists.newArrayList(), mailTo, null, null, true);
  }

  @Test
  public void should_not_send_mail_without_context_with_cc_bcc() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    InternetAddress[] mailTo = {new InternetAddress("test@test.test")};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, this.locale, mailTo, mailTo, mailTo);
  }

  @Test
  public void should_not_send_mail_without_context_with_cc_bcc_without_locale() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    InternetAddress[] mailTo = {new InternetAddress("test@test.test")};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo, mailTo, null);
  }

  @Test
  public void should_not_send_mail_without_context_with_inputstream_attachment() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    InternetAddress[] mailTo = {new InternetAddress("test@test.test")};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, this.locale, null, null, null, mailTo, mailTo, null, true);
  }


  @Test
  public void should_not_send_mail_without_mailtos_string() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = "subject";

    this.mailSender.sendMail(htmlTemplate, parameters, subject, new String[]{});
  }

  @Test
  public void should_not_send_mail_without_context_string() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    String[] mailTo = {"test@test.test"};

    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo);
  }


  @Test
  public void should_not_send_mail_without_subject_string() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = Maps.newHashMap();
    String subject = null;
    String[] mailTo = {"test@test.test"};

    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo);
  }


  @Test
  public void should_not_send_mail_without_context_with_highpriority_string() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    String[] mailTo = {"test@test.test"};

    this.mailSender.sendMail(htmlTemplate, parameters, subject, this.locale, Lists.newArrayList(), mailTo, null, null, true);
  }

  @Test
  public void should_not_send_mail_without_context_with_cc_bcc_string() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    String[] mailTo = {"test@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, this.locale, mailTo, mailTo, mailTo);
  }

  @Test
  public void should_not_send_mail_without_context_with_cc_bcc_without_locale_string() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    String[] mailTo = {"test@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo, mailTo, null);
  }

  @Test
  public void should_not_send_mail_without_context_with_inputstream_attachment_string() throws Exception {
    thrown.expect(IllegalArgumentException.class);
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = null;
    String subject = "subject";
    String[] mailTo = {"test@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, this.locale, null, null, null, mailTo, mailTo, null, true);
  }

  @Test
  public void should_send_mail_with_mailTo() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = new HashMap<>();
    String subject = "subject";
    String[] mailTo = {"test@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo, null, null);

    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_mailCC() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = new HashMap<>();
    String subject = "subject";
    String[] mailTo = {"test@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo, mailTo, null);

    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_mailBCC() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = new HashMap<>();
    String subject = "subject";
    String[] mailTo = {"test@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo, mailTo, mailTo);

    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_only_mailBCC() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = new HashMap<>();
    String subject = "subject";
    String[] mailTo = {"test@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, (String[])null, (String[])null, mailTo);

    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_multiple_mailTo() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = new HashMap<>();
    String subject = "subject";
    String[] mailTo = {"test@test.test", "test2@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo, null, null);

    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_multiple_mailCC() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = new HashMap<>();
    String subject = "subject";
    String[] mailTo = {"test@test.test", "test2@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, mailTo, mailTo, null);

    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_multiple_mailBCC() throws Exception {
    String htmlTemplate = "htmlTemplate";
    Map<String, Object> parameters = new HashMap<>();
    String subject = "subject";
    String[] mailTo = {"test@test.test", "test2@test.test"};
    this.mailSender.sendMail(htmlTemplate, parameters, subject, (String[])null, (String[])null, mailTo);

    verify(javaMailSender, times(1)).send(any(MimeMessage.class));
  }
}
