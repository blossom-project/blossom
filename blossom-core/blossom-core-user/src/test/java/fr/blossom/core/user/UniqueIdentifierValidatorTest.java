package fr.blossom.core.user;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UniqueIdentifierValidatorTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private UserRepository userRepository;

  @Spy
  @InjectMocks
  private UniqueIdentifierValidator validator;

  @Test
  public void test_is_valid_not_null_and_not_present_should_return_true() throws Exception {
    BDDMockito.given(userRepository.findOneByIdentifier(BDDMockito.anyString())).willReturn(Optional.ofNullable(null));
    Assert.assertTrue(validator.isValid("any", null));

  }

  @Test
  public void test_is_valid_null_and_not_present_should_return_false() throws Exception {
    Assert.assertFalse(validator.isValid(null, null));
  }

  @Test
  public void test_is_valid_null_and_present_should_return_false() throws Exception {
    Assert.assertFalse(validator.isValid(null, null));
  }

  @Test
  public void test_is_valid_not_null_and_present_should_return_false() throws Exception {
    BDDMockito.given(userRepository.findOneByIdentifier(BDDMockito.anyString())).willReturn(
        Optional.ofNullable(new User()));
    Assert.assertFalse(validator.isValid("any", null));
  }

  @Test
  public void test_unique_email_validator_user_repository_not_null() throws Exception {
    new UniqueIdentifierValidator(userRepository);
  }

  @Test
  public void test_unique_email_validator_user_repository_null() throws Exception {
    thrown.expect(NullPointerException.class);
    new UniqueIdentifierValidator(null);
  }
}
