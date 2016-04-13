package fr.mgargadennec.blossom.core.user.service;

import org.springframework.security.authentication.BadCredentialsException;

import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericService;
import fr.mgargadennec.blossom.core.user.process.dto.BlossomUserProcessDTO;
import fr.mgargadennec.blossom.core.user.service.dto.BlossomUserServiceDTO;

public interface IBlossomUserService extends IBlossomGenericService<BlossomUserProcessDTO, BlossomUserServiceDTO> {

  BlossomUserServiceDTO getByLogin(String login) throws BadCredentialsException;

}
