package com.blossomproject.core.user;

import com.blossomproject.core.common.utils.mail.MailSender;
import com.google.common.base.Preconditions;

public class UserMailServiceImpl implements UserMailService {

  private final MailSender mailSender;

  public UserMailServiceImpl(MailSender mailSender) {
    Preconditions.checkNotNull(mailSender);
    this.mailSender = mailSender;
  }

  @Override
  public void sendAccountCreationEmail(UserDTO user, String token) throws Exception {
    Preconditions.checkNotNull(user);
    Preconditions.checkNotNull(token);

    this.mailSender.builder()
      .htmlTemplate("user-activation")
      .addContext("user", user)
      .addContext("token", token)
      .mailSubject("activation.subject")
      .locale(user.getLocale())
      .addTo(user.getEmail(), user.getFirstname() + " " + user.getLastname())
      .build()
      .asyncSend();
  }

  @Override
  public void sendChangePasswordEmail(UserDTO user, String token) throws Exception {
    Preconditions.checkNotNull(user);
    Preconditions.checkNotNull(token);

    this.mailSender.builder()
      .htmlTemplate("user-change-password")
      .addContext("user", user)
      .addContext("token", token)
      .mailSubject("change.password.subject")
      .locale(user.getLocale())
      .addTo(user.getEmail(), user.getFirstname() + " " + user.getLastname())
      .build()
      .asyncSend();
  }
}
