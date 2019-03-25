package com.blossomproject.core.user;

import com.blossomproject.core.common.utils.action_token.ActionTokenService;
import com.blossomproject.core.common.utils.mail.BlossomMail;
import com.blossomproject.core.common.utils.mail.BlossomMailBuilderImpl;
import com.blossomproject.core.common.utils.mail.MailSender;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;

import static org.mockito.Mockito.*;

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

  private BlossomMail setupBlossomMail() {
    BlossomMail mail = mock(BlossomMail.class);
    when(mailSender.builder()).thenReturn(new BlossomMailBuilderImpl() {
      @Override
      public BlossomMail build() {
        return mail;
      }
    });
    return mail;
  }

  @Test
  public void test_send_change_password_email_user_not_null_token_not_null() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setLocale(Locale.FRANCE);
    userDTO.setEmail("test@test.test");

    BlossomMail mail = setupBlossomMail();

    userMailService.sendChangePasswordEmail(userDTO, "token !");

    verify(mailSender, times(1)).builder();
    verify(mail, times(1)).asyncSend();
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
    userDTO.setEmail("test@test.test");

    thrown.expect(NullPointerException.class);
    userMailService.sendChangePasswordEmail(userDTO, null);
  }

  @Test
  public void test_send_account_creation_email_user_not_null_token_not_null() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setLocale(Locale.FRANCE);
    userDTO.setEmail("test@test.test");

    BlossomMail mail = setupBlossomMail();

    userMailService.sendAccountCreationEmail(userDTO, "token !");

    verify(mailSender, times(1)).builder();
    verify(mail, times(1)).asyncSend();
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
