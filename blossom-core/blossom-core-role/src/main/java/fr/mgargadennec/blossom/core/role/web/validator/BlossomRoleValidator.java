package fr.mgargadennec.blossom.core.role.web.validator;

import org.elasticsearch.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.mgargadennec.blossom.core.role.web.resource.BlossomRoleResourceState;

public class BlossomRoleValidator implements Validator {

  private final static int MAX_SIZE_NAME = 50;

  @Override
  public boolean supports(Class<?> clazz) {
    return BlossomRoleResourceState.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", new Object[]{}, "name.required");

    BlossomRoleResourceState boRoleResource = (BlossomRoleResourceState) target;
    if (!Strings.isNullOrEmpty(boRoleResource.getName()) && boRoleResource.getName().length() > MAX_SIZE_NAME) {
      errors.rejectValue("name", "field.invalid", new Object[]{boRoleResource.getName().length(), MAX_SIZE_NAME},
          "The name size is expected to be less than {0}");
    }
  }
}
