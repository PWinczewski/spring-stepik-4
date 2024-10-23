package com.techcorp.app.service;

import com.opencsv.CSVReader;
import com.techcorp.app.domain.Person;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private List<Person> employees = new ArrayList<>();

    public EmployeeService() {
        loadEmployeesFromCSV();
    }

    public void loadEmployeesFromCSV() {
        boolean first = true;
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/MOCK_DATA.csv"))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (!first) employees.add(new Person(line[0], line[1], line[2], line[5]));
                else first = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayAllEmployees() {
        System.out.println("Employees from CSV:");
        employees.forEach(System.out::println);
    }

    public List<Person> filterByCompany(String companyName) {
        return employees.stream()
                .filter(employee -> employee.getCompany().equalsIgnoreCase(companyName))
                .collect(Collectors.toList());
    }

    public void displaySortedEmployees() {
        employees.stream()
                .sorted(Comparator.comparing(Person::getLastName))
                .forEach(System.out::println);
    }
}
