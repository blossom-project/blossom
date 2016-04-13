package fr.mgargadennec.blossom.core.right.process.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.common.process.generic.impl.BlossomGenericProcessImpl;
import fr.mgargadennec.blossom.core.right.dao.IBlossomRightDAO;
import fr.mgargadennec.blossom.core.right.model.BlossomRightPO;
import fr.mgargadennec.blossom.core.right.process.IBlossomRightProcess;
import fr.mgargadennec.blossom.core.right.process.dto.BlossomRightProcessDTO;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;

/**
 * Couche Process - Right
 */
public class BlossomRightProcessImpl extends BlossomGenericProcessImpl<BlossomRightPO, BlossomRightProcessDTO> implements IBlossomRightProcess {

  private static String PERMISSIONS_STRING_SEPARATOR = "_";
  IBlossomRightDAO boRightDAO;

  public BlossomRightProcessImpl(IBlossomRightDAO boRightDAO) {
    super(boRightDAO);
    this.boRightDAO = boRightDAO;
  }

  public Page<BlossomRightProcessDTO> getByRoleId(Pageable pageable, Long roleId) {
    Page<BlossomRightPO> boRightPOPage = boRightDAO.getByRoleId(pageable, roleId);
    return pageProcessDTOFromPagePO(boRightPOPage, pageable);
  }

  public void deleteByRoleId(Long roleId) {
    boRightDAO.deleteByRoleId(roleId);
  }

  public BlossomRightProcessDTO createProcessDTOfromPO(BlossomRightPO po) {

    if (po == null) {
      return null;
    }
    BlossomRightProcessDTO processDTO = new BlossomRightProcessDTO();
    fillProcessDTOFromPO(processDTO, po);

    return processDTO;
  }

  @Override
  protected void fillProcessDTOFromPO(BlossomRightProcessDTO processDTO, BlossomRightPO po) {
    super.fillProcessDTOFromPO(processDTO, po);
    List<BlossomRightPermissionEnum> permissions = permissionsStringToEnum(po.getPermissions());
    processDTO.setPermissions(permissions);
    processDTO.setResourceName(po.getResourceName());
    processDTO.setRoleId(po.getRoleId());
  }

  @Override
  protected void fillPOFromProcessDTO(BlossomRightPO po, BlossomRightProcessDTO processDTO) {
    super.fillPOFromProcessDTO(po, processDTO);

    po.setPermissions(permissionEnumListToString(processDTO.getPermissions()));
    po.setResourceName(processDTO.getResourceName());
    po.setRoleId(processDTO.getRoleId());
  }

  public BlossomRightPO createPOfromProcessDTO(BlossomRightProcessDTO processDTO) {
    if (processDTO == null) {
      return null;
    }

    BlossomRightPO po = new BlossomRightPO();
    fillPOFromProcessDTO(po, processDTO);

    return po;
  }

  protected List<BlossomRightPermissionEnum> permissionsStringToEnum(String permissionsStr) {
    String[] permissionsArray = permissionsStr != null ? permissionsStr.split(PERMISSIONS_STRING_SEPARATOR)
        : new String[]{};
    List<BlossomRightPermissionEnum> permissions = new ArrayList<BlossomRightPermissionEnum>();
    for (String code : permissionsArray) {
      BlossomRightPermissionEnum e = BlossomRightPermissionEnum.getByCode(code);
      if (e != null) {
        permissions.add(e);
      }
    }
    return permissions;
  }

  protected String permissionEnumListToString(List<BlossomRightPermissionEnum> permissionsList) {
    String result = "";
    if (permissionsList != null) {
      List<String> codes = new ArrayList<String>();
      for (BlossomRightPermissionEnum boRightPermissionEnum : permissionsList) {
        codes.add(boRightPermissionEnum.getCode());
      }
      return StringUtils.join(codes, PERMISSIONS_STRING_SEPARATOR);
    }
    return result;
  }

}
