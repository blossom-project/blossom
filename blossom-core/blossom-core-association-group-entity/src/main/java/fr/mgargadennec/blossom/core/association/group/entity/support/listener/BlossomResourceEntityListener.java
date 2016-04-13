package fr.mgargadennec.blossom.core.association.group.entity.support.listener;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PreRemove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mgargadennec.blossom.core.association.group.entity.repository.BlossomAssociationGroupEntityRepository;
import fr.mgargadennec.blossom.core.common.support.BlossomAutowireHelper;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomSecurityUtilService;

public class BlossomResourceEntityListener {

  private Logger logger = LoggerFactory.getLogger(BlossomResourceEntityListener.class);

  private EntityManagerFactory entityManagerFactory;
  private BlossomAssociationGroupEntityRepository groupEntityRepository;
  private IBlossomSecurityUtilService boSecurityUtilService;

  @PreRemove
  public void preRemove(Object entity) {
    try {
      if (groupEntityRepository == null || entityManagerFactory == null) {
        BlossomAutowireHelper.autowire(this, this.groupEntityRepository);
        BlossomAutowireHelper.autowire(this, this.entityManagerFactory);
      }
      
      if (entity != null && groupEntityRepository != null && entityManagerFactory != null && canBeScopeResource(entity)) {
        Object identifier = entityManagerFactory.getPersistenceUnitUtil().getIdentifier(entity);
        if (identifier instanceof Long) {
          IBlossomEntityDefinition boEntityDefinition = boSecurityUtilService
              .getEntityDefinitionFromClass(entity.getClass());

          if (boEntityDefinition != null && boEntityDefinition.getEntityName() != null) {
            groupEntityRepository.deleteByEntityIdAndEntityType((Long) identifier, boEntityDefinition.getEntityName());
          }
        }
      }

    } catch (Exception e) {
      logger.error("Erreur lors de la suppression d'une entity " + entity, e);
    }

  }

  private boolean canBeScopeResource(Object entity) {
    return true;
    		//!(
//    		entity instanceof BlossomAssociationGroupEntityPO 
//    		|| entity instanceof BlossomAssociationGroupUserPO
//    		|| entity instanceof BlossomAssociationUserRolePO 
//    		|| entity instanceof BlossomGroupPO 
//    		|| entity instanceof BlossomUserPO
//    		|| entity instanceof BlossomRolePO 
//    		|| entity instanceof BlossomRightPO
//    		/);
  }
}
