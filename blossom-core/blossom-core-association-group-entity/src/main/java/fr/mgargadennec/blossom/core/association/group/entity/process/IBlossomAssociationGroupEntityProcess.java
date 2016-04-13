package fr.mgargadennec.blossom.core.association.group.entity.process;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import fr.mgargadennec.blossom.core.association.group.entity.model.BlossomAssociationGroupEntityPO;
import fr.mgargadennec.blossom.core.association.group.entity.process.dto.BlossomAssociationGroupEntityProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.IBlossomGenericProcess;

public interface IBlossomAssociationGroupEntityProcess extends
    IBlossomGenericProcess<BlossomAssociationGroupEntityPO, BlossomAssociationGroupEntityProcessDTO> {

  @PreAuthorize("hasPermission(#processDTO.entityType,#secUtils.clearance())")
  BlossomAssociationGroupEntityProcessDTO create(@P("processDTO") BlossomAssociationGroupEntityProcessDTO processDTO);

  @PreAuthorize("hasPermission(#secUtils.getEntityByIdAndType(T(fr.mgargadennec.blossom.core.association.group.entity.model.BlossomAssociationGroupEntityPO),#id).entityType,#secUtils.clearance())")
  void delete(@P("id") Long id);

  @PostAuthorize("hasPermission(returnObject.entityType,#secUtils.clearance())")
  BlossomAssociationGroupEntityProcessDTO get(Long id);

  @PreAuthorize("hasPermission(#processDTO.entityType,#secUtils.clearance())")
  BlossomAssociationGroupEntityProcessDTO update(@P("processDTO") BlossomAssociationGroupEntityProcessDTO processDTO);

  @PreAuthorize("hasPermission(#entityType,#secUtils.clearance())")
  Page<BlossomAssociationGroupEntityProcessDTO> getAllByEntityIdAndEntityType(Pageable pageable, Long entityId,
      @P("entityType") String entityType);

  @PreAuthorize("hasPermission(#entityType,#secUtils.clearance())")
  void deleteByGroupIdAndEntityIdAndEntityType(Long groupId, Long entityId, @P("entityType") String entityType);

  @PreAuthorize("hasPermission(#entityType,#secUtils.clearance())")
  void deleteByEntityIdAndEntityType(Long entityId, String entityType);

  @PreAuthorize("hasPermission(#entityType,#secUtils.clearance())")
  Page<BlossomAssociationGroupEntityProcessDTO> getAllByEntityType(Pageable pageable, String entityType);

  @PreAuthorize("hasPermission(#entityType,#secUtils.clearance())")
  Page<BlossomAssociationGroupEntityProcessDTO> getAllByEntityTypeAndGroupId(Pageable pageable,
      @P("entityType") String resourceType, Long groupId);

  @PreAuthorize("hasPermission(#entityType,#secUtils.clearance())")
  Page<BlossomAssociationGroupEntityProcessDTO> getAllByEntityIdAndEntityTypeAndGroupId(Pageable pageable, Long entityId,
      @P("entityType") String entityType, Long groupId);

  void deleteByGroupId(Long groupId);

}
