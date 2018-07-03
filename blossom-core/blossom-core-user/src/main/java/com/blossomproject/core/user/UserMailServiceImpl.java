package com.blossomproject.core.user;

import com.blossomproject.core.common.utils.mail.MailSender;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.mail.internet.InternetAddress;

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

    Map<String, Object> ctx = Maps.newHashMap();
    ctx.put("user", user);
    ctx.put("token", token);

    this.mailSender.sendMail("user-activation", ctx, "activation.subject", user.getLocale(), new InternetAddress(user.getEmail(), user.getFirstname()+" "+user.getLastname()));
  }

  @Override
  public void sendChangePasswordEmail(UserDTO user, String token) throws Exception {
    Preconditions.checkNotNull(user);
    Preconditions.checkNotNull(token);

    Map<String, Object> ctx = Maps.newHashMap();
    ctx.put("user", user);
    ctx.put("token", token);

    this.mailSender.sendMail("user-change-password", ctx, "change.password.subject", user.getLocale(), new InternetAddress(user.getEmail(), user.getFirstname()+" "+user.getLastname()));
  }
}
