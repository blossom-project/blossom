package com.blossomproject.core.group;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueGroupNameValidator.class)
public @interface UniqueGroupName {
  String message() default "{groups.group.validation.name.UniqueGroupName.message}";

  String idField() default "";

  String field() default "name";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
