package fr.mgargadennec.blossom.core.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueIdentifierValidator.class)
public @interface UniqueIdentifier {
  String message() default "{users.user.validation.identifier.UniqueIdentifier.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
