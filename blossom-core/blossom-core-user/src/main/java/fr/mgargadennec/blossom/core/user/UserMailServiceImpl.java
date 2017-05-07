package fr.mgargadennec.blossom.core.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import fr.mgargadennec.blossom.core.common.utils.action_token.ActionToken;
import fr.mgargadennec.blossom.core.common.utils.action_token.ActionTokenService;
import fr.mgargadennec.blossom.core.common.utils.mail.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;

public class UserMailServiceImpl implements UserMailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserMailServiceImpl.class);

  private final MailSender mailSender;
  private final ActionTokenService tokenService;

  public UserMailServiceImpl(MailSender mailSender, ActionTokenService tokenService) {
    this.mailSender = mailSender;
    this.tokenService = tokenService;
  }

  @Override
  public void sendAccountCreationEmail(UserDTO user) throws Exception {
    Preconditions.checkNotNull(user);

    ActionToken actionToken = new ActionToken();
    actionToken.setAction("USER_ACTIVATION");
    actionToken.setUserId(user.getId());
    actionToken.setExpirationDate(LocalDateTime.now().plusDays(3));

    Map<String, Object> ctx = Maps.newHashMap();
    ctx.put("user", user);
    ctx.put("token", this.tokenService.generateToken(actionToken));

    this.mailSender.sendMail("user-activation", ctx, "[CSM] User activation", user.getEmail());
  }

  @Override
  public void sendChangePasswordEmail(UserDTO user) throws Exception {
    Preconditions.checkNotNull(user);

    ActionToken actionToken = new ActionToken();
    actionToken.setAction("USER_RESET_PASSWORD");
    actionToken.setUserId(user.getId());
    actionToken.setExpirationDate(LocalDateTime.now().plusHours(3));

    Map<String, Object> ctx = Maps.newHashMap();
    ctx.put("user", user);
    ctx.put("token", this.tokenService.generateToken(actionToken));

    this.mailSender.sendMail("user-change-password", ctx, "[CSM] Password change", user.getEmail());
  }
}
