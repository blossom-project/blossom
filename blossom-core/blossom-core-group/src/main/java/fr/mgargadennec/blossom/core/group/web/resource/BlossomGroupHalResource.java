package fr.mgargadennec.blossom.core.group.web.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

@Relation(value = "group", collectionRelation = "groups")
public class BlossomGroupHalResource extends Resource<BlossomGroupResourceState> {

  public BlossomGroupHalResource(BlossomGroupResourceState content) {
    super(content, new Link[]{});
  }

  public BlossomGroupHalResource() {
    this(new BlossomGroupResourceState());
  }
}
