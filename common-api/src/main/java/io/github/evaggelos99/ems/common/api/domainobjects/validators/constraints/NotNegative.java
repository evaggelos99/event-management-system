package io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints;

import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.NotNegative.List;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom annotation for numeric fields that their value should not be negative
 *
 * @author Evangelos Georgiou
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
@Documented
@Constraint(validatedBy = {NotNegativeValidator.class})
public @interface NotNegative {

    String message() default "Cannot be negative";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@link NotNegative} constraints on the same element.
     *
     * @see NotNegative
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        NotNegative[] value();
    }
}
