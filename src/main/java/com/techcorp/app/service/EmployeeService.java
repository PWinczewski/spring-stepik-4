package com.techcorp.app.service;

import com.techcorp.app.domain.Company;
import com.techcorp.app.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    @Qualifier("company")
    Company company;

    public Company getCountry(){
        return company;
    }

    public void getEmployees(){
        company.displayAllEmployees();
    }
    public void getSortedEmployees(){
        company.displaySortedEmployees();
    }
    public List<Person> getEmployeesByCountry(String countryName){
        return company.filterByCountry(countryName);
    }
}
