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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    private final EmployeeService employeeService;
    private final FileService fileService;

    @Autowired
    public HomeController(EmployeeService employeeService, FileService fileService) {
        this.employeeService = employeeService;
        this.fileService = fileService;
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
    public String processCreateEmployeeForm(@Valid Person employee, Errors errors, Model model, @RequestParam("image") MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            String fileName = fileService.saveFile(image);
            employee.setImagePath(fileName);
        }

        if(errors.hasErrors()){
            model.addAttribute("employeeCount", employeeService.getEmployees().size());
            model.addAttribute("countries", employeeService.getCountries());
            return "employees/personForm";
        }
        log.info("Person created: {}", employee);
        employeeService.addEmployee(employee);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateEmployeeForm(@PathVariable String id, Model model) {

        Person person = employeeService.getEmployeeById(id).orElse(null);
        if (person != null) {
            model.addAttribute("person", person);
        } else {

            model.addAttribute("error", "Employee not found.");
        }

        model.addAttribute("employeeCount", employeeService.getEmployees().size());
        model.addAttribute("countries", employeeService.getCountries());

        return "employees/personUpdateForm";
    }

    @PostMapping("/update/{id}")
    public String processUpdateEmployeeForm(@PathVariable String id, @Valid Person person, Errors errors, Model model, @RequestParam("image") MultipartFile image) throws IOException {
        RequestContextHolder.currentRequestAttributes().setAttribute("currentEmployeeId", id, RequestAttributes.SCOPE_REQUEST);

        if (errors.hasErrors()) {
            model.addAttribute("employeeCount", employeeService.getEmployees().size());
            model.addAttribute("countries", employeeService.getCountries());
            model.addAttribute("person", person);
            return "employees/personUpdateForm";
        }

        Person existingEmployee = employeeService.getEmployeeById(id).orElse(null);
        if (existingEmployee != null) {
            existingEmployee.setFirstName(person.getFirstName());
            existingEmployee.setLastName(person.getLastName());
            existingEmployee.setCountry(person.getCountry());
            existingEmployee.setCurrency(person.getCurrency());
            existingEmployee.setSalary(person.getSalary());
            existingEmployee.setEmail(person.getEmail());

            if (!image.isEmpty()) {
                String fileName = fileService.saveFile(image);
                if (existingEmployee.getImagePath() != null) {
                    fileService.deleteFile(existingEmployee.getImagePath());
                }
                existingEmployee.setImagePath(fileName);
            }

        }
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String processDeleteEmployee(@PathVariable String id, Model model, RedirectAttributes redirectAttributes){
        List<Person> employees = employeeService.getCountry().getEmployees();
        for (int i = 0; i < employees.size(); i++) {
            if (Objects.equals(employees.get(i).getId().toString(), id)) {
                employees.remove(i);
                redirectAttributes.addFlashAttribute("message", "Employee deleted successfully!");
                return "redirect:/";
            }
        }
        redirectAttributes.addFlashAttribute("error", "Employee not found!");
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

    @GetMapping("/image/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        Resource file = fileService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/import")
    public String importEmployeesFromCSV(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            employeeService.importEmployeesFromCSV(file);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "File processing error: " + e.getMessage());
        }
        return "redirect:/";
    }
}