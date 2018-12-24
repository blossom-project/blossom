package com.blossomproject.core.common.utils.mail;

import com.sun.mail.smtp.SMTPMessage;
import freemarker.template.Configuration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MailSenderImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private Configuration configuration;
  private MessageSource messageSource;
  private InternetAddress from;
  private InternetAddress defaultTo;
  private String basePath;
  private Locale locale;
  private MailFilter mailFilter;
  private AsyncMailSender asyncMailSender;

  private String randomString = RandomStringUtils.random(10);

  @Before
  public void setUp() throws Exception {
    FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
    factory.setTemplateLoaderPath("/templates");
    this.configuration = factory.createConfiguration();

    this.messageSource = mock(MessageSource.class);
    when(messageSource.getMessage(any(), any(), any(), any())).thenReturn("Subject");

    this.mailFilter = mock(MailFilterImpl.class);
    when(mailFilter.filter(any())).thenAnswer(invocation -> (((MimeMessageHelper) invocation.getArgument(0)).getMimeMessage()));

    this.from = new InternetAddress("blossom-test@blossom-project.com");
    this.defaultTo = new InternetAddress("test@blossom-project.com");
    this.basePath = "basePath";
    this.locale = Locale.ENGLISH;
    this.asyncMailSender = spy(new AsyncMailSenderImpl());
  }

  private interface Consumer<T> {
    void accept(T t) throws Exception;
  }

  private JavaMailSender mockJavaMailSender() {
    JavaMailSender javaMailSender = mock(JavaMailSender.class);
    when(javaMailSender.createMimeMessage()).thenReturn(new SMTPMessage((Session) null));
    return javaMailSender;
  }

  private JavaMailSender mockJavaMailSender(Consumer<SMTPMessage> send) {
    JavaMailSender javaMailSender = mock(JavaMailSender.class);
    when(javaMailSender.createMimeMessage()).thenReturn(new SMTPMessage((Session) null));
    doAnswer(invocationOnMock -> {
      SMTPMessage mimeMessage = invocationOnMock.getArgument(0);
      send.accept(mimeMessage);
      return null;
    }).when(javaMailSender).send(any(MimeMessage.class));
    return javaMailSender;
  }

  private MailSender testedMailSender(JavaMailSender javaMailSender) {
    return new MailSenderImpl(
      javaMailSender,
      this.configuration,
      this.messageSource,
      this.basePath,
      this.locale,
      this.mailFilter,
      asyncMailSender,
      from,
      Collections.emptySet());
  }

  /*
   * Tests
   */

  @Test
  public void should_send_mail_with_minimum_requirements() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender();
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test Subject").addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
    verify(messageSource, atLeastOnce()).getMessage(eq("Test Subject"), any(), any(), any());
  }

  @Test
  public void should_not_send_mail_with_no_subject() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    JavaMailSender mockJavaMailSender = mockJavaMailSender();
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().addTo(defaultTo).build().send();
    verify(mockJavaMailSender, never()).send(any(MimeMessage.class));
  }

  @Test
  public void should_not_send_mail_with_no_recipients() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    JavaMailSender mockJavaMailSender = mockJavaMailSender();
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").build().send();
    verify(mockJavaMailSender, never()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_text_template() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertNotNull(mimeMessage.getContent());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ((MimeMultipart) mimeMessage.getContent()).writeTo(os);
      assertTrue(os.toString().contains("Text template"));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").textTemplate("textTemplate").addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_text_body() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertNotNull(mimeMessage.getContent());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ((MimeMultipart) mimeMessage.getContent()).writeTo(os);
      assertTrue(os.toString().contains(randomString));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").textBody(randomString).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_html_template() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertNotNull(mimeMessage.getContent());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ((MimeMultipart) mimeMessage.getContent()).writeTo(os);
      assertTrue(os.toString().contains("Test template"));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").htmlTemplate("htmlTemplate").addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_html_body() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertNotNull(mimeMessage.getContent());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ((MimeMultipart) mimeMessage.getContent()).writeTo(os);
      assertTrue(os.toString().contains(randomString));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").htmlBody(randomString).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_text_context() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertNotNull(mimeMessage.getContent());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ((MimeMultipart) mimeMessage.getContent()).writeTo(os);
      assertTrue(os.toString().contains("variable " + randomString));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").textTemplate("textTemplateWithContext").addContext("variable", randomString).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_html_context() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertNotNull(mimeMessage.getContent());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ((MimeMultipart) mimeMessage.getContent()).writeTo(os);
      assertTrue(os.toString().contains("variable " + randomString));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    Map<String, Object> ctx = new HashMap<>();
    ctx.put("variable", randomString);
    mailSender.builder().mailSubject("Test").htmlTemplate("textTemplateWithContext").addContext(ctx).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_text_translation() throws Exception {
    when(messageSource.getMessage(eq("test"), any(), any())).thenReturn(randomString);
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertNotNull(mimeMessage.getContent());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ((MimeMultipart) mimeMessage.getContent()).writeTo(os);
      assertTrue(os.toString().contains("translated " + randomString));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").textTemplate("textTemplateWithTranslation").addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_html_translation_and_french_locale() throws Exception {
    when(messageSource.getMessage(eq("test"), any(), eq(Locale.FRENCH))).thenReturn(randomString);
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertNotNull(mimeMessage.getContent());
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ((MimeMultipart) mimeMessage.getContent()).writeTo(os);
      assertTrue(os.toString().contains("translated " + randomString));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").htmlTemplate("textTemplateWithTranslation").locale(Locale.FRENCH).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_high_priority() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      //https://www.chilkatsoft.com/p/p_471.asp
      assertEquals("1", mimeMessage.getHeader("X-Priority")[0]);
      assertEquals("High", mimeMessage.getHeader("X-MSMail-Priority")[0]);
      assertEquals("High", mimeMessage.getHeader("Importance")[0]);
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").highPriority(true).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_internetAddress_sender() throws Exception {
    InternetAddress from = new InternetAddress("sender@blossom-project.com", "Test sender");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(mimeMessage.getFrom()[0], from));
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").from(from).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_string_sender() throws Exception {
    String from = "sender@blossom-project.com";
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(((InternetAddress) mimeMessage.getFrom()[0]).getAddress(), from));
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").from(from).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_string_sender_with_personnal() throws Exception {
    String from = "sender@blossom-project.com";
    String name = "Sender";
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertEquals(((InternetAddress) mimeMessage.getFrom()[0]).getAddress(), from);
      assertEquals(((InternetAddress) mimeMessage.getFrom()[0]).getPersonal(), name);
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").from(from, name).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_internetAddress_replyTo() throws Exception {
    InternetAddress replyTo = new InternetAddress("sender@blossom-project.com", "Test sender");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(mimeMessage.getReplyTo()[0], replyTo));
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").replyTo(replyTo).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_string_replyTo() throws Exception {
    String replyTo = "sender@blossom-project.com";
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(((InternetAddress) mimeMessage.getReplyTo()[0]).getAddress(), replyTo));
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").replyTo(replyTo).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_string_replyTo_with_personnal() throws Exception {
    String replyTo = "sender@blossom-project.com";
    String name = "Sender";
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertEquals(((InternetAddress) mimeMessage.getReplyTo()[0]).getAddress(), replyTo);
      assertEquals(((InternetAddress) mimeMessage.getReplyTo()[0]).getPersonal(), name);
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").replyTo(replyTo, name).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_file_attachment() throws Exception {
    URL resource = MailSenderImplTest.class.getResource("/templates/mail/textTemplate.ftl");
    File file = Paths.get(resource.toURI()).toFile();

    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(2, ((MimeMultipart) mimeMessage.getContent()).getCount()));

    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addAttachment(file).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_input_stream_attachment() throws Exception {
    URL resource = MailSenderImplTest.class.getResource("/templates/mail/textTemplate.ftl");
    File file = Paths.get(resource.toURI()).toFile();
    InputStreamSource is = new FileSystemResource(file);

    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(2, ((MimeMultipart) mimeMessage.getContent()).getCount()));

    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addAttachment(file.getName(), is, "text/plain").addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_blossom_attachment() throws Exception {
    BlossomMailAttachment attachment = mock(BlossomMailAttachment.class);

    JavaMailSender mockJavaMailSender = mockJavaMailSender();

    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addAttachment(attachment).addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
    verify(attachment, times(1)).appendTo(any());
  }

  @Test
  public void should_send_mail_with_multiple_attachments() throws Exception {
    List<BlossomMailAttachment> attachments = Arrays.asList(mock(BlossomMailAttachment.class), mock(BlossomMailAttachment.class));

    JavaMailSender mockJavaMailSender = mockJavaMailSender();

    MailSender mailSender = testedMailSender(mockJavaMailSender);
    BlossomMailBuilder builder = mailSender.builder().mailSubject("Test").addTo(defaultTo);
    for (BlossomMailAttachment attachment : attachments) {
      builder = builder.addAttachment(attachment);
    }
    builder.build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
    for (BlossomMailAttachment attachment : attachments) {
      verify(attachment, times(1)).appendTo(any());
    }
  }

  @Test
  public void should_send_mail_with_internetAddress_to() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(mimeMessage.getRecipients(Message.RecipientType.TO)[0], defaultTo));
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_string_to() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(((InternetAddress) mimeMessage.getRecipients(Message.RecipientType.TO)[0]).getAddress(), defaultTo.getAddress()));
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo.getAddress()).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_string_to_with_personnal() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertEquals(((InternetAddress) mimeMessage.getRecipients(Message.RecipientType.TO)[0]).getAddress(), defaultTo.getAddress());
      assertEquals(((InternetAddress) mimeMessage.getRecipients(Message.RecipientType.TO)[0]).getPersonal(), defaultTo.getPersonal());
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo.getAddress(), defaultTo.getPersonal()).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_multiple_to() throws Exception {
    InternetAddress additionnalTo = new InternetAddress("another@blossom-project.com", "Another");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.TO)).contains(defaultTo));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.TO)).contains(additionnalTo));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).addTo(additionnalTo).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_internetAddress_cc() throws Exception {
    InternetAddress cc = new InternetAddress("cc@blossom-project.com", "CC");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(mimeMessage.getRecipients(Message.RecipientType.CC)[0], cc));
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).addCc(cc).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_string_cc() throws Exception {
    InternetAddress cc = new InternetAddress("cc@blossom-project.com", "CC");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(((InternetAddress) mimeMessage.getRecipients(Message.RecipientType.CC)[0]).getAddress(), cc.getAddress()));
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).addCc(cc.getAddress()).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_string_cc_with_personnal() throws Exception {
    InternetAddress cc = new InternetAddress("cc@blossom-project.com", "CC");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertEquals(((InternetAddress) mimeMessage.getRecipients(Message.RecipientType.CC)[0]).getAddress(), cc.getAddress());
      assertEquals(((InternetAddress) mimeMessage.getRecipients(Message.RecipientType.CC)[0]).getPersonal(), cc.getPersonal());
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).addCc(cc.getAddress(), cc.getPersonal()).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_multiple_cc() throws Exception {
    InternetAddress cc = new InternetAddress("cc@blossom-project.com", "CC");
    InternetAddress additionnalCc = new InternetAddress("another@blossom-project.com", "Another");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.CC)).contains(cc));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.CC)).contains(additionnalCc));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).addCc(cc).addCc(additionnalCc).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_internetAddress_bcc() throws Exception {
    InternetAddress bcc = new InternetAddress("bcc@blossom-project.com", "BCC");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(mimeMessage.getRecipients(Message.RecipientType.BCC)[0], bcc));
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).addBcc(bcc).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_string_bcc() throws Exception {
    InternetAddress bcc = new InternetAddress("bcc@blossom-project.com", "BCC");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> assertEquals(((InternetAddress) mimeMessage.getRecipients(Message.RecipientType.BCC)[0]).getAddress(), bcc.getAddress()));
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).addBcc(bcc.getAddress()).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_string_bcc_with_personnal() throws Exception {
    InternetAddress bcc = new InternetAddress("bcc@blossom-project.com", "BCC");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertEquals(((InternetAddress) mimeMessage.getRecipients(Message.RecipientType.BCC)[0]).getAddress(), bcc.getAddress());
      assertEquals(((InternetAddress) mimeMessage.getRecipients(Message.RecipientType.BCC)[0]).getPersonal(), bcc.getPersonal());
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).addBcc(bcc.getAddress(), bcc.getPersonal()).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_multiple_bcc() throws Exception {
    InternetAddress bcc = new InternetAddress("bcc@blossom-project.com", "BCC");
    InternetAddress additionnalBcc = new InternetAddress("another@blossom-project.com", "Another");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.BCC)).contains(bcc));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.BCC)).contains(additionnalBcc));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).addBcc(bcc).addBcc(additionnalBcc).build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_filter_string() throws Exception {
    String filter = "kept.*";
    InternetAddress keptAddress = new InternetAddress("kept@blossom-project.com");
    InternetAddress filteredAddress = new InternetAddress("filtered@blossom-project.com");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.TO)).contains(keptAddress));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.CC)).contains(keptAddress));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.BCC)).contains(keptAddress));
      assertFalse(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.TO)).contains(filteredAddress));
      assertFalse(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.CC)).contains(filteredAddress));
      assertFalse(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.BCC)).contains(filteredAddress));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test")
      .addFilter(filter)
      .addTo(keptAddress).addCc(keptAddress).addBcc(keptAddress)
      .addTo(filteredAddress).addCc(filteredAddress).addBcc(filteredAddress)
      .build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_filter_pattern() throws Exception {
    Pattern filter = Pattern.compile("kept.*");
    InternetAddress keptAddress = new InternetAddress("kept@blossom-project.com");
    InternetAddress filteredAddress = new InternetAddress("filtered@blossom-project.com");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.TO)).contains(keptAddress));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.CC)).contains(keptAddress));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.BCC)).contains(keptAddress));
      assertFalse(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.TO)).contains(filteredAddress));
      assertFalse(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.CC)).contains(filteredAddress));
      assertFalse(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.BCC)).contains(filteredAddress));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test")
      .addFilter(filter)
      .addTo(keptAddress).addCc(keptAddress).addBcc(keptAddress)
      .addTo(filteredAddress).addCc(filteredAddress).addBcc(filteredAddress)
      .build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test
  public void should_send_mail_with_multiple_filters() throws Exception {
    String filter = "kept.*";
    String filter2 = ".*@blossom-project.com";
    InternetAddress keptAddress = new InternetAddress("kept@test.blossom-project.com");
    InternetAddress filteredAddress = new InternetAddress("not-filtered@blossom-project.com");
    JavaMailSender mockJavaMailSender = mockJavaMailSender(mimeMessage -> {
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.TO)).contains(keptAddress));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.CC)).contains(keptAddress));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.BCC)).contains(keptAddress));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.TO)).contains(filteredAddress));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.CC)).contains(filteredAddress));
      assertTrue(Arrays.asList(mimeMessage.getRecipients(Message.RecipientType.BCC)).contains(filteredAddress));
    });
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test")
      .addFilter(filter).addFilter(filter2)
      .addTo(keptAddress).addCc(keptAddress).addBcc(keptAddress)
      .addTo(filteredAddress).addCc(filteredAddress).addBcc(filteredAddress)
      .build().send();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
  }

  @Test(expected = Exception.class)
  public void should_not_send_mail_with_recipients_filtered() throws Exception {
    String filter = ".*@test.blossom-project.com";
    InternetAddress filteredAddress = new InternetAddress("filtered@blossom-project.com");
    JavaMailSender mockJavaMailSender = mockJavaMailSender();
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test")
      .addFilter(filter)
      .addTo(filteredAddress).addCc(filteredAddress).addBcc(filteredAddress)
      .build().send();
    verify(mockJavaMailSender, never()).send(any(MimeMessage.class));
  }

  @Test()
  public void should_async_send_use_asyncMailSender() throws Exception {
    JavaMailSender mockJavaMailSender = mockJavaMailSender();
    MailSender mailSender = testedMailSender(mockJavaMailSender);
    mailSender.builder().mailSubject("Test").addTo(defaultTo).build().asyncSend();
    verify(mockJavaMailSender, atLeastOnce()).send(any(MimeMessage.class));
    verify(asyncMailSender, times(1)).asyncSend(any());
  }

}
