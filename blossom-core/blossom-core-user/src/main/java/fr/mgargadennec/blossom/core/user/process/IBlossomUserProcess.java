package fr.mgargadennec.blossom.core.user.process;

import fr.mgargadennec.blossom.core.common.process.generic.IBlossomGenericProcess;
import fr.mgargadennec.blossom.core.user.model.BlossomUserPO;
import fr.mgargadennec.blossom.core.user.process.dto.BlossomUserProcessDTO;

public interface IBlossomUserProcess extends IBlossomGenericProcess<BlossomUserPO, BlossomUserProcessDTO> {

  BlossomUserProcessDTO getByEmail(String email);

  BlossomUserProcessDTO getByLogin(String login);

  BlossomUserProcessDTO update(BlossomUserProcessDTO processDTO);

}
