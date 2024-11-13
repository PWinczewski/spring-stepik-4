package com.techcorp.app.validation;

import com.techcorp.app.service.EmployeeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private EmployeeService employeeService;

    public void initialize(UniqueEmail constraint) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return employeeService.getEmployeeByEmail(email).isEmpty();
    }
}