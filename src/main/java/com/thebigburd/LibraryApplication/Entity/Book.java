package com.thebigburd.LibraryApplication.Entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private int publishYear;

    @Transient
    private boolean borrowed;

    public Book() {
    }

    public Book(String name, String description, int publishYear, boolean borrowed) {
        this.name = name;
        this.description = description;
        this.publishYear = publishYear;
        this.borrowed = false;
    }

    public Book(long id, String name, String description, int publishYear, boolean borrowed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.publishYear = publishYear;
        this.borrowed = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", publishYear=" + publishYear +
                ", borrowed=" + borrowed +
                '}';
    }
}
