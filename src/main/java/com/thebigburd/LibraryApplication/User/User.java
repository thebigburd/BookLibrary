package com.thebigburd.LibraryApplication.User;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="people")
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "USER_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    private String email;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private Integer age;

    public User() {
    }

    public User(String email, String name, String surname, LocalDate dateOfBirth, Integer age) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
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
        return age;
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
