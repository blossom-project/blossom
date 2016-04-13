package fr.mgargadennec.blossom.core.association.user.role.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.mgargadennec.blossom.core.association.user.role.web.resource.BlossomRightResourceState;

public class BlossomRightValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return BlossomRightResourceState.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "permissions", "field.required", new Object[]{},
        "permissions.required");
    ValidationUtils
        .rejectIfEmptyOrWhitespace(errors, "resource", "field.required", new Object[]{}, "resource.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roleId", "field.required", new Object[]{}, "roleId.required");
  }
}
