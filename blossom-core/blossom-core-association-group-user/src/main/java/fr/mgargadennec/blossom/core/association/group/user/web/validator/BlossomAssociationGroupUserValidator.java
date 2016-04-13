package fr.mgargadennec.blossom.core.association.group.user.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.mgargadennec.blossom.core.association.group.user.web.resource.BlossomAssociationGroupUserResourceState;

public class BlossomAssociationGroupUserValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return BlossomAssociationGroupUserResourceState.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "groupId", "field.required", new Object[]{}, "id.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "field.required", new Object[]{}, "id.required");

  }
}
