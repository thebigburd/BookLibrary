package com.thebigburd.LibraryApplication.Entity;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name="people")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String name;
    private String surname;

    // Date of Birth uses YYYY-MM-DD format
    private LocalDate dateOfBirth;
    @Transient
    private Integer age;

    public User() {
    }

    public User(String email, String name, String surname, LocalDate dateOfBirth) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
    }

    public User(Long id, String email, String name, String surname, LocalDate dateOfBirth) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {

        return Period.between(getDateOfBirth(), LocalDate.now()).getYears();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                '}';
    }


}
