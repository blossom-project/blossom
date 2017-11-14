package fr.blossom.core.common.utils.specification;

public interface ISpecification<T> {
    boolean isSatisfiedBy(T candidate);

    ISpecification<T> or(ISpecification<T> specification);
    ISpecification<T> and(ISpecification<T> specification);
    ISpecification<T> not();
}
