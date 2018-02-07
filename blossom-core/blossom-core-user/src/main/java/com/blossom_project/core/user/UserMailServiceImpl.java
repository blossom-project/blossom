package com.blossom_project.core.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.blossom_project.core.common.utils.mail.MailSender;

import java.util.Map;

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

    this.mailSender.sendMail("user-activation", ctx, "activation.subject", user.getLocale(), user.getEmail());
  }

  @Override
  public void sendChangePasswordEmail(UserDTO user, String token) throws Exception {
    Preconditions.checkNotNull(user);
    Preconditions.checkNotNull(token);

    Map<String, Object> ctx = Maps.newHashMap();
    ctx.put("user", user);
    ctx.put("token", token);

    this.mailSender.sendMail("user-change-password", ctx, "change.password.subject", user.getLocale(), user.getEmail());
  }
}
