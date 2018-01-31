package fr.blossom.core.common.utils.specification;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;

@RunWith(MockitoJUnitRunner.class)
public class AbstractCompositeSpecificationTest {

  @Test
  public void should_compose_or_specification() throws Exception {
    ISpecification<String> specification =
      new AlwaysSatisfiedSpecification<String>()
        .or(new AlwaysSatisfiedSpecification<>());
    assertTrue(specification instanceof OrSpecification);
  }

  @Test
  public void should_compose_and_specification() throws Exception {
    ISpecification<String> specification =
      new AlwaysSatisfiedSpecification<String>()
        .and(new AlwaysSatisfiedSpecification<>());
    assertTrue(specification instanceof AndSpecification);
  }

  @Test
  public void should_compose_not_specification() throws Exception {
    ISpecification<String> specification =
      new AlwaysSatisfiedSpecification<String>().not();
    assertTrue(specification instanceof NotSpecification);
  }
}
