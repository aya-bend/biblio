package com.library;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.service.StudentService;
import com.library.util.DbConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    private StudentService studentService;
    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() throws SQLException {

        DbConnection.getConnection().prepareStatement("DELETE FROM students").executeUpdate();


        studentDAO = new StudentDAO(DbConnection.getConnection());
        studentService = new StudentService(studentDAO);
    }

    @Test
    void testAddStudent() {
        // Ajouter un étudiant
        Student student = new Student(1,"Alice", "alice@example.com");
        studentService.addStudent(student);
        System.out.println("Etudiant ajouté avec succès: " + student);

        // Vérifier l'ajout de l'étudiant
        List<Student> students = studentDAO.getAllStudents();
        assertFalse(students.isEmpty(), "Liste d'étudiants ne doit pas être vide");
        Student studentadded = studentDAO.getStudentById(1);
        System.out.println("L'etudiant ajouté est: "+ studentadded);

        int studentId = studentadded.getId(); // Récupérer l'ID de l'étudiant ajouté
        assertEquals("Alice", studentDAO.getStudentById(studentId).getName(),
                "Le nom de l'étudiant ajouté ne correspond pas");
    }
    @Test
    void testUpdateStudent() {
            Student student = new Student(2, "maryam", "maryamon222@gmail.com");
            studentDAO.addStudent(student);

            System.out.println("Student avant update " + student);

            Student student2 = new Student(2, "maryame", "maryamon2223@gmail.com");
            studentDAO.updateStudent(student2);

            Student student3 = studentDAO.getStudentById(2);
            System.out.println("Student 3 : "+ student3);
            System.out.println("Student apres update "+ studentDAO.getStudentById(2));

        }

    @Test
    void testDeleteStudent() {

        Student student = new Student(3,"Alice", "alice@example.com");
        studentService.addStudent(student);

        System.out.println("Student avant delete "+ studentService.findStudentById(3));

        studentService.deleteStudent(3);
        System.out.println("Student deleted");

        assertNull(studentDAO.getStudentById(3));
    }

    @Test
    void testGetAllStudents() {
        Student student1 = new Student(4,"Alice", "alice@example.com");
        Student student2 = new Student(5,"Bob", "bob@example.com");
        studentService.addStudent(student1);
        studentService.addStudent(student2);
        List<Student> students= studentDAO.getAllStudents();
        System.out.println("\n=== Liste des Students ===");
        for (Student student : students) {
            System.out.println("Nom: " + student.getName() + " | email: " + student.getEmail());
        }
        assertEquals(2, studentDAO.getAllStudents().size());
    }
    @Test
    void testFindStudentById() {
        Student student = new Student(6, "maryamoo", "maryamoooo@gmail.com");
        studentDAO.addStudent(student);
        Student student1 = studentService.findStudentById(6);
        System.out.println("Student trouvé: "+ student1);

    }
}
