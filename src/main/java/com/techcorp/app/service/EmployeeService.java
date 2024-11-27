package com.techcorp.app.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.techcorp.app.domain.Company;
import com.techcorp.app.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    @Qualifier("company")
    Company company;

    public Company getCountry(){
        return company;
    }

    public List<Person> getEmployees(){
        return company.getEmployees();
    }

    public Optional<Person> getEmployeeById(String id){
        return company.getEmployeeById(id);
    }

    public Optional<Person> getEmployeeByEmail(String email){
        return company.getEmployeeByEmail(email);
    }

    public Map<String, String> getSalarySummary() {
        return company.getSalarySummary();
    }

    public List<String> getCountries(){
        return company.getCountries();
    }

    public void getSortedEmployees(){
        company.displaySortedEmployees();
    }

    public List<Person> getEmployeesByCountry(String countryName){
        return company.filterByCountry(countryName);
    }

    public void addEmployee(Person newEmployee) { company.addEmployee(newEmployee); }

    public void updateEmployee(Person updatedEmployee) { company.updateEmployee(updatedEmployee); }



    public void importEmployeesFromCSV(MultipartFile file) throws IOException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            boolean firstLine = true;

            while ((line = reader.readNext()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip header line
                    continue;
                }

                // Extract data from the CSV line
                String firstName = line[0];
                String lastName = line[1];
                String email = line[2];
                double salary = Double.parseDouble(line[3]);
                String currency = line[4];
                String country = line[5];

                Person person = new Person(firstName, lastName, email, salary, currency, country, null);

                addEmployee(person); // Add valid employee to the list
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
