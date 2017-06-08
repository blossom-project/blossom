package fr.mgargadennec.blossom.core.user;

import fr.mgargadennec.blossom.core.common.dao.GenericCrudDaoImpl;

import java.util.Date;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class UserDaoImpl extends GenericCrudDaoImpl<User> implements UserDao {

  public UserDaoImpl(UserRepository repository) {
    super(repository);
  }

  @Override
  protected User updateEntity(User originalEntity, User modifiedEntity) {
    originalEntity.setIdentifier(modifiedEntity.getIdentifier());
    originalEntity.setEmail(modifiedEntity.getEmail());
    originalEntity.setFirstname(modifiedEntity.getFirstname());
    originalEntity.setLastname(modifiedEntity.getLastname());
    originalEntity.setFunction(modifiedEntity.getFunction());
    originalEntity.setPhone(modifiedEntity.getPhone());
    originalEntity.setAvatar(modifiedEntity.getAvatar());

    return originalEntity;
  }

  @Override
  public User getByIdentifier(String identifier) {
    return from(QUser.user).where(QUser.user.identifier.eq(identifier)).fetchOne();
  }

  @Override
  public User getByEmail(String email) {
    return from(QUser.user).where(QUser.user.email.eq(email)).fetchOne();
  }


  @Override
  public User updateActivation(long id, boolean activated) {
    User user = repository.findOne(id);
    user.setActivated(activated);
    return repository.save(user);
  }

  @Override
  public User updatePassword(Long id, String encodedPassword) {
    User user = repository.findOne(id);
    user.setPasswordHash(encodedPassword);
    return repository.save(user);
  }

  @Override
  public User updateAvatar(Long id, byte[] avatar) {
    User user = repository.findOne(id);
    user.setAvatar(avatar);
    return repository.save(user);
  }

  @Override
  public User updateLastConnection(Long id, Date lastConnection) {
    User user = repository.findOne(id);
    if(user == null){
      return null;
    }
    user.setLastConnection(lastConnection);
    return repository.save(user);
  }

}
