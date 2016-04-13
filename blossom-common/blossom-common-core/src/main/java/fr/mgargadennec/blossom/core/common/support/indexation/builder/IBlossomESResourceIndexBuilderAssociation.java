package fr.mgargadennec.blossom.core.common.support.indexation.builder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;

@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_ASSOCIATIONS_REGISTRY)
public interface IBlossomESResourceIndexBuilderAssociation extends Plugin<String> {

  public <T  extends BlossomAbstractServiceDTO> BlossomIndexationAssociationDTO<T> getAssociationInfos();

}
