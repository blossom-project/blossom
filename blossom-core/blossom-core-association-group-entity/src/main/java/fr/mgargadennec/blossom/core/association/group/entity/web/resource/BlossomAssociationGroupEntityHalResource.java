package fr.mgargadennec.blossom.core.association.group.entity.web.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.Relation;

import fr.mgargadennec.blossom.core.common.web.resources.BlossomResourceWithEmbeds;

@Relation(value = "authorization", collectionRelation = "authorizations")
public class BlossomAssociationGroupEntityHalResource extends BlossomResourceWithEmbeds<BlossomAssociationGroupEntityResourceState> {

  public BlossomAssociationGroupEntityHalResource(BlossomAssociationGroupEntityResourceState content, Resources embeddeds) {
    super(content, new Link[]{}, embeddeds);
  }

  public BlossomAssociationGroupEntityHalResource(BlossomAssociationGroupEntityResourceState content) {
    this(content, null);
  }

  public BlossomAssociationGroupEntityHalResource() {
    this(new BlossomAssociationGroupEntityResourceState());
  }

}
