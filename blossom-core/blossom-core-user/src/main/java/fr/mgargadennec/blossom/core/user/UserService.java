package fr.mgargadennec.blossom.core.user;

import fr.mgargadennec.blossom.core.common.service.CrudService;

import java.util.Date;
import java.util.Optional;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface UserService extends CrudService<UserDTO> {

  Optional<UserDTO> getByIdentifier(String identifier);

  Optional<UserDTO> getByEmail(String email);

  UserDTO updateActivation(long id, boolean activated);

  UserDTO updatePassword(Long id, String password);

  UserDTO updateLastConnection(Long id, Date lastConnection);

  void askPasswordChange(long id);

}
