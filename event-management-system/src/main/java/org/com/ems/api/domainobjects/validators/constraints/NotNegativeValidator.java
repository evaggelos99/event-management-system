package org.com.ems.api.domainobjects.validators.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator responsible for not allowing number fields to be negative
 *
 * @author Evangelos Georgiou
 *
 */
public class NotNegativeValidator implements ConstraintValidator<NotNegative, Number> {

    /**
     * Returns {@link Boolean#TRUE} if the value is not negative else
     * {@link Boolean#FALSE}
     */
    @Override
    public boolean isValid(final Number value,
			   final ConstraintValidatorContext context) {

	return (null != value && value.intValue() >= 0);

    }

}
