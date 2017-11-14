package fr.mgargadennec.blossom.core.common.utils.specification;

public abstract class LeafSpecification<T> extends AbstractCompositeSpecification<T> {

  public abstract boolean isSatisfiedBy(T candidate);

}
