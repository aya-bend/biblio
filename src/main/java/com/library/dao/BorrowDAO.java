package com.library.dao;

import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {
    private final Connection connection;

    public BorrowDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Borrow> getAllBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM borrows";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int studentId = rs.getInt("student_id");
                int bookId = rs.getInt("book_id");

                Student student = new StudentDAO(connection).getStudentById(studentId);
                Book book = new BookDAO(connection).getBookById(bookId);

                Borrow borrow = new Borrow(
                        id,
                        student,
                        book,
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date")
                );
                borrows.add(borrow);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all borrows", e);
        }
        return borrows;
    }

    public void save(Borrow borrow) {
        String query = "INSERT INTO borrows (id, student_id, book_id, borrow_date, return_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, borrow.getId());
            stmt.setInt(2, borrow.getStudent().getId());
            stmt.setInt(3, borrow.getBook().getId());
            stmt.setDate(4, new java.sql.Date(borrow.getBorrowDate().getTime()));
            stmt.setDate(5, borrow.getReturnDate() != null ? new java.sql.Date(borrow.getReturnDate().getTime()) : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving borrow", e);
        }
    }

    public void update(Borrow borrow) {
        String query = "UPDATE borrows SET student_id = ?, book_id = ?, borrow_date = ?, return_date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());
            stmt.setDate(3, new java.sql.Date(borrow.getBorrowDate().getTime()));
            stmt.setDate(4, borrow.getReturnDate() != null ? new java.sql.Date(borrow.getReturnDate().getTime()) : null);
            stmt.setInt(5, borrow.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating borrow", e);
        }
    }

    // Ajout d'une méthode pour vérifier si un livre est déjà emprunté
    public boolean isBookBorrowed(int bookId) {
        String query = "SELECT COUNT(*) FROM borrows WHERE book_id = ? AND return_date IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if book is borrowed", e);
        }
        return false;
    }
}