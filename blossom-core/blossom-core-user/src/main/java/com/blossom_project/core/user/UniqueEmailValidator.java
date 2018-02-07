package com.blossom_project.core.user;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.reflect.FieldUtils;

class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Object> {

  private UserRepository userRepository;
  private String field;
  private String idField;

  public UniqueEmailValidator(UserRepository userRepository) {
    Preconditions.checkNotNull(userRepository);
    this.userRepository = userRepository;
  }

  public void initialize(UniqueEmail constraint) {
    this.field = constraint.field();
    this.idField = constraint.idField();
  }

  public boolean isValid(Object target, ConstraintValidatorContext context) {
    try {
      Long id = Strings.isNullOrEmpty(idField) ? null : (Long) FieldUtils.readField(target, idField, true);
      String email = (String) FieldUtils.readField(target, field, true);
      Optional<User> user = userRepository.findOneByEmail(email);
      if (user.isPresent() && (id == null || !user.get().getId().equals(id))) {
        return false;
      }
      return true;
    } catch (final Exception ignore) {
    }
    return false;
  }

}
