package fr.mgargadennec.blossom.security.core.support.security;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;

public interface IBlossomSecurityUtilService {

  String getScopeResourceNameFromObject(Object po);

  String getScopeResourceNameFromClass(Class<?> clazz);

  IBlossomEntityDefinition getEntityDefinitionFromClass(Class<?> clazz);

  BlossomAbstractEntity getEntityByIdAndType(Class<?> type, Long id);

  String read();

  String write();

  String create();

  String delete();

  String clearance();

}