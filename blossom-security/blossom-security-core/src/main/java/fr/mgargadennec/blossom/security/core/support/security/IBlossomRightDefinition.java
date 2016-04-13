package fr.mgargadennec.blossom.security.core.support.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;

@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RIGHT_DEFINITION_REGISTRY)
public interface IBlossomRightDefinition extends Plugin<String> {

  public String getRightName();

  public List<BlossomRightPermissionEnum> getAvailablePermissions();

  public boolean isAssociation();

}
