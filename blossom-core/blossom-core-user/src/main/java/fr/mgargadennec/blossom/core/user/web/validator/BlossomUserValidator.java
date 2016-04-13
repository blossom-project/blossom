package fr.mgargadennec.blossom.core.user.web.validator;

import org.elasticsearch.common.base.Strings;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import fr.mgargadennec.blossom.core.common.web.validator.BlossomEmailValidator;
import fr.mgargadennec.blossom.core.common.web.validator.BlossomPhoneValidator;
import fr.mgargadennec.blossom.core.user.web.resources.BlossomUserResourceState;

public class BlossomUserValidator implements Validator {
  private final static int MAX_SIZE_FIRSTNAME = 30;
  private final static int MAX_SIZE_LASTNAME = 30;
  private final static int MAX_SIZE_LOGIN = 31;
  private final static int MAX_SIZE_FUNCTION = 256;
  private final static int MAX_SIZE_EMAIL = 80;

  private BlossomPhoneValidator phoneValidator;
  private BlossomEmailValidator emailValidator;

  public BlossomUserValidator(BlossomPhoneValidator phoneValidator, BlossomEmailValidator emailValidator) {
    this.phoneValidator = phoneValidator;
    this.emailValidator = emailValidator;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return BlossomUserResourceState.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
	  BlossomUserResourceState blossomUserResource = (BlossomUserResourceState) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "field.required", new Object[]{},
        "firstname.required");

    ValidationUtils
        .rejectIfEmptyOrWhitespace(errors, "lastname", "field.required", new Object[]{}, "lastname.required");

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required", new Object[]{}, "email.required");
    errors.pushNestedPath("email");
    emailValidator.validate(blossomUserResource.getEmail(), errors);
    errors.popNestedPath();

    if (!Strings.isNullOrEmpty(blossomUserResource.getPhone())) {
      errors.pushNestedPath("phone");
      phoneValidator.validate(blossomUserResource.getPhone(), errors);
      errors.popNestedPath();
    }

    // Taille des champs
    String firstname = blossomUserResource.getFirstname();
    if (!Strings.isNullOrEmpty(firstname) && firstname.length() > MAX_SIZE_FIRSTNAME) {
      errors.rejectValue("firstname", "firstname.size", new Object[]{firstname.length(), MAX_SIZE_FIRSTNAME},
          "The firstname size is expected to be less than {0}");
    }

    String lastname = blossomUserResource.getLastname();
    if (!Strings.isNullOrEmpty(lastname) && lastname.length() > MAX_SIZE_LASTNAME) {
      errors.rejectValue("lastname", "lastname.size", new Object[]{lastname.length(), MAX_SIZE_LASTNAME},
          "The lastname size is expected to be less than {0}");
    }
    String email = blossomUserResource.getEmail();
    if (!Strings.isNullOrEmpty(email) && email.length() > MAX_SIZE_EMAIL) {
      errors.rejectValue("email", "email.size", new Object[]{email.length(), MAX_SIZE_EMAIL},
          "The email size is expected to be less than {0}");
    }
    String function = blossomUserResource.getFunction();
    if (!Strings.isNullOrEmpty(function) && function.length() > MAX_SIZE_FUNCTION) {
      errors.rejectValue("function", "function.size", new Object[]{function.length(), MAX_SIZE_FUNCTION},
          "The function size is expected to be less than {0}");
    }
    String login = blossomUserResource.getLogin();
    if (!Strings.isNullOrEmpty(login) && login.length() > MAX_SIZE_LOGIN) {
      errors.rejectValue("login", "login.size", new Object[]{login.length(), MAX_SIZE_LOGIN},
          "The login size is expected to be less than {0}");
    }
  }
}
