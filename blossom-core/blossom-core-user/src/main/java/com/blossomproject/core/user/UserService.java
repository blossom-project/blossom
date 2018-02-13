package com.blossomproject.core.user;

import com.blossomproject.core.common.service.CrudService;
import com.blossomproject.core.common.utils.action_token.ActionToken;

import java.io.IOException;
import java.io.InputStream;
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

  /**
   * Return the user (if any) identified by an ActionToken produced by this service.
   * Since these ActionToken are made to activate/retrieve a user password, they are
   * invalidated once the user manages to login once.
   *
   * @param actionToken Action token obtained by email sent by this service
   * @return optional UserDTO, present if the actionToken was obtained after the last user login
   */
  Optional<UserDTO> getByActionToken(ActionToken actionToken);

  UserDTO update(Long userId, UserUpdateForm userUpdateForm);

  UserDTO updateActivation(long id, boolean activated);

  UserDTO updatePassword(Long id, String password);

  UserDTO updateLastConnection(Long id, Date lastConnection);

  void askPasswordChange(long id) throws Exception;

  void updateAvatar(long id, byte[] avatar);

  InputStream loadAvatar(long id) throws IOException;

  /**
   * Generate a valid password reset token for a user
   * @param userDTO User which password should be reset with this token
   * @return A valid password reset token
   */
  String generatePasswordResetToken(UserDTO userDTO);

}
