package fr.blossom.core.user;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import fr.blossom.core.common.utils.action_token.ActionToken;
import fr.blossom.core.common.utils.action_token.ActionTokenService;
import fr.blossom.core.common.utils.mail.MailSender;

public class UserMailServiceImpl implements UserMailService {

  private final MailSender mailSender;
  private final ActionTokenService tokenService;

  public UserMailServiceImpl(MailSender mailSender, ActionTokenService tokenService) {
    Preconditions.checkNotNull(mailSender);
    Preconditions.checkNotNull(tokenService);
    this.mailSender = mailSender;
    this.tokenService = tokenService;
  }

  @Override
  public void sendAccountCreationEmail(UserDTO user) throws Exception {
    Preconditions.checkNotNull(user);

    ActionToken actionToken = new ActionToken();
    actionToken.setAction(UserService.USER_ACTIVATION);
    actionToken.setUserId(user.getId());
    actionToken.setExpirationDate(Instant.now().plus(3, ChronoUnit.DAYS));

    Map<String, Object> ctx = Maps.newHashMap();
    ctx.put("user", user);
    ctx.put("token", this.tokenService.generateToken(actionToken));

    this.mailSender.sendMail("user-activation", ctx, "activation.subject", user.getLocale(), user.getEmail());
  }

  @Override
  public void sendChangePasswordEmail(UserDTO user) throws Exception {
    Preconditions.checkNotNull(user);

    ActionToken actionToken = new ActionToken();
    actionToken.setAction(UserService.USER_RESET_PASSWORD);
    actionToken.setUserId(user.getId());
    actionToken.setExpirationDate(Instant.now().plus(3, ChronoUnit.HOURS));

    Map<String, Object> ctx = Maps.newHashMap();
    ctx.put("user", user);
    ctx.put("token", this.tokenService.generateToken(actionToken));

    this.mailSender.sendMail("user-change-password", ctx, "change.password.subject", user.getLocale(), user.getEmail());
  }
}
