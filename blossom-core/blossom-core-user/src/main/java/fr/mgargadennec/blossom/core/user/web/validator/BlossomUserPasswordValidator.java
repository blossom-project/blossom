package fr.mgargadennec.blossom.core.user.web.validator;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.mgargadennec.blossom.core.user.web.resources.BlossomUserPasswordResource;

public class BlossomUserPasswordValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return BlossomUserPasswordResource.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    BlossomUserPasswordResource boUserPasswordResource = (BlossomUserPasswordResource) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldPassword", "field.required", new Object[]{},
        "oldPassword.required");

    validateNewPassword(errors, boUserPasswordResource.getNewPasswords());
  }

  private void validateNewPassword(Errors errors, String[] newPasswords) {
    if (newPasswords != null && newPasswords.length == 2) {
      if (!newPasswords[0].equals(newPasswords[1])) {
        errors.rejectValue("newPasswords", "field.invalid", new Object[]{}, "newPasswords.invalid");
      }
      Pattern letter = Pattern.compile("[a-zA-z]");
      Pattern digit = Pattern.compile("[0-9]");
      Pattern length = Pattern.compile(".{8,}");

      String password = newPasswords[0];

      if (!letter.matcher(password).find() || !digit.matcher(password).find() || !length.matcher(password).matches()) {
        errors.rejectValue("newPasswords", "field.invalid", new Object[]{}, "newPasswords.format_rules");
      }
    } else {
      errors.rejectValue("newPasswords", "field.required", new Object[]{}, "newPasswords.required");
    }
  }
}
