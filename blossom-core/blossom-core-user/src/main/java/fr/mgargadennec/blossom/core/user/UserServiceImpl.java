package fr.mgargadennec.blossom.core.user;

import fr.mgargadennec.blossom.core.common.mapper.DTOMapper;
import fr.mgargadennec.blossom.core.common.service.GenericCrudServiceImpl;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class UserServiceImpl extends GenericCrudServiceImpl<UserDTO, User> implements UserService {
  public UserServiceImpl(UserDao dao, DTOMapper<User, UserDTO> mapper, ApplicationEventPublisher publisher) {
    super(dao, mapper, publisher);
  }
}
