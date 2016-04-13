package fr.mgargadennec.blossom.core.common.web.assembler;

import java.io.Serializable;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Resource;

public abstract class BlossomDefaultResourceAssembler<X, T extends Identifiable<?>, D extends Resource<T>> extends
    BlossomResourceSupportAssembler<X, T, D> {

  public BlossomDefaultResourceAssembler(Class<?> controllerClass) {
    super(controllerClass, Resource.class);
  }

  @Override
  public D toResource(X entity) {
    return createResourceWithId(getId(entity), entity);
  }

  protected abstract Serializable getId(X entity);

  public X fromResource(D resource) {
    return fromResourceState(resource.getContent());
  }
}
