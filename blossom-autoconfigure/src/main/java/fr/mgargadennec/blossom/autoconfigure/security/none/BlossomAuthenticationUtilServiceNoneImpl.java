package fr.mgargadennec.blossom.autoconfigure.security.none;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;

import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.service.dto.BlossomBaseRightServiceDTO;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

public class BlossomAuthenticationUtilServiceNoneImpl implements IBlossomAuthenticationUtilService {
  private PluginRegistry<IBlossomEntityDefinition, String> entityDefinitionRegistry;

  public BlossomAuthenticationUtilServiceNoneImpl(
      PluginRegistry<IBlossomEntityDefinition, String> entityDefinitionRegistry) {
    this.entityDefinitionRegistry = entityDefinitionRegistry;
  }

  @Override
  public Authentication beginAs(boolean keepGroups, boolean addRootGroup, BlossomBaseRightServiceDTO... right) {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  @Override
  public void endAs(Authentication previousAuthentication) {
    SecurityContextHolder.getContext().setAuthentication(previousAuthentication);
  }

  @Override
  public void runAs(Runnable runnable, boolean keepGroups, boolean addRootGroup, BlossomBaseRightServiceDTO... rights) {
    runnable.run();
  }

  @Override
  public BlossomBaseRightServiceDTO right(String rightName, BlossomRightPermissionEnum... permissions) {
    BlossomBaseRightServiceDTO rightServiceDTO = new BlossomBaseRightServiceDTO();
    rightServiceDTO.setResource(rightName);
    rightServiceDTO.setPermissions(Arrays.asList(permissions));
    return rightServiceDTO;
  }

  @Override
  public BlossomBaseRightServiceDTO[] rightsForEntity(String entityName, BlossomRightPermissionEnum... clearance) {
    IBlossomEntityDefinition entityDefinition = entityDefinitionRegistry.getPluginFor(entityName);
    String[] rightNames = entityDefinition.getRightNames();
    if (rightNames != null && rightNames.length > 0) {
      List<BlossomBaseRightServiceDTO> rights = new ArrayList<BlossomBaseRightServiceDTO>();
      for (String rightName : rightNames) {
        rights.add(right(rightName, clearance));
      }
      return rights.toArray(new BlossomBaseRightServiceDTO[]{});
    }
    return new BlossomBaseRightServiceDTO[0];
  }

  @Override
  public boolean hasRight(Authentication authentication, String rightName, BlossomRightPermissionEnum permission) {
    return true;
  }

  @Override
  public boolean hasRightForEntity(Authentication authentication, String entityName,
      BlossomRightPermissionEnum permission) {
    return true;
  }

  @Override
  public boolean hasPermission(Authentication authentication, String entityName, Long resourceId,
      BlossomRightPermissionEnum permission) {
    return true;
  }

  @Override
  public List<Long> getGroupIds(Collection<? extends GrantedAuthority> authorities) {
    return Lists.newArrayList();
  }

  @Override
  public boolean hasGroup(Authentication authentication, long idGroup) {
    return true;
  }

  @Override
  public boolean isRoot(Authentication authentication) {
    return true;
  }

  @Override
  public Predicate getAllPredicate(Class<?> entityClass) {
    return null;
  }

}
