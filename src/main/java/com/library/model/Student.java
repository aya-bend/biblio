package com.library.model;

public class Student {
    private int id;
    private String name;
    private String email;

    // Constructeur par défaut
    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructeur complet
    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    public Student(String name) {
        this.name = name;
    }

    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }


    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
