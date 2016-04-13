package fr.mgargadennec.blossom.core.common.web.validator;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;

public class BlossomEmailValidator implements Validator {
  private Pattern pattern;

  private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  public BlossomEmailValidator() {
    pattern = Pattern.compile(EMAIL_PATTERN);
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return String.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    String email = (String) target;
    if (Strings.isNullOrEmpty(email) || !pattern.matcher(email).matches()) {
      errors.rejectValue(null, "field.invalid", new Object[]{}, "email.invalid");
    }
  }

}
