package com.blossomproject.core.common.utils.specification;

public class AlwaysSatisfiedSpecification<T> extends LeafSpecification<T> {

  @Override
  public boolean isSatisfiedBy(final T candidate) {
    return true;
  }

  @Override
  public String toString() {
    return "TrueSpecification{}";
  }
}
