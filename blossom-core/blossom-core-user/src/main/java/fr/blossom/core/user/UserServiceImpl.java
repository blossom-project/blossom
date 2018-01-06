package fr.blossom.core.user;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.event.CreatedEvent;
import fr.blossom.core.common.event.UpdatedEvent;
import fr.blossom.core.common.mapper.DTOMapper;
import fr.blossom.core.common.service.AssociationServicePlugin;
import fr.blossom.core.common.service.GenericCrudServiceImpl;
import fr.blossom.core.common.utils.action_token.ActionToken;
import fr.blossom.core.common.utils.action_token.ActionTokenService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class UserServiceImpl extends GenericCrudServiceImpl<UserDTO, User> implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserDao userDao;
  private final ActionTokenService tokenService;
  private final UserMailService userMailService;
  private final Resource defaultAvatar;

  private final String creationDateParameter = "creationDate";

  public UserServiceImpl(
    UserDao dao, DTOMapper<User, UserDTO> mapper,
    ApplicationEventPublisher publisher,
    PluginRegistry<AssociationServicePlugin, Class<? extends AbstractDTO>> associationRegistry,
    PasswordEncoder passwordEncoder, ActionTokenService tokenService, UserMailService userMailService,
    Resource defaultAvatar) {
    super(dao, mapper, publisher, associationRegistry);
    Preconditions.checkNotNull(passwordEncoder);
    Preconditions.checkNotNull(dao);
    Preconditions.checkNotNull(tokenService);
    Preconditions.checkNotNull(userMailService);
    Preconditions.checkNotNull(defaultAvatar);
    this.passwordEncoder = passwordEncoder;
    this.userDao = dao;
    this.tokenService = tokenService;
    this.userMailService = userMailService;
    this.defaultAvatar = defaultAvatar;
  }

  @Override
  @Transactional
  public UserDTO create(UserCreateForm userCreateForm) throws Exception {
    User user = new User();
    user.setFirstname(userCreateForm.getFirstname());
    user.setLastname(userCreateForm.getLastname());
    user.setCivility(userCreateForm.getCivility());
    user.setIdentifier(userCreateForm.getIdentifier());
    user.setEmail(userCreateForm.getEmail());
    user.setLocale(userCreateForm.getLocale());

    String passwordHash = generateRandomPasswordHash();
    user.setPasswordHash(passwordHash);
    user.setActivated(false);

    UserDTO savedUser = this.mapper.mapEntity(this.userDao.create(user));
    userMailService.sendAccountCreationEmail(savedUser, generateActivationToken(savedUser));

    this.publisher.publishEvent(new CreatedEvent<>(this, savedUser));

    return savedUser;
  }

  @Override
  public Optional<UserDTO> getByEmail(String email) {
    return Optional.ofNullable(mapper.mapEntity(this.userDao.getByEmail(email)));
  }

  @Override
  public Optional<UserDTO> getByIdentifier(String identifier) {
    return Optional.ofNullable(mapper.mapEntity(this.userDao.getByIdentifier(identifier)));
  }

  @Override
  public Optional<UserDTO> getById(Long id) {
    return Optional.ofNullable(mapper.mapEntity(this.userDao.getOne(id)));
  }

  @Override
  public Optional<UserDTO> getByActionToken(ActionToken actionToken) {
    Preconditions.checkState(actionToken.isValid(), "Invalid token");
    Preconditions.checkNotNull(actionToken.getAdditionalParameters().get(creationDateParameter));

    Optional<UserDTO> optionalUserDTO = getById(actionToken.getUserId());
    if (!optionalUserDTO.isPresent()) {
      return Optional.empty();
    }
    UserDTO userDTO = optionalUserDTO.get();
    Date creationDate = new Date(Long.parseLong(actionToken.getAdditionalParameters().get(creationDateParameter)));

    if (userDTO.getLastConnection() != null && creationDate.before(userDTO.getLastConnection())) {
      return Optional.empty();
    }

    return optionalUserDTO;
  }

  @Override
  public UserDTO update(Long userId, UserUpdateForm userUpdateForm) {
    UserDTO toUpdate = this.getOne(userId);
    toUpdate.setActivated(userUpdateForm.isActivated());
    toUpdate.setFirstname(userUpdateForm.getFirstname());
    toUpdate.setLastname(userUpdateForm.getLastname());
    toUpdate.setDescription(userUpdateForm.getDescription());
    toUpdate.setCivility(userUpdateForm.getCivility());
    toUpdate.setCompany(userUpdateForm.getCompany());
    toUpdate.setFunction(userUpdateForm.getFunction());
    toUpdate.setEmail(userUpdateForm.getEmail());
    toUpdate.setPhone(userUpdateForm.getPhone());

    return this.update(userId, toUpdate);
  }

  @Override
  @Transactional
  public UserDTO updateActivation(long id, boolean activated) {
    UserDTO user = mapper.mapEntity(this.userDao.updateActivation(id, activated));
    this.publisher.publishEvent(new UpdatedEvent<>(this, user));
    return user;
  }

  @Override
  @Transactional
  public UserDTO updatePassword(Long id, String password) {
    UserDTO user = mapper
      .mapEntity(this.userDao.updatePassword(id, passwordEncoder.encode(password)));
    this.publisher.publishEvent(new UpdatedEvent<>(this, user));
    return user;
  }

  @Override
  @Transactional
  public UserDTO updateLastConnection(Long id, Date lastConnection) {
    UserDTO user = mapper.mapEntity(this.userDao.updateLastConnection(id, lastConnection));
    this.publisher.publishEvent(new UpdatedEvent<>(this, user));
    return user;
  }

  @Override
  @Transactional
  public void askPasswordChange(long userId) throws Exception {
    UserDTO user = this
      .updatePassword(userId, passwordEncoder.encode(UUID.randomUUID().toString()));
    userMailService.sendChangePasswordEmail(user, generatePasswordResetToken(user));
  }

  @Override
  @Transactional
  public void updateAvatar(long id, byte[] avatar) {
    UserDTO user = this.getOne(id);
    if (user != null) {
      this.userDao.updateAvatar(id, avatar);
    }
    this.publisher.publishEvent(new UpdatedEvent<>(this, user));
  }

  @Override
  public InputStream loadAvatar(long id) throws IOException {
    User user = this.userDao.getOne(id);
    if (user != null && user.getAvatar() != null) {
      return new ByteArrayInputStream(user.getAvatar());
    } else {
      return defaultAvatar.getInputStream();
    }
  }

  protected String generateRandomPasswordHash() {
    return passwordEncoder.encode(UUID.randomUUID().toString());
  }

  @VisibleForTesting
  protected String generateActivationToken(UserDTO user) {
    ActionToken actionToken = new ActionToken();
    actionToken.setAction(UserService.USER_ACTIVATION);
    actionToken.setUserId(user.getId());
    actionToken.setExpirationDate(Instant.now().plus(3, ChronoUnit.DAYS));

    Map<String, String> additionalParameters = new HashMap<>();
    additionalParameters.put(creationDateParameter, Long.toString(Instant.now().toEpochMilli()));
    actionToken.setAdditionalParameters(additionalParameters);

    return tokenService.generateToken(actionToken);
  }

  public String generatePasswordResetToken(UserDTO user) {
    ActionToken actionToken = new ActionToken();
    actionToken.setAction(UserService.USER_RESET_PASSWORD);
    actionToken.setUserId(user.getId());
    actionToken.setExpirationDate(Instant.now().plus(3, ChronoUnit.HOURS));

    Map<String, String> additionalParameters = new HashMap<>();
    additionalParameters.put(creationDateParameter, Long.toString(Instant.now().toEpochMilli()));
    actionToken.setAdditionalParameters(additionalParameters);

    return tokenService.generateToken(actionToken);
  }
}
