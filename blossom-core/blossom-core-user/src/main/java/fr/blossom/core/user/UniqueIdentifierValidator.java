package fr.blossom.core.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Preconditions;

class UniqueIdentifierValidator implements ConstraintValidator<UniqueIdentifier, String> {

  private UserRepository userRepository;

  public UniqueIdentifierValidator(UserRepository userRepository) {
    Preconditions.checkNotNull(userRepository);
    this.userRepository = userRepository;
  }

  public void initialize(UniqueIdentifier constraint) {
  }

  public boolean isValid(String identifier, ConstraintValidatorContext context) {
    return identifier != null && !userRepository.findOneByIdentifier(identifier).isPresent();
  }

}
