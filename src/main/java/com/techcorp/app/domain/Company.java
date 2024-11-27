package com.techcorp.app.domain;

import com.opencsv.CSVReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Company {
    private List<Person> employees = new ArrayList<>();

    public Company() {}

    public Company(String csvFilePath) {
        loadEmployeesFromCSV(csvFilePath);
    }

    public void loadEmployeesFromCSV(String csvFilePath) {
        boolean first = true;
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] line;

            while ((line = reader.readNext()) != null) {

                if (!first) employees.add(new Person(line[0], line[1], line[2], Double.parseDouble(line[3]), line[4], line[5], ""));
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
        return List.of(
                "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia",
                "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
                "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
                "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia",
                "Comoros", "Congo", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Democratic Republic of the Congo",
                "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
                "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany",
                "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary",
                "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan",
                "Kenya", "Kiribati", "Korea, North", "Korea, South", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho",
                "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali",
                "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro",
                "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria",
                "North Macedonia", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines",
                "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines",
                "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore",
                "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname",
                "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago",
                "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States",
                "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
        );
    }

    public void updateEmployee(Person updatedEmployee) {
        for (int i = 0; i < employees.size(); i++) {
            if (Objects.equals(employees.get(i).getId(), updatedEmployee.getId())) {
                employees.set(i, updatedEmployee);
                return;
            }
        }
    }

    public void addEmployee(Person newEmployee) {
        employees.add(newEmployee);
    }

    public boolean isEmailExists(String email, String currentEmail) {
        if (email.equals(currentEmail)) {
            return false; // No conflict if it's the same email
        }
        return getEmployeeByEmail(email).orElse(null) != null;
    }
}

