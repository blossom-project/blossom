package fr.mgargadennec.blossom.security.support.security.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.collect.Lists;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

import fr.mgargadennec.blossom.core.association.group.entity.model.BlossomAssociationGroupEntityPO;
import fr.mgargadennec.blossom.core.association.group.entity.model.QBlossomAssociationGroupEntityPO;
import fr.mgargadennec.blossom.core.association.group.entity.repository.BlossomAssociationGroupEntityRepository;
import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.QBlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.service.dto.BlossomBaseRightServiceDTO;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomSecurityScopeDelegate;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomSecurityUtilService;

public class BlossomAuthenticationUtilServiceGroupBasedImpl implements IBlossomAuthenticationUtilService {
  private BlossomAssociationGroupEntityRepository boAssociationGroupRepository;
  private IBlossomSecurityUtilService boSecurityUtilService;
  private PluginRegistry<IBlossomEntityDefinition, String> entityDefinitionRegistry;
  private PluginRegistry<IBlossomSecurityScopeDelegate, String> scopeDelegateRegistry;

  public BlossomAuthenticationUtilServiceGroupBasedImpl(BlossomAssociationGroupEntityRepository boAssociationGroupRepository,
      IBlossomSecurityUtilService boSecurityUtilService, PluginRegistry<IBlossomEntityDefinition, String> entityBuilderRegistry,
      PluginRegistry<IBlossomSecurityScopeDelegate, String> scopeDelegateRegistry) {
    this.boAssociationGroupRepository = boAssociationGroupRepository;
    this.boSecurityUtilService = boSecurityUtilService;
    this.entityDefinitionRegistry = entityBuilderRegistry;
    this.scopeDelegateRegistry = scopeDelegateRegistry;
  }

  @Override
  public BlossomBaseRightServiceDTO right(String rightName, BlossomRightPermissionEnum... clearance) {
    BlossomBaseRightServiceDTO rightServiceDTO = new BlossomBaseRightServiceDTO();

    rightServiceDTO.setResource(rightName);
    rightServiceDTO.setPermissions(Arrays.asList(clearance));

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
  public Authentication beginAs(boolean keepGroups, boolean addRootGroup, BlossomBaseRightServiceDTO... rights) {
    Authentication previousAuthentication = SecurityContextHolder.getContext().getAuthentication();

    List<BlossomBaseRightServiceDTO> mergedRights = Lists.newArrayList(rights);

    mergedRights.addAll(extractRightAuthorithies(previousAuthentication));

    List<BlossomGroupGrantedAuthority> groups = new ArrayList<BlossomGroupGrantedAuthority>();

    if (keepGroups) {
      groups = extractGroupGrantedAuthorithy(previousAuthentication);
    }
    if (addRootGroup) {
      groups.add(new BlossomGroupGrantedAuthority(BlossomConst.SECURITY_GROUP_ROOT_ID));
    }

    configureAuthentication(groups, mergedRights);

    return previousAuthentication;
  }

  @Override
  public void endAs(Authentication previousAuthentication) {
    configureAuthentication(previousAuthentication);
  }

  /*
   * (non-Javadoc)
   * 
   * @see fr.lotus.bo.support.security.IBoAuthenticationUtilService#runAs(java.lang.Runnable,
   * fr.lotus.bo.service.right.dto.BlossomBaseRightServiceDTO[])
   */
  @Override
  public void runAs(Runnable runnable, boolean keepGroups, boolean addRootGroup, BlossomBaseRightServiceDTO... rights) {

    Authentication previousAuthentication = beginAs(keepGroups, addRootGroup, rights);

    try {
      runnable.run();
    } finally {
      endAs(previousAuthentication);
    }
  }

  @Override
  public boolean isRoot(Authentication authentication) {
    if (authentication != null) {
      return getAuthoritySet(authentication).contains(BlossomConst.SECURITY_ROOT_AUTHORITY);
    }
    return false;

  }

  @Override
  public boolean hasRightForEntity(Authentication authentication, String entityName, BlossomRightPermissionEnum permission) {
    if (authentication != null) {
      return isRoot(authentication) || hasRightForEntity(authentication.getAuthorities(), entityName, permission);
    }
    return false;
  }

  @Override
  public boolean hasRight(Authentication authentication, String rightName, BlossomRightPermissionEnum permission) {
    if (authentication != null) {
      return isRoot(authentication) || hasRight(authentication.getAuthorities(), rightName, permission);
    }
    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, String entityName, Long resourceId,
      BlossomRightPermissionEnum permission) {

    if (authentication != null) {
      if (isRoot(authentication)) {
        return true;
      }

      if (permission != null && !hasRightForEntity(authentication.getAuthorities(), entityName, permission)) {
        return false;
      }

      if (resourceId != null) {
        List<Long> groupsId = getGroupIds(authentication.getAuthorities());
        if (groupsId.contains(BlossomConst.SECURITY_GROUP_ROOT_ID)) {
          return true;
        }

        IBlossomEntityDefinition entityDefinition = entityDefinitionRegistry.getPluginFor(entityName);
        Class entityClass = entityDefinition.getEntityClass();
        Set<String> delegatedEntities = new HashSet<String>();
        while (!delegatedEntities.contains(entityName) && scopeDelegateRegistry.hasPluginFor(entityName)) {
          delegatedEntities.add(entityName);
          IBlossomSecurityScopeDelegate scopeDelegate = scopeDelegateRegistry.getPluginFor(entityName);
          BlossomAbstractEntity delegateScopeObject = scopeDelegate.getDelegateScopeObject(entityClass, resourceId);
          if (delegateScopeObject != null) {
            entityName = scopeDelegate.getDelegateScopeEntityName(entityClass, resourceId);
            entityClass = delegateScopeObject.getClass();
            resourceId = delegateScopeObject.getId();
          } else {
            return true;
          }
        }

        if (!exists(authentication.getName(), entityClass, resourceId)
            || isCreator(authentication.getName(), entityClass, resourceId)) {
          return true;
        }

        Page<BlossomAssociationGroupEntityPO> groups = boAssociationGroupRepository.findAllByEntityIdAndEntityType(null,
            resourceId, entityName);

        for (BlossomAssociationGroupEntityPO group : groups.getContent()) {
          for (Long grantedGroupId : groupsId) {
            if (group.getGroupId().equals(grantedGroupId)) {
              return true;
            }
          }
        }
        return false;
      } else {
        return true;
      }
    }
    return false;

  }

  protected boolean exists(String name, Class entityClass, Long resourceId) {
    if (entityClass != null) {
      BlossomAbstractEntity result = boSecurityUtilService.getEntityByIdAndType(entityClass, resourceId);

      return result != null;
    }
    return false;
  }

  protected boolean isCreator(String name, Class entityClass, Long resourceId) {
    if (entityClass != null) {
      BlossomAbstractEntity result = boSecurityUtilService.getEntityByIdAndType(entityClass, resourceId);
      return result != null && result.getUserCreation() != null && result.getUserCreation().equals(name);
    }
    return false;
  }

  @Override
  public boolean hasGroup(Authentication authentication, long idGroup) {
    return authentication != null ? getGroupIds(authentication.getAuthorities()).contains(idGroup) : false;
  }

  @Override
  public List<Long> getGroupIds(Collection<? extends GrantedAuthority> authorities) {
    List<Long> groupIds = new ArrayList<Long>();
    if (authorities != null) {
      for (GrantedAuthority grantedAuthority : authorities) {
        if (grantedAuthority instanceof BlossomGroupGrantedAuthority) {
          BlossomGroupGrantedAuthority groupAuthority = (BlossomGroupGrantedAuthority) grantedAuthority;
          groupIds.add(groupAuthority.getGroupId());
        }
      }
    }
    return groupIds;
  }

  /**
   * Private Methods
   */

  private List<BlossomGroupGrantedAuthority> extractGroupGrantedAuthorithy(Authentication previousAuthentication) {
    List<BlossomGroupGrantedAuthority> groups = new ArrayList<BlossomGroupGrantedAuthority>();
    if (previousAuthentication != null && previousAuthentication.getAuthorities() != null) {
      Collection<? extends GrantedAuthority> authorities = previousAuthentication.getAuthorities();
      for (GrantedAuthority grantedAuthority : authorities) {
        if (grantedAuthority instanceof BlossomGroupGrantedAuthority) {
          BlossomGroupGrantedAuthority groupAuthority = (BlossomGroupGrantedAuthority) grantedAuthority;
          groups.add(groupAuthority);
        }
      } 
    }
    return groups;
  }

  private List<BlossomBaseRightServiceDTO> extractRightAuthorithies(Authentication previousAuthentication) {
    List<BlossomBaseRightServiceDTO> rights = new ArrayList<BlossomBaseRightServiceDTO>();
    if (previousAuthentication != null && previousAuthentication.getAuthorities() != null) {
      Collection<? extends GrantedAuthority> authorities = previousAuthentication.getAuthorities();
      for (GrantedAuthority grantedAuthority : authorities) {
        if (grantedAuthority instanceof BlossomBaseRightServiceDTO) {
          BlossomBaseRightServiceDTO rightAuthority = (BlossomBaseRightServiceDTO) grantedAuthority;
          rights.add(rightAuthority);
        }
      }
    }
    return rights;
  }

  protected void configureAuthentication(List<BlossomGroupGrantedAuthority> groups, List<BlossomBaseRightServiceDTO> rights) {
    List<GrantedAuthority> grants = new ArrayList<GrantedAuthority>();
    if (groups != null) {
      grants.addAll(groups);
    }
    if (rights != null) {
      grants.addAll(rights);
    }
    Authentication authentication = new UsernamePasswordAuthenticationToken("system", null, grants);
    configureAuthentication(authentication);
  }

  protected void configureAuthentication(Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private Set<String> getAuthoritySet(Authentication authentication) {
    Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();

    return AuthorityUtils.authorityListToSet(userAuthorities);
  }

  protected boolean hasRightForEntity(Collection<? extends GrantedAuthority> authorities, String entityName,
      BlossomRightPermissionEnum permission) {
    IBlossomEntityDefinition entityDefinition = entityDefinitionRegistry.getPluginFor(entityName);
    String[] rightNames = entityDefinition.getRightNames();
    if (rightNames != null && rightNames.length > 0) {
      for (String rightName : rightNames) {
        if (!hasRight(authorities, rightName, permission)) {
          return false;
        }
      }
    }
    return true;
  }

  protected boolean hasRight(Collection<? extends GrantedAuthority> authorities, String rightName,
      BlossomRightPermissionEnum permission) {
    for (GrantedAuthority grantedAuthority : authorities) {
      if (grantedAuthority instanceof BlossomBaseRightServiceDTO) {
        BlossomBaseRightServiceDTO rightDTO = (BlossomBaseRightServiceDTO) grantedAuthority;
        if (rightName.equals(rightDTO.getResource())) {
          for (BlossomRightPermissionEnum rightPermission : rightDTO.getPermissions()) {
            if (rightPermission == permission) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  @Override
  public Predicate getAllPredicate(Class entityClass) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && !isRoot(auth)) {
      String resourceName = boSecurityUtilService.getScopeResourceNameFromClass(entityClass);
      if (resourceName != null) {
        List<Long> groupIds = getGroupIds(auth.getAuthorities());
        if (!groupIds.contains(BlossomConst.SECURITY_GROUP_ROOT_ID)) {
          Predicate defaultPredicate = getDefaultAllPredicate(entityClass, auth, resourceName, groupIds);
          Predicate result = defaultPredicate;
          if (scopeDelegateRegistry.hasPluginFor(resourceName)) {
            result = scopeDelegateRegistry.getPluginFor(resourceName).getAllPredicate(boSecurityUtilService, auth,
                defaultPredicate, entityClass, groupIds);
          }
          return result;
        }
      }
    }
    return null;
  }

  private Predicate getDefaultAllPredicate(Class entityClass, Authentication auth, String resourceName,
      List<Long> groupIds) {
    QBlossomAssociationGroupEntityPO subQ = QBlossomAssociationGroupEntityPO.blossomAssociationGroupEntityPO;
    String entityPathName = entityClass.getSimpleName().substring(0, 1).toLowerCase()
        + entityClass.getSimpleName().substring(1);
    Path<? extends BlossomAbstractEntity> entityPath = Expressions.path(entityClass, entityPathName);
    QBlossomAbstractEntity boEntityPath = new QBlossomAbstractEntity(entityPath);
    return boEntityPath.userCreation.eq(auth.getName()).or(
        boEntityPath.id.in(new JPASubQuery().from(subQ)
            .where(subQ.entityType.eq(resourceName).and(subQ.groupId.in(groupIds))).list(subQ.entityId)));
  }
}
