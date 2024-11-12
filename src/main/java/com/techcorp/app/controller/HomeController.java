package com.techcorp.app.controller;

import com.techcorp.app.domain.Person;
import com.techcorp.app.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/employees")
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

        System.out.println( employeeService.getSalarySummary());
        return "employees/list";
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