package fr.mgargadennec.blossom.core.user.web.resources;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.Relation;

import fr.mgargadennec.blossom.core.common.web.resources.BlossomResourceWithEmbeds;

@Relation(value = "user", collectionRelation = "users")
public class BlossomUserHalResource extends BlossomResourceWithEmbeds<BlossomUserResourceState> {

  public BlossomUserHalResource(BlossomUserResourceState content, Resources embeddeds) {
    super(content, new Link[]{}, embeddeds);
  }

  public BlossomUserHalResource() {
    this(new BlossomUserResourceState(), null);
  }
}
