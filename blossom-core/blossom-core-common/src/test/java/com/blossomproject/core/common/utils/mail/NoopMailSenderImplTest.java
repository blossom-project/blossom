package com.blossomproject.core.common.utils.mail;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Locale;
import javax.mail.internet.InternetAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;

@RunWith(MockitoJUnitRunner.class)
public class NoopMailSenderImplTest {

  private NoopMailSenderImpl mailSender;

  @Before
  public void setUp() throws Exception {
    this.mailSender = new NoopMailSenderImpl();
  }

  @Test
  public void should_not_send_mail() throws Exception {
    mailSender.sendMail("test", Maps.newHashMap(), "", new InternetAddress("test@test.test"));
  }

  @Test
  public void should_not_send_mail_with_locale() throws Exception {
    mailSender.sendMail("test", Maps.newHashMap(), "", Locale.FRANCE, new InternetAddress("test@test.test"));
  }
  @Test
  public void should_not_send_mail_with_locale_and_attached_files() throws Exception {
    mailSender.sendMail("test", Maps.newHashMap(), "", Locale.FRANCE, Lists.newArrayList(), new InternetAddress("test@test.test"));
  }

  @Test
  public void should_not_send_mail_with_cc_bcc() throws Exception {
    InternetAddress[] tabMail = {new InternetAddress("test@test.test")};
    mailSender.sendMail("test", Maps.newHashMap(), "", tabMail,tabMail, tabMail);
  }

  @Test
  public void should_not_send_mail_with_locale_cc_bcc() throws Exception {
    InternetAddress[] tabMail = {new InternetAddress("test@test.test")};
    mailSender.sendMail("test", Maps.newHashMap(), "", Locale.FRANCE, tabMail,null,null);
  }

  @Test
  public void should_not_send_mail_with_locale_and_attached_files_cc_bcc() throws Exception {
    InternetAddress[] tabMail = {new InternetAddress("test@test.test")};
    mailSender.sendMail("test", Maps.newHashMap(), "", Locale.FRANCE, Lists.newArrayList(), tabMail,null,null);
  }

  @Test
  public void should_not_send_mail_with_locale_and_attached_files_cc_bcc_highpriority() throws Exception {
    InternetAddress[] tabMail = {new InternetAddress("test@test.test")};
    mailSender.sendMail("test", Maps.newHashMap(), "", Locale.FRANCE, Lists.newArrayList(), tabMail,null,null,true);
  }

  @Test
  public void should_not_send_mail_inputstream_attachment() throws Exception{
    InternetAddress[] tabMail = {new InternetAddress("test@test.test")};
    mailSender.sendMail("test", Maps.newHashMap(), "", Locale.FRANCE, null,null,null, tabMail,null,null,true);

  }
}
