package fr.blossom.core.common.utils.mail;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Locale;
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
    mailSender.sendMail("test", Maps.newHashMap(), "", "");
  }

  @Test
  public void should_not_send_mail_with_locale() throws Exception {
    mailSender.sendMail("test", Maps.newHashMap(), "", Locale.FRANCE, "");
  }
  @Test
  public void should_not_send_mail_with_locale_and_attached_files() throws Exception {
    mailSender.sendMail("test", Maps.newHashMap(), "", Locale.FRANCE, Lists.newArrayList(), "");
  }
}
