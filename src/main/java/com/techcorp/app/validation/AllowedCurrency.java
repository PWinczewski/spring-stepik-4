package com.techcorp.app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedCurrencyValidator.class)
public @interface AllowedCurrency {

    String message() default "Currency must be one of the following: EUR, USD, GBP, JPY";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}