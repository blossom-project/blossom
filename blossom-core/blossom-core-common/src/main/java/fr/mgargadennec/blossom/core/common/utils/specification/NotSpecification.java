package fr.mgargadennec.blossom.core.common.utils.specification;

import com.google.common.collect.Iterables;

public class NotSpecification<T> extends AbstractCompositeSpecification<T> {

  @Override
  public boolean isSatisfiedBy(final T candidate) {
    return !Iterables.getOnlyElement(this.specifications).isSatisfiedBy(candidate);
  }

  public NotSpecification(ISpecification<T> specification) {
    super(specification);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("NOT(");
    sb.append(Iterables.getOnlyElement(this.specifications).toString());
    sb.append(")");
    return sb.toString();
  }
}
