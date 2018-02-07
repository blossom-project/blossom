package com.blossom_project.core.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validation annotation to validate that 2 fields have the same value.
 * An array of fields and their matching confirmation fields can be supplied.
 *
 * Example, compare 1 pair of fields:
 * <pre>
 * {@code @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")}
 * </pre>
 * Example, compare more than 1 pair of fields:
 * <pre>
 *  {@code @FieldMatch.List({ @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"), @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")})}
 * </pre>
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
public @interface FieldMatch
{
    String message() default "{constraints.fieldmatch}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();

    String confirmation();

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @see FieldMatch
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
            @interface List
    {
        FieldMatch[] value();
    }
}
