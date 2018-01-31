package fr.blossom.core.role;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.reflect.FieldUtils;

class UniqueRoleNameValidator implements ConstraintValidator<UniqueRoleName, Object> {

  private RoleRepository roleRepository;
  private String field;
  private String idField;

  public UniqueRoleNameValidator(RoleRepository roleRepository) {
    Preconditions.checkNotNull(roleRepository);
    this.roleRepository = roleRepository;
  }

  public void initialize(UniqueRoleName constraint) {
    this.field = constraint.field();
    this.idField = constraint.idField();
  }

  public boolean isValid(final Object target, ConstraintValidatorContext context) {
    try {
      Long id =
        Strings.isNullOrEmpty(idField) ? null : (Long) FieldUtils.readField(target, idField, true);
      String name = (String) FieldUtils.readField(target, field, true);
      Optional<Role> group = roleRepository.findOneByName(name);
      if (group.isPresent() && (id == null || !group.get().getId().equals(id))) {
        return false;
      }
      return true;
    } catch (final Exception ignore) {
    }
    return false;
  }
}
