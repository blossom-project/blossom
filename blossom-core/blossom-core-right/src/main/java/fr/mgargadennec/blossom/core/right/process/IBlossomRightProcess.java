package fr.mgargadennec.blossom.core.right.process;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.common.process.generic.IBlossomGenericProcess;
import fr.mgargadennec.blossom.core.right.model.BlossomRightPO;
import fr.mgargadennec.blossom.core.right.process.dto.BlossomRightProcessDTO;

public interface IBlossomRightProcess extends IBlossomGenericProcess<BlossomRightPO, BlossomRightProcessDTO> {

  Page<BlossomRightProcessDTO> getByRoleId(Pageable pageable, Long roleId);

  void deleteByRoleId(Long roleId);

}
