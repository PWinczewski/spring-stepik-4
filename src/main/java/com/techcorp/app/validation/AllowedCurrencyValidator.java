package com.techcorp.app.validation;

import com.techcorp.app.service.EmployeeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;



public class AllowedCurrencyValidator implements ConstraintValidator<AllowedCurrency, String> {
    @Autowired
    private EmployeeService employeeService;

    public void initialize(AllowedCurrency constraint) {
    }

    private String[] allowedCurrencies = new String[]{"EUR", "USD", "GBP", "JPY"};

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(allowedCurrencies).contains(currency);
    }
}