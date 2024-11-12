package com.techcorp.app.service;

import com.techcorp.app.domain.Company;
import com.techcorp.app.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
}
