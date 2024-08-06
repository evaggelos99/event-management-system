package com.github.evaggelos99.ems.common.api.domainobjects.validators.constraints;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.NotNegativeValidator;

class NotNegativeValidatorTest {

    NotNegativeValidator validator;

    @BeforeEach
    void setUp() {

	this.validator = new NotNegativeValidator();

    }

    @Test
    void isValid_valueIsNotNullAndIsValid_doesNotThrow_expectTrue() {

	Assertions.assertTrue(Assertions.assertDoesNotThrow(() -> this.validator.isValid(5, null)));

    }

    @Test
    void isValid_valueIsNotNullAndIsNegative_doesNotThrow_expectFalse() {

	Assertions.assertFalse(Assertions.assertDoesNotThrow(() -> this.validator.isValid(-5, null)));

    }

    @Test
    void isValid_valueIsNull_doesNotThrow_expectFalse() {

	Assertions.assertFalse(Assertions.assertDoesNotThrow(() -> this.validator.isValid(null, null)));

    }

}
