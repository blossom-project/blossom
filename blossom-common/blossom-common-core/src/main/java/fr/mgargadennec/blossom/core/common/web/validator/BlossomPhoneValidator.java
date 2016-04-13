package fr.mgargadennec.blossom.core.common.web.validator;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.google.common.base.Strings;

public class BlossomPhoneValidator implements Validator {
  private Pattern pattern;
  private Pattern patternMobile;

  private static final String PHONE_PATTERN = "^((\\+|00)33\\s?|0)[1-5](\\s?\\d{2}){4}$";
  private static final String MOBILE_PHONE_PATTERN = "^((\\+|00)33\\s?|0)[679](\\s?\\d{2}){4}$";

  public BlossomPhoneValidator() {
    pattern = Pattern.compile(PHONE_PATTERN);
    patternMobile = Pattern.compile(MOBILE_PHONE_PATTERN);
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return String.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    String phone = (String) target;
    if (Strings.isNullOrEmpty(phone) || (!pattern.matcher(phone).matches() && !patternMobile.matcher(phone).matches())) {
      errors.rejectValue(null, "field.invalid", new Object[]{}, "phone.invalid");
    }
  }

}
