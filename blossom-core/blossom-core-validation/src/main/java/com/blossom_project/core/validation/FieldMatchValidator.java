package com.blossom_project.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
  private String value;
  private String confirmation;

  @Override public void initialize(final FieldMatch constraintAnnotation) {
    value = constraintAnnotation.value();
    confirmation = constraintAnnotation.confirmation();
  }

  @Override public boolean isValid(final Object target, final ConstraintValidatorContext context) {
    try {
      String valueValue= extractField(target, value);

      String confirmationValue = extractField(target, confirmation);

      return valueValue== null && confirmationValue== null || valueValue!= null && valueValue.equals(confirmationValue);
    } catch (final Exception ignore) {
    } return true;
  }

  private String extractField(final Object target, String fieldValue)
    throws NoSuchFieldException, IllegalAccessException {
    Field field = target.getClass().getDeclaredField(fieldValue);
    boolean accessible = field.isAccessible();
    field.setAccessible(true);
    String str = (String) field.get(target);
    field.setAccessible(accessible);
    return str;
  }
}
