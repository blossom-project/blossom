package fr.mgargadennec.blossom.core.user;

import fr.mgargadennec.blossom.core.common.service.CrudService;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface UserService extends CrudService<UserDTO> {
  String USER_ACTIVATION = "USER_ACTIVATION";
  String USER_RESET_PASSWORD = "USER_RESET_PASSWORD";

  UserDTO create(UserCreateForm userCreateForm) throws Exception;

  Optional<UserDTO> getByIdentifier(String identifier);

  Optional<UserDTO> getByEmail(String email);

  Optional<UserDTO> getById(Long id);

  UserDTO updateActivation(long id, boolean activated);

  UserDTO updatePassword(Long id, String password);

  UserDTO updateLastConnection(Long id, Date lastConnection);

  void askPasswordChange(long id) throws Exception;

  void updateAvatar(long id, byte[] avatar);

  byte[] loadAvatar(long id) throws IOException;

}
