package com.blossomproject.core.group;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.reflect.FieldUtils;

class UniqueGroupNameValidator implements ConstraintValidator<UniqueGroupName, Object> {

  private GroupRepository groupRepository;
  private String field;
  private String idField;

  public UniqueGroupNameValidator(GroupRepository groupRepository) {
    Preconditions.checkNotNull(groupRepository);
    this.groupRepository = groupRepository;
  }

  public void initialize(UniqueGroupName constraint) {
    this.field = constraint.field();
    this.idField = constraint.idField();
  }

  public boolean isValid(final Object target, ConstraintValidatorContext context) {
    try {
      Long id =
        Strings.isNullOrEmpty(idField) ? null : (Long) FieldUtils.readField(target, idField, true);
      String name = (String) FieldUtils.readField(target, field, true);
      Optional<Group> group = groupRepository.findOneByName(name);
      if (group.isPresent() && (id == null || !group.get().getId().equals(id))) {
        return false;
      }
      return true;
    } catch (final Exception ignore) {
    }
    return false;
  }
}
