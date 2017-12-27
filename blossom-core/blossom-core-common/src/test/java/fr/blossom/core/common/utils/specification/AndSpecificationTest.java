package fr.blossom.core.common.utils.specification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AndSpecificationTest {

  @Test
  public void should_satisfied_all() throws Exception {
    AlwaysSatisfiedSpecification a = new AlwaysSatisfiedSpecification();
    AlwaysSatisfiedSpecification b = new AlwaysSatisfiedSpecification();

    AndSpecification<String> specification = new AndSpecification(a, b);
    assertTrue(specification.isSatisfiedBy("test"));
  }

  @Test
  public void should_fail_if_any_fail_all() throws Exception {
    AlwaysSatisfiedSpecification a = new AlwaysSatisfiedSpecification();
    AlwaysSatisfiedSpecification b = new AlwaysSatisfiedSpecification();

    AndSpecification<String> specification = new AndSpecification(a, b.not());
    assertFalse(specification.isSatisfiedBy("test"));
  }

  @Test
  public void should_print() throws Exception {
    AlwaysSatisfiedSpecification a = new AlwaysSatisfiedSpecification();
    AlwaysSatisfiedSpecification b = new AlwaysSatisfiedSpecification();

    AndSpecification<String> specification = new AndSpecification(a, b.not());
    Pattern pattern = Pattern.compile("AND\\(.*\\)");
    assertTrue(pattern.matcher(specification.toString()).matches());
  }
}
