package fr.mgargadennec.blossom.core.association.group.user.process;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.association.group.user.model.BlossomAssociationGroupUserPO;
import fr.mgargadennec.blossom.core.association.group.user.process.dto.BlossomAssociationGroupUserProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.IBlossomGenericProcess;

/**
 * Couche Process - Group
 */
public interface IBlossomAssociationGroupUserProcess extends
    IBlossomGenericProcess<BlossomAssociationGroupUserPO, BlossomAssociationGroupUserProcessDTO> {

  /**
   * R�cup�re une liste pagin�e de toutes les AssociationGroupUser pour un groupe
   * 
   * @param pageable
   * @param groupId id du groupe
   * @return liste pagin�e de toutes les AssociationGroupUser pour un groupe
   */
  Page<BlossomAssociationGroupUserProcessDTO> getAllByGroupId(Pageable pageable, Long groupId);

  /**
   * R�cup�re une liste pagin�e de toutes les AssociationGroupUser pour un utilisateur
   * 
   * @param pageable
   * @param userId id du groupe
   * @return liste pagin�e de toutes les AssociationGroupUser pour un utilisateur
   */
  Page<BlossomAssociationGroupUserProcessDTO> getAllByUserId(Pageable pageable, Long userId);

  /**
   * R�cup�re une liste pagin�e de toutes les AssociationGroupUser pour un groupe et un utilisateur
   * 
   * @param pageable
   * @param groupId id du groupe
   * @param userId id de l'utilisateur
   * @return liste pagin�e de toutes les AssociationGroupUser pour un groupe et un utilisateur
   */
  Page<BlossomAssociationGroupUserProcessDTO> getAllByGroupIdAndUserId(Pageable pageable, Long groupId, Long userId);

  /**
   * Supprime l'AssociationGroupUser du groupe portant l'id pass� en param�tre
   * 
   * @param groupId id du groupe
   */
  void deleteByGroupId(Long groupId);

  /**
   * Supprime l'AssociationGroupUser de l'utilisateur portant l'id pass� en param�tre
   * 
   * @param userId id de l'utilisateur
   */
  void deleteByUserId(Long userId);

}
