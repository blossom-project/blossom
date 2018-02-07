package com.blossom_project.core.role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueRoleNameValidator.class)
public @interface UniqueRoleName {
  String message() default "{roles.role.validation.name.UniqueRoleName.message}";

  String idField() default "";

  String field() default "name";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
