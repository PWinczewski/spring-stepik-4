package com.techcorp.app.controller;

import com.techcorp.app.domain.Person;
import com.techcorp.app.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Person> getAllEmployees() {
        return employeeService.getCountry().getEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getEmployeeById(@PathVariable String id) {
        List<Person> employees = employeeService.getCountry().getEmployees();
        for (Person employee : employees) {
            if (Objects.equals(employee.getId().toString(), id)) {
                return new ResponseEntity<>(employee, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Person> addEmployee(@RequestBody Person newEmployee) {
        employeeService.getCountry().getEmployees().add(newEmployee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updateEmployee(@PathVariable String id, @RequestBody Person updatedEmployee) {
        List<Person> employees = employeeService.getCountry().getEmployees();
        for (int i = 0; i < employees.size(); i++) {
            if (Objects.equals(employees.get(i).getId().toString(), id)) {
                employees.set(i, updatedEmployee);
                return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        List<Person> employees = employeeService.getCountry().getEmployees();
        for (int i = 0; i < employees.size(); i++) {
            if (Objects.equals(employees.get(i).getId().toString(), id)) {
                employees.remove(i);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
