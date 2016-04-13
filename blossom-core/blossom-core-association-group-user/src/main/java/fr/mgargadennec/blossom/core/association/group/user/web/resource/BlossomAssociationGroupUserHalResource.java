package fr.mgargadennec.blossom.core.association.group.user.web.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.Relation;

import fr.mgargadennec.blossom.core.common.web.resources.BlossomResourceWithEmbeds;

@Relation(value = "membership", collectionRelation = "memberships")
public class BlossomAssociationGroupUserHalResource extends BlossomResourceWithEmbeds<BlossomAssociationGroupUserResourceState> {

  public BlossomAssociationGroupUserHalResource(BlossomAssociationGroupUserResourceState content, Resources embeddeds) {
    super(content, new Link[]{}, embeddeds);
  }

  public BlossomAssociationGroupUserHalResource(BlossomAssociationGroupUserResourceState content) {
    this(content, null);
  }

  public BlossomAssociationGroupUserHalResource() {
    this(new BlossomAssociationGroupUserResourceState(), null);
  }
}