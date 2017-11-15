package fr.blossom.core.user;

import java.util.Date;

import org.assertj.core.util.Preconditions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;

import fr.blossom.core.common.dao.GenericCrudDaoImpl;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@CacheConfig(cacheResolver = "blossomCacheResolver")
public class UserDaoImpl extends GenericCrudDaoImpl<User> implements UserDao {
  private final UserRepository userRepository;

  public UserDaoImpl(UserRepository repository) {
    super(repository);
    Preconditions.checkNotNull(repository);
    this.userRepository = repository;
  }

  @Override
  protected User updateEntity(User originalEntity, User modifiedEntity) {
    originalEntity.setActivated(modifiedEntity.isActivated());
    originalEntity.setFirstname(modifiedEntity.getFirstname());
    originalEntity.setLastname(modifiedEntity.getLastname());
    originalEntity.setCivility(modifiedEntity.getCivility());
    originalEntity.setEmail(modifiedEntity.getEmail());
    originalEntity.setPhone(modifiedEntity.getPhone());
    originalEntity.setDescription(modifiedEntity.getDescription());
    originalEntity.setCompany(modifiedEntity.getCompany());
    originalEntity.setFunction(modifiedEntity.getFunction());
    return originalEntity;
  }

  @Override
  public User getByIdentifier(String identifier) {
    return this.userRepository.findOneByIdentifier(identifier).orElse(null);
  }

  @Override
  public User getByEmail(String email) {
    return this.userRepository.findOneByEmail(email).orElse(null);
  }

  @Override
  @CachePut(key = "#a0+''")
  public User updateActivation(long id, boolean activated) {
    User user = repository.findOne(id);
    user.setActivated(activated);
    return repository.save(user);
  }

  @Override
  @CachePut(key = "#a0+''")
  public User updatePassword(Long id, String encodedPassword) {
    User user = repository.findOne(id);
    user.setPasswordHash(encodedPassword);
    return repository.save(user);
  }

  @Override
  @CachePut(key = "#a0+''")
  public User updateAvatar(Long id, byte[] avatar) {
    User user = repository.findOne(id);
    user.setAvatar(avatar);
    return repository.save(user);
  }

  @Override
  @CachePut(key = "#a0+''")
  public User updateLastConnection(Long id, Date lastConnection) {
    User user = repository.findOne(id);
    if (user == null) {
      return null;
    }
    user.setLastConnection(lastConnection);
    return repository.save(user);
  }

}
