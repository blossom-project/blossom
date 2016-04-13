package fr.mgargadennec.blossom.core.association.user.role.web.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

@Relation(value = "right", collectionRelation = "rights")
public class BlossomRightHalResource extends Resource<BlossomRightResourceState> {
  public BlossomRightHalResource(BlossomRightResourceState content) {
    super(content, new Link[]{});
  }

  public BlossomRightHalResource() {
    this(new BlossomRightResourceState());
  }
}
