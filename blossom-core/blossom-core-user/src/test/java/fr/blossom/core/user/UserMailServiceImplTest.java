package fr.blossom.core.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import fr.blossom.core.common.utils.action_token.ActionTokenService;
import fr.blossom.core.common.utils.mail.MailSender;
import java.util.Locale;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

;

@RunWith(MockitoJUnitRunner.class)
public class UserMailServiceImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private MailSender mailSender;

  @Mock
  private ActionTokenService tokenService;

  @Spy
  @InjectMocks
  private UserMailServiceImpl userMailService;

  @Test
  public void test_send_change_password_email_user_not_null_token_not_null() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setLocale(Locale.FRANCE);
    userDTO.setEmail("test");
    userMailService.sendChangePasswordEmail(userDTO , "token !");

    verify(mailSender, times(1))
      .sendMail(anyString(),
        any(),
        anyString(),
        any(Locale.class),
        anyString());
  }

  @Test
  public void test_send_change_password_email_user_null() throws Exception {
    thrown.expect(NullPointerException.class);
    userMailService.sendChangePasswordEmail(null, "token !");
  }

  @Test
  public void test_send_change_password_email_token_null() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setLocale(Locale.FRANCE);
    userDTO.setEmail("test");

    thrown.expect(NullPointerException.class);
    userMailService.sendChangePasswordEmail(userDTO, null);
  }

  @Test
  public void test_send_account_creation_email_user_not_null_token_not_null() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setLocale(Locale.FRANCE);
    userDTO.setEmail("test");

    userMailService.sendAccountCreationEmail(userDTO, "token !");

    verify(mailSender, times(1))
      .sendMail(anyString(), any(),
        anyString(), any(Locale.class), anyString());
  }

  @Test
  public void test_send_account_creation_email_user_null() throws Exception {
    thrown.expect(NullPointerException.class);
    userMailService.sendAccountCreationEmail(null, "token !");
  }

  @Test
  public void test_send_account_creation_email_token_null() throws Exception {
    thrown.expect(NullPointerException.class);
    userMailService.sendAccountCreationEmail(new UserDTO(), null);
  }

  @Test
  public void test_user_mail_service_impl_nothing_null() throws Exception {
    new UserMailServiceImpl(mailSender);
  }

  @Test
  public void test_user_mail_service_impl_user_mail_service_null() throws Exception {
    thrown.expect(NullPointerException.class);
    new UserMailServiceImpl(null);
  }

}
