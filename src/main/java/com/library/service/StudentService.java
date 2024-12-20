package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }
    

    // Ajouter un étudiant
    public void addStudent(Student student) {
        studentDAO.addStudent(student);
    }

    // Afficher tous les étudiants
    public void displayStudents() {
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            System.out.println("Nom: " + student.getName() + " | email: " + student.getEmail());
        }
    }

    // Trouver un étudiant par ID
    public Student findStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    public void updateStudent(Student studentupdate) {
        studentDAO.updateStudent(studentupdate);
    }

    public void deleteStudent(int i) {
        studentDAO.deleteStudent(i);
    }
}
