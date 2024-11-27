package com.techcorp.app.validation;

import com.techcorp.app.domain.Person;
import com.techcorp.app.service.EmployeeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Person> {

    private final EmployeeService employeeService;

    UniqueEmailValidator(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext context) {
        if (person.getId() != null) {
            var employee = employeeService.getEmployeeById(person.getId().toString()).orElse(null);

            if (employee!=null) {
                var isSameAsPreviousEmail = employee.getEmail().equalsIgnoreCase(person.getEmail());

                if (isSameAsPreviousEmail) {
                    return true;
                }
            }
        }

        var isEmailUnique =
                employeeService.getEmployees().stream().noneMatch(p -> p.getEmail().equalsIgnoreCase(person.getEmail()));
        if (!isEmailUnique) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}