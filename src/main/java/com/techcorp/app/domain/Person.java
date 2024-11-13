package com.techcorp.app.domain;
import java.util.UUID;

import com.techcorp.app.validation.AllowedCurrency;
import com.techcorp.app.validation.UniqueEmail;
import jakarta.validation.constraints.*;

public class Person {
    private UUID id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "[a-zA-Z-]+", message = "First name must contain only letters and '-' symbols")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "[a-zA-Z-]+", message = "Last name must contain only letters and '-' symbols")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email")
    @UniqueEmail
    private String email;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Currency is required")
    @AllowedCurrency
    private String currency;

    @NotNull(message = "Salary is required")
    @Positive
    @DecimalMax(value = "1000000.0", message = "Salary must not exceed 1,000,000")
    private double salary;

    public Person(){
        this.id = UUID.randomUUID();
    }

    public Person(String firstName, String lastName, String email, double salary, String currency, String country) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.currency = currency;
        this.salary = salary;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Person [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", company=" + country + "]";
    }
}

