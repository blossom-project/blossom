package fr.mgargadennec.blossom.core.common.web.assembler;

import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public abstract class BlossomResourceSupportAssembler<X, T extends Identifiable<?>, D extends ResourceSupport> extends
    ResourceAssemblerSupport<X, D> {

  public BlossomResourceSupportAssembler(Class<?> controllerClass, Class<? super D> resourceClass) {
    super(controllerClass, (Class<D>) resourceClass);
  }

  @Override
  protected D instantiateResource(X resourceState) {
    return createResource(toResourceState(resourceState));
  }

  protected abstract D createResource(T resourceState);

  public abstract T toResourceState(X serviceDTO);

  public abstract X fromResourceState(T resourceState);

}
