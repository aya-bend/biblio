package com.library.dao;

import com.library.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDAO {
    private final Connection connection;
    private static final Logger LOGGER = Logger.getLogger(StudentDAO.class.getName());

    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    public void addStudent(Student student) {
        String query = "INSERT INTO students (id,name,email) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de l'étudiant", e);
        }
    }

    public Student getStudentById(int id) {
        String query = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email")
                    );
                }
                return null; // Retourne null si l'étudiant n'existe pas
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de l'étudiant", e);
            return null;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                students.add(new Student(resultSet.getInt("id"),resultSet.getString("name"), resultSet.getString("email")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des étudiants", e);
        }
        return students;
    }

    public void updateStudent(Student student) {
        String query = "UPDATE students SET email = ?, name = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, student.getEmail());
            statement.setString(2, student.getName());
            statement.setInt(3, student.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'étudiant", e);
        }
    }

    public void deleteStudent(int id) {
        String query = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'étudiant", e);
        }
    }
}
