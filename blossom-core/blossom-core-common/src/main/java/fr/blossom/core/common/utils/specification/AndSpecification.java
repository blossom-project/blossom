package fr.blossom.core.common.utils.specification;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.ArrayList;

public class AndSpecification<T> extends AbstractCompositeSpecification<T> {

  @Override
  public boolean isSatisfiedBy(final T candidate) {
    boolean result = true;

    for (ISpecification<T> specification : this.specifications) {
      result &= specification.isSatisfiedBy(candidate);
    }
    return result;
  }

  public AndSpecification(ISpecification<T>... specifications) {
    super(specifications);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("AND(");
    ArrayList<Object> specificationsAsString = Lists.newArrayList();

    for (ISpecification<T> specification : this.specifications) {
      specificationsAsString.add(specification.toString());
    }
    sb.append(Joiner.on(",").join(specificationsAsString));
    sb.append(")");
    return sb.toString();
  }
}
