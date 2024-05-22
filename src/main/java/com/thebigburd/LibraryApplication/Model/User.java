package com.thebigburd.LibraryApplication.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.thebigburd.LibraryApplication.Model.enumeration.UserRole;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "people")
public class User {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	private String email;
	private String name;
	private String surname;
	private String password;
	private String address;
	private String phone;
	private UserRole role;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;
	@Transient
	private int age;
	private int borrowCount;
	private int borrowLimit;

	// No Args Constructor (Hibernate)
	public User() {
	}

	// Default Borrow Count/Limit Constructor
	public User(Long id, String email, String name, String surname, String password, String address, String phone, UserRole role, LocalDate dateOfBirth) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.address = address;
		this.phone = phone;
		this.role = role;
		this.dateOfBirth = dateOfBirth;
		setAge();
		borrowCount = 0;
		borrowLimit = 3;
	}
	// No Age Constructor
	public User(Long id, String email, String name, String surname, String password, String address, String phone, UserRole role, LocalDate dateOfBirth, int borrowCount, int borrowLimit) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.address = address;
		this.phone = phone;
		this.role = role;
		this.dateOfBirth = dateOfBirth;
		setAge();
		this.borrowCount = borrowCount;
		this.borrowLimit = borrowLimit;
	}

	// All Arg Constructor
	public User(Long id, String email, String name, String surname, String password, String address, String phone, UserRole role, LocalDate dateOfBirth, int age, int borrowCount, int borrowLimit) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.address = address;
		this.phone = phone;
		this.role = role;
		this.dateOfBirth = dateOfBirth;
		this.age = age;
		this.borrowCount = borrowCount;
		this.borrowLimit = borrowLimit;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getAge() {
		return age;
	}

	public void setAge() {
		age = Period.between(getDateOfBirth(), LocalDate.now()).getYears();
	}

	public int getBorrowCount() {
		return borrowCount;
	}

	public void setBorrowCount(int borrowCount) {
		this.borrowCount = borrowCount;
	}

	public int getBorrowLimit() {
		return borrowLimit;
	}

	public void setBorrowLimit(int borrowLimit) {
		this.borrowLimit = borrowLimit;
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", email='" + email + '\'' +
			", name='" + name + '\'' +
			", surname='" + surname + '\'' +
			", password='" + password + '\'' +
			", address='" + address + '\'' +
			", phone='" + phone + '\'' +
			", role=" + role +
			", dateOfBirth=" + dateOfBirth +
			", age=" + age +
			", borrowCount=" + borrowCount +
			", borrowLimit=" + borrowLimit +
			'}';
	}
}
