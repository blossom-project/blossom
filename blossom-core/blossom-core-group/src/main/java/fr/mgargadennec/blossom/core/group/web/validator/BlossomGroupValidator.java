package fr.mgargadennec.blossom.core.group.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;

import fr.mgargadennec.blossom.core.group.web.resource.BlossomGroupResourceState;

public class BlossomGroupValidator implements Validator {

  private final static int MAX_SIZE_NAME = 50;

  @Override
  public boolean supports(Class<?> clazz) {
    return BlossomGroupResourceState.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", new Object[]{}, "name.required");

    // Validation du name
    BlossomGroupResourceState boTemplateResourceState = (BlossomGroupResourceState) target;
    String name = boTemplateResourceState.getName();
    if (!Strings.isNullOrEmpty(name) && name.length() > MAX_SIZE_NAME) {
      errors.rejectValue("name", "name.size", new Object[]{name.length(), MAX_SIZE_NAME},
          "The name size is expected to be less than {0}");
    }
  }
}
