package com.blossomproject.core.common.utils.mail;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(MockitoJUnitRunner.class)
public class MailFilterImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private MailFilter mailFilter;


  @Before
  public void setUp() throws AddressException {
    this.mailFilter = new MailFilterImpl(Sets.newHashSet(".*@test.com"));

  }


  @Test
  public void filter_with_cc_bcc() throws Exception {
    final MimeMessageHelper helper = new MimeMessageHelper(new MimeMessage((Session) null));
    helper.setTo("test@test.com");
    helper.setCc("filter@filter.com");
    helper.setBcc("filter@filter.com");
    helper.setSubject("subject");
    helper.setText("htmlContent", true);
    MimeMessage result = this.mailFilter.filter(helper);
    assertEquals("test.com matches", 1, result.getRecipients(Message.RecipientType.TO).length);
    assertNull("filter.com does not match", result.getRecipients(Message.RecipientType.CC));
    assertNull("filter.com does not match", result.getRecipients(Message.RecipientType.BCC));
  }

  @Test
  public void no_filter_cc_bcc() throws Exception {
    final MimeMessageHelper helper = new MimeMessageHelper(new MimeMessage((Session) null));
    helper.setTo("test@test.com");
    helper.setCc("test@test.com");
    helper.setBcc("test@test.com");
    helper.setSubject("subject");
    helper.setText("htmlContent", true);
    MimeMessage result = this.mailFilter.filter(helper);
    assertEquals("test.com matches", 1, result.getRecipients(Message.RecipientType.TO).length);
    assertEquals("test.com matches", 1, result.getRecipients(Message.RecipientType.CC).length);
    assertEquals("test.com matches", 1, result.getRecipients(Message.RecipientType.BCC).length);
  }

  @Test
  public void filter_to() throws Exception {
    thrown.expect(MessagingException.class);
    final MimeMessageHelper helper = new MimeMessageHelper(new MimeMessage((Session) null));
    helper.setTo("filter@filter.com");
    helper.setCc("filter@filter.com");
    helper.setBcc("filter@filter.com");
    helper.setSubject("subject");
    helper.setText("htmlContent", true);
    MimeMessage result = this.mailFilter.filter(helper);
    assertNull("filter.com does not match", result.getRecipients(Message.RecipientType.TO));
    assertNull("filter.com does not match", result.getRecipients(Message.RecipientType.CC));
    assertNull("filter.com does not match", result.getRecipients(Message.RecipientType.BCC));
  }

  @Test
  public void no_filter() throws Exception {
    MailFilter mailFilterNoFilter = new MailFilterImpl(null);
    final MimeMessageHelper helper = new MimeMessageHelper(new MimeMessage((Session) null));
    helper.setTo("test@test.com");
    helper.setCc("filter@filter.com");
    helper.setBcc("filter@filter.com");
    helper.setSubject("subject");
    helper.setText("htmlContent", true);
    MimeMessage result = mailFilterNoFilter.filter(helper);
    assertEquals("To goes through", 1, result.getRecipients(Message.RecipientType.TO).length);
    assertEquals("Cc goes through", 1, result.getRecipients(Message.RecipientType.CC).length);
    assertEquals("Bcc goes through", 1, result.getRecipients(Message.RecipientType.BCC).length);
  }

  @Test
  public void no_recipients() throws Exception {
    thrown.expect(MessagingException.class);
    final MimeMessageHelper helper = new MimeMessageHelper(new MimeMessage((Session) null));
    helper.setSubject("subject");
    helper.setText("htmlContent", true);
    MimeMessage result = this.mailFilter.filter(helper);
    assertNull("filter.com does not match", result.getRecipients(Message.RecipientType.TO));
    assertNull("filter.com does not match", result.getRecipients(Message.RecipientType.CC));
    assertNull("filter.com does not match", result.getRecipients(Message.RecipientType.BCC));
  }

  @Test
  public void filter_internet_adress_with_personnal() throws Exception {
    final MimeMessageHelper helper = new MimeMessageHelper(new MimeMessage((Session) null));
    helper.setTo("test test <test@test.com>");
    helper.setCc("filter this <filter@filter.com>");
    helper.setBcc("filter that <filter@filter.com>");
    helper.setSubject("subject");
    helper.setText("htmlContent", true);
    MimeMessage result = this.mailFilter.filter(helper);
    assertEquals("test.com matches", 1, result.getRecipients(Message.RecipientType.TO).length);
    assertNull("filter.com does not match", result.getRecipients(Message.RecipientType.CC));
    assertNull("filter.com does not match", result.getRecipients(Message.RecipientType.BCC));
  }

}
