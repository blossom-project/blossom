package com.blossomproject.core.user;

import java.util.Date;

import com.blossomproject.core.common.dao.BlossomGenericCrudDaoImpl;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;

import com.google.common.base.Preconditions;

import com.blossomproject.core.common.dao.GenericCrudDaoImpl;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@CacheConfig(cacheResolver = "blossomCacheResolver")
public class UserDaoImpl extends BlossomGenericCrudDaoImpl<User> implements UserDao {
  private final UserRepository userRepository;

  public UserDaoImpl(UserRepository repository) {
    super(repository, Mappers.getMapper(UserEntityMapper.class));
    Preconditions.checkNotNull(repository);
    this.userRepository = repository;
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
    User user = repository.findById(id).orElse(null);
    user.setActivated(activated);
    return repository.save(user);
  }

  @Override
  @CachePut(key = "#a0+''")
  public User updatePassword(Long id, String encodedPassword) {
    User user = repository.findById(id).orElse(null);
    user.setPasswordHash(encodedPassword);
    return repository.save(user);
  }

  @Override
  @CachePut(key = "#a0+''")
  public User updateAvatar(Long id, byte[] avatar) {
    User user = repository.findById(id).orElse(null);
    user.setAvatar(avatar);
    return repository.save(user);
  }

  @Override
  @CachePut(key = "#a0+''")
  public User updateLastConnection(Long id, Date lastConnection) {
    User user = repository.findById(id).orElse(null);
    if (user == null) {
      return null;
    }
    user.setLastConnection(lastConnection);
    return repository.save(user);
  }

}
