package fr.mgargadennec.blossom.core.user;

import com.google.common.io.ByteStreams;
import fr.mgargadennec.blossom.core.common.mapper.DTOMapper;
import fr.mgargadennec.blossom.core.common.service.GenericCrudServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class UserServiceImpl extends GenericCrudServiceImpl<UserDTO, User> implements UserService {
  private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private final PasswordEncoder passwordEncoder;
  private final UserDao userDao;
  private final UserMailService userMailService;
  private final Resource defaultAvatar;

  public UserServiceImpl(UserDao dao, DTOMapper<User, UserDTO> mapper, ApplicationEventPublisher publisher, PasswordEncoder passwordEncoder, UserMailService userMailService, Resource defaultAvatar) {
    super(dao, mapper, publisher);
    this.passwordEncoder = passwordEncoder;
    this.userDao = dao;
    this.userMailService = userMailService;
    this.defaultAvatar = defaultAvatar;
  }

  @Override
  @Transactional
  public UserDTO create(UserCreateForm userCreateForm) throws Exception{
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
  @Transactional
  public UserDTO updateActivation(long id, boolean activated) {
    return mapper.mapEntity(this.userDao.updateActivation(id, activated));
  }

  @Override
  @Transactional
  public UserDTO updatePassword(Long id, String password) {
    return mapper.mapEntity(this.userDao.updatePassword(id, passwordEncoder.encode(password)));
  }

  @Override
  @Transactional
  public UserDTO updateLastConnection(Long id, Date lastConnection) {
    return mapper.mapEntity(this.userDao.updateLastConnection(id, lastConnection));
  }

  @Override
  @Transactional
  public void askPasswordChange(long userId) throws Exception {
    UserDTO updatedUser = this.updatePassword(userId, passwordEncoder.encode(UUID.randomUUID().toString()));
    userMailService.sendChangePasswordEmail(updatedUser);
  }

  @Override
  @Transactional
  public void updateAvatar(long id, byte[] avatar) {
    this.userDao.updateAvatar(id, avatar);
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
