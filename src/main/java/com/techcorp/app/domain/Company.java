package com.techcorp.app.domain;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Company {
    private List<Person> employees = new ArrayList<>();

    public Company(String csvFilePath) {
        loadEmployeesFromCSV(csvFilePath);
    }

    public void loadEmployeesFromCSV(String csvFilePath) {
        boolean first = true;
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] line;

            while ((line = reader.readNext()) != null) {

                if (!first) employees.add(new Person(line[0], line[1], line[2], line[5]));
                else first = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Person> getEmployees() {
        return employees;
    }

    public void displayAllEmployees() {
        System.out.println("Employees from CSV:");
        employees.forEach(System.out::println);
    }

    public List<Person> filterByCountry(String countryName) {
        return employees.stream()
                .filter(employee -> employee.getCountry().equalsIgnoreCase(countryName))
                .collect(Collectors.toList());
    }

    public void displaySortedEmployees() {
        employees.stream()
                .sorted(Comparator.comparing(Person::getLastName))
                .forEach(System.out::println);
    }
}

