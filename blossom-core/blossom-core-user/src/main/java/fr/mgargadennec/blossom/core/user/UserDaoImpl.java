package fr.mgargadennec.blossom.core.user;

import fr.mgargadennec.blossom.core.common.PredicateProvider;
import fr.mgargadennec.blossom.core.common.dao.GenericCrudDaoImpl;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class UserDaoImpl extends GenericCrudDaoImpl<User> implements UserDao {
  public UserDaoImpl(UserRepository repository, PredicateProvider predicateProvider) {
    super(repository, predicateProvider);
  }

  @Override
  protected User updateEntity(User originalEntity, User modifiedEntity) {
    return null;
  }
}
