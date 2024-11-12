package com.techcorp.app.domain;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.*;
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

                if (!first) employees.add(new Person(line[0], line[1], line[2], Double.parseDouble(line[3]), line[4], line[5]));
                else first = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getSalarySummary() {
        Map<String, Double> summary = employees.stream()
                .collect(Collectors.groupingBy(
                        Person::getCurrency,
                        Collectors.summingDouble(Person::getSalary)
                ));
        Map<String, String> formattedSalarySummary = new HashMap<>(Map.of());
        for (Map.Entry<String, Double> entry : summary.entrySet()) {
            formattedSalarySummary.put(entry.getKey(), String.format("%,.2f", entry.getValue()));
        }
        return formattedSalarySummary;
    }

    public List<Person> getEmployees() {
        return employees;
    }

    public Optional<Person> getEmployeeById(String id) {
        return employees.stream()
                .filter(employee -> Objects.equals(employee.getId().toString(), id))
                .findFirst();
    }

    public Optional<Person> getEmployeeByEmail(String email) {
        return employees.stream()
                .filter(employee -> Objects.equals(employee.getEmail(), email))
                .findFirst();
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

    public List<String> getCountries() {
        return employees.stream()
                .map(Person::getCountry).distinct().collect(Collectors.toList());
    }
}

