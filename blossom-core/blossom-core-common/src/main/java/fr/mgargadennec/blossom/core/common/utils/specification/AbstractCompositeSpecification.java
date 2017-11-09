package fr.mgargadennec.blossom.core.common.utils.specification;

import com.google.common.collect.Lists;
import java.util.List;

public abstract class AbstractCompositeSpecification<T> implements ISpecification<T> {
  protected List<ISpecification<T>> specifications;

  protected AbstractCompositeSpecification(ISpecification<T>... specifications) {
    this.specifications = Lists.newArrayList();

    for (ISpecification<T> specification : specifications) {
      this.specifications.add(specification);
    }
  }

  protected AbstractCompositeSpecification() {
    this.specifications = Lists.newArrayList();
  }

  public ISpecification<T> or(ISpecification<T> specification) {
    return new OrSpecification<>(this, specification);
  }

  public ISpecification<T> and(ISpecification<T> specification) {
    return new AndSpecification<>(this, specification);
  }

  public ISpecification<T> not() {
    return new NotSpecification<>(this);
  }

}
