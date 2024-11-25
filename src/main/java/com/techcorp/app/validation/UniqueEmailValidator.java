package com.techcorp.app.validation;

import com.techcorp.app.service.EmployeeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private EmployeeService employeeService;

    public void initialize(UniqueEmail constraint) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null || email.isBlank()) {
            return true;
        }

        String currentEmployeeId = (String) RequestContextHolder.currentRequestAttributes()
                .getAttribute("currentEmployeeId", RequestAttributes.SCOPE_REQUEST);

        return employeeService.getEmployees().stream()
                .filter(person -> !person.getId().toString().equals(currentEmployeeId))
                .noneMatch(person -> person.getEmail().equalsIgnoreCase(email));
    }
}