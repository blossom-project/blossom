package fr.blossom.core.common.utils.specification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrSpecificationTest {

  @Test
  public void should_satisfied_if_all_succeed() throws Exception {
    AlwaysSatisfiedSpecification a = new AlwaysSatisfiedSpecification();
    AlwaysSatisfiedSpecification b = new AlwaysSatisfiedSpecification();

    OrSpecification<String> specification = new OrSpecification(a, b);
    assertTrue(specification.isSatisfiedBy("test"));
  }

  @Test
  public void should_validate_if_only_one_succeed() throws Exception {
    AlwaysSatisfiedSpecification a = new AlwaysSatisfiedSpecification();
    AlwaysSatisfiedSpecification b = new AlwaysSatisfiedSpecification();

    OrSpecification<String> specification = new OrSpecification(a, b.not());
    assertTrue(specification.isSatisfiedBy("test"));
  }

  @Test
  public void should_fail_if_all_failed() throws Exception {
    AlwaysSatisfiedSpecification a = new AlwaysSatisfiedSpecification();
    AlwaysSatisfiedSpecification b = new AlwaysSatisfiedSpecification();

    OrSpecification<String> specification = new OrSpecification(a.not(), b.not());
    assertFalse(specification.isSatisfiedBy("test"));
  }

  @Test
  public void should_validate_all() throws Exception {
    AlwaysSatisfiedSpecification a = new AlwaysSatisfiedSpecification();
    AlwaysSatisfiedSpecification b = new AlwaysSatisfiedSpecification();

    OrSpecification<String> specification = new OrSpecification(a, b.not());
    assertTrue(specification.isSatisfiedBy("test"));
  }

  @Test
  public void should_print() throws Exception {
    AlwaysSatisfiedSpecification a = new AlwaysSatisfiedSpecification();
    AlwaysSatisfiedSpecification b = new AlwaysSatisfiedSpecification();

    OrSpecification<String> specification = new OrSpecification(a, b.not());
    Pattern pattern = Pattern.compile("OR\\(.*\\)");
    assertTrue(pattern.matcher(specification.toString()).matches());
  }
}
