package com.blossom_project.core.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueIdentifierValidator.class)
public @interface UniqueIdentifier {
  String message() default "{users.user.validation.identifier.UniqueIdentifier.message}";

  Class<?>[] groups() default {};

  String idField() default "";

  String field() default "identifier";

  Class<? extends Payload>[] payload() default {};
}
