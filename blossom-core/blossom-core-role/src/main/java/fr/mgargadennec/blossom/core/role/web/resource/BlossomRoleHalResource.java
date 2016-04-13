package fr.mgargadennec.blossom.core.role.web.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

@Relation(value = "role", collectionRelation = "roles")
public class BlossomRoleHalResource extends Resource<BlossomRoleResourceState> {

  public BlossomRoleHalResource(BlossomRoleResourceState content) {
    super(content, new Link[]{});
  }

  public BlossomRoleHalResource() {
    this(new BlossomRoleResourceState());
  }
}
