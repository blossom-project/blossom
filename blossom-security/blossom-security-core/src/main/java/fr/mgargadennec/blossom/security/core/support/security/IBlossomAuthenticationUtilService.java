package fr.mgargadennec.blossom.security.core.support.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.mysema.query.types.Predicate;

import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.service.dto.BlossomBaseRightServiceDTO;

public interface IBlossomAuthenticationUtilService {

  Authentication beginAs(boolean keepGroups, boolean addRootGroup, BlossomBaseRightServiceDTO... right);

  void endAs(Authentication previousAuthentication);

  void runAs(Runnable runnable, boolean keepGroups, boolean addRootGroup, BlossomBaseRightServiceDTO... rights);

  BlossomBaseRightServiceDTO right(String rightName, BlossomRightPermissionEnum... permissions);

  BlossomBaseRightServiceDTO[] rightsForEntity(String entityName, BlossomRightPermissionEnum... clearance);

  boolean hasRight(Authentication authentication, String rightName, BlossomRightPermissionEnum permission);

  boolean hasRightForEntity(Authentication authentication, String entityName, BlossomRightPermissionEnum permission);

  boolean hasPermission(Authentication authentication, String entityName, Long resourceId,
      BlossomRightPermissionEnum permission);

  List<Long> getGroupIds(Collection<? extends GrantedAuthority> authorities);

  boolean hasGroup(Authentication authentication, long idGroup);

  boolean isRoot(Authentication authentication);

  Predicate getAllPredicate(Class<?> entityClass);

}
