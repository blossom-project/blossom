package fr.blossom.core.user;

import com.google.common.base.Strings;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.reflect.FieldUtils;

class UniqueIdentifierValidator implements ConstraintValidator<UniqueIdentifier, Object> {

  private UserRepository userRepository;
  private String field;
  private String idField;

  public UniqueIdentifierValidator(UserRepository userRepository) {
    Preconditions.checkNotNull(userRepository);
    this.userRepository = userRepository;
  }

  public void initialize(UniqueIdentifier constraint) {
    this.field = constraint.field();
    this.idField = constraint.idField();
  }

  public boolean isValid(Object target, ConstraintValidatorContext context) {
    try {
      Long id = Strings.isNullOrEmpty(idField) ? null : (Long) FieldUtils.readField(target, idField, true);
      String name = (String) FieldUtils.readField(target, field, true);
      Optional<User> user = userRepository.findOneByIdentifier(name);
      if (user.isPresent() && (id == null || !user.get().getId().equals(id))) {
        return false;
      }
      return true;
    } catch (final Exception ignore) {
    }
    return false;
  }

}
