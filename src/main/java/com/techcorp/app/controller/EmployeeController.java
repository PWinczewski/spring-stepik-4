package com.techcorp.app.controller;

import com.techcorp.app.domain.Person;
import com.techcorp.app.service.EmployeeService;
import com.techcorp.app.service.FileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;
    private final FileService fileService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, FileService fileService) {
        this.employeeService = employeeService;
        this.fileService = fileService;
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
    public ResponseEntity<Person> addEmployee(@RequestBody Person newEmployee,
                                              @RequestParam("image") MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            String fileName = fileService.saveFile(image);
            newEmployee.setImagePath(fileName);
        }
        employeeService.getEmployees().add(newEmployee);
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

    @GetMapping("/image/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        Resource file = fileService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
