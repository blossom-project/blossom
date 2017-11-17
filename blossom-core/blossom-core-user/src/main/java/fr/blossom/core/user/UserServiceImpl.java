package fr.blossom.core.user;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import fr.blossom.core.common.event.CreatedEvent;
import fr.blossom.core.common.event.UpdatedEvent;
import fr.blossom.core.common.mapper.DTOMapper;
import fr.blossom.core.common.service.GenericCrudServiceImpl;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class UserServiceImpl extends GenericCrudServiceImpl<UserDTO, User> implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserDao userDao;
  private final UserMailService userMailService;
  private final Resource defaultAvatar;

  public UserServiceImpl(UserDao dao, DTOMapper<User, UserDTO> mapper,
    ApplicationEventPublisher publisher,
    PasswordEncoder passwordEncoder, UserMailService userMailService, Resource defaultAvatar) {
    super(dao, mapper, publisher);
    Preconditions.checkNotNull(passwordEncoder);
    Preconditions.checkNotNull(dao);
    Preconditions.checkNotNull(userMailService);
    Preconditions.checkNotNull(defaultAvatar);
    this.passwordEncoder = passwordEncoder;
    this.userDao = dao;
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
    userMailService.sendAccountCreationEmail(savedUser);

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
    userMailService.sendChangePasswordEmail(user);
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
  public byte[] loadAvatar(long id) throws IOException {
    User user = this.userDao.getOne(id);
    if (user != null && user.getAvatar() != null) {
      return user.getAvatar();
    } else {
      return ByteStreams.toByteArray(defaultAvatar.getInputStream());
    }
  }

  protected String generateRandomPasswordHash() {
    return passwordEncoder.encode(UUID.randomUUID().toString());
  }
}
