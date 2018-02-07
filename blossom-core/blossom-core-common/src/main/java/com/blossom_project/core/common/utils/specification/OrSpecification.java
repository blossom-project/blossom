package com.blossom_project.core.common.utils.specification;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.List;

public class OrSpecification<T> extends AbstractCompositeSpecification<T> {
  @Override
  public boolean isSatisfiedBy(final T candidate) {
    boolean result = false;

    for (ISpecification<T> specification : this.specifications) {
      result |= specification.isSatisfiedBy(candidate);
    }
    return result;
  }

  public OrSpecification(ISpecification<T>... specifications) {
    super(specifications);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("OR(");
    List<String> specificationsAsString = Lists.newArrayList();

    for (ISpecification<T> specification : this.specifications) {
      specificationsAsString.add(specification.toString());
    }
    sb.append(Joiner.on(",").join(specificationsAsString));
    sb.append(")");
    return sb.toString();
  }
}
