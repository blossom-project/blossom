package fr.mgargadennec.blossom.core.right.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericService;
import fr.mgargadennec.blossom.core.right.process.dto.BlossomRightProcessDTO;
import fr.mgargadennec.blossom.core.right.service.dto.BlossomRightServiceDTO;

/**
 * Couche Service - Right
 */
public interface IBlossomRightService extends IBlossomGenericService<BlossomRightProcessDTO, BlossomRightServiceDTO> {

  /**
   * R�cup�re la liste des droits associ�s � un r�le
   *
   * @param pageable
   * @param roleId
   * @return la liste des droits associ�s au r�le
   */
  Page<BlossomRightServiceDTO> getByRoleId(Pageable pageable, Long roleId);

}
