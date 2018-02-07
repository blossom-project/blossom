package com.blossom_project.core.common.service;

import com.blossom_project.core.common.PluginConstants;
import com.blossom_project.core.common.dto.AbstractAssociationDTO;
import com.blossom_project.core.common.dto.AbstractDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

@Qualifier(value = PluginConstants.PLUGIN_ASSOCIATION_SERVICE)
public interface AssociationServicePlugin extends Plugin<Class<? extends AbstractDTO>> {

  <A extends AbstractDTO> Class<A> getAClass();

  <B extends AbstractDTO> Class<B> getBClass();

  void dissociateAll(AbstractDTO o);

  List<? extends AbstractAssociationDTO> getAssociations(AbstractDTO o);

}
