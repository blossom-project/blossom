package fr.mgargadennec.blossom.security.core.support.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;
import org.springframework.security.core.Authentication;

import com.mysema.query.types.Predicate;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;

@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_SECURITY_SCOPE_DELEGATE_REGISTRY)
public interface IBlossomSecurityScopeDelegate extends Plugin<String> {

  /**
   * Delegate Scope control of instance to scopes of return object
   *
   * @return The object to retrieve scope from
   */
  public BlossomAbstractEntity getDelegateScopeObject(Class<? extends BlossomAbstractEntity> clazz, long id);

  public String getDelegateScopeEntityName(Class<? extends BlossomAbstractEntity> clazz, long id);

  public abstract Predicate getAllPredicate(IBlossomSecurityUtilService boSecurityUtilService,
      Authentication authentication, Predicate defaultPredicate, Class entityClass, List<Long> groupIds);

}
