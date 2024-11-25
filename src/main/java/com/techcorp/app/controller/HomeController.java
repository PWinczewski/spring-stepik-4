package com.techcorp.app.controller;

import com.techcorp.app.domain.Person;
import com.techcorp.app.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    private final EmployeeService employeeService;

    @Autowired
    public HomeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getEmployees(@RequestParam(value = "country", required = false) String country, Model model) {

        List<Person> employees = (country != null && !country.isEmpty()) ?
                employeeService.getEmployeesByCountry(country) :
                employeeService.getEmployees();

        model.addAttribute("employees", employees);
        model.addAttribute("salarySummary", employeeService.getSalarySummary());
        model.addAttribute("employeeCount", employeeService.getEmployees().size());
        model.addAttribute("countries", employeeService.getCountries());
        model.addAttribute("selectedCountry", country);

        return "employees/list";
    }

    @GetMapping("/create")
    public String createEmployeeForm(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("employeeCount", employeeService.getEmployees().size());
        model.addAttribute("countries", employeeService.getCountries());

        return "employees/personForm";
    }

    @PostMapping("/create")
    public String processCreateEmployeeForm(@Valid Person person, Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute("employeeCount", employeeService.getEmployees().size());
            model.addAttribute("countries", employeeService.getCountries());
            return "employees/personForm";
        }
        log.info("Person created: {}", person);
        employeeService.getEmployees().add(person);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateEmployeeForm(@PathVariable String id, Model model) {

        Person employee = employeeService.getEmployeeById(id).orElse(null);
        if (employee != null) {
            model.addAttribute("employee", employee);
        } else {
            model.addAttribute("error", "Employee not found.");
        }

        model.addAttribute("employeeCount", employeeService.getEmployees().size());
        model.addAttribute("countries", employeeService.getCountries());

        return "employees/personUpdateForm";
    }

    @PostMapping("/update/{id}")
    public String processUpdateEmployeeForm(@PathVariable String id, @Valid Person employee, Errors errors, Model model) {
        RequestContextHolder.currentRequestAttributes().setAttribute("currentEmployeeId", id, RequestAttributes.SCOPE_REQUEST);

        if (errors.hasErrors()) {
            model.addAttribute("employeeCount", employeeService.getEmployees().size());
            model.addAttribute("countries", employeeService.getCountries());
            return "employees/personUpdateForm";
        }

        Person existingEmployee = employeeService.getEmployeeById(id).orElse(null);
        if (existingEmployee != null) {
            existingEmployee.setFirstName(employee.getFirstName());
            existingEmployee.setLastName(employee.getLastName());
            existingEmployee.setCountry(employee.getCountry());
            existingEmployee.setCurrency(employee.getCurrency());
            existingEmployee.setSalary(employee.getSalary());
            if (!existingEmployee.getEmail().equals(employee.getEmail())) {
                existingEmployee.setEmail(employee.getEmail());
            }
        }
        return "redirect:/";
    }

    @GetMapping("/details/{email}")
    public String employeeDetails(@PathVariable String email, Model model) {
        Person employee = employeeService.getEmployeeByEmail(email).orElse(null);

        if (employee != null) {
            model.addAttribute("employee", employee);
        } else {
            model.addAttribute("error", "Employee not found.");
        }

        model.addAttribute("employeeCount", employeeService.getEmployees().size());


        return "employees/details";
    }
}