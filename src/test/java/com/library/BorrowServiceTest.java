package com.library;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;
import com.library.service.BorrowService;
import com.library.util.DbConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BorrowServiceTest {
    private BorrowService borrowService;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;
    private BorrowDAO borrowDAO;

    @BeforeEach
    void setUp() throws SQLException {
        var connection = DbConnection.getConnection();
        // Nettoyer la base de données
        connection.prepareStatement("DELETE FROM borrows").executeUpdate();
        connection.prepareStatement("DELETE FROM books").executeUpdate();
        connection.prepareStatement("DELETE FROM students").executeUpdate();

        // Initialiser les DAOs
        bookDAO = new BookDAO(connection);
        studentDAO = new StudentDAO(connection);
        borrowDAO = new BorrowDAO(connection);
        borrowService = new BorrowService(studentDAO, borrowDAO);

        // Ajouter des données de test
        Student student1 = new Student(1, "Alice", "alice@example.com");
        Student student2 = new Student(2, "Bob", "bob@example.com");
        studentDAO.addStudent(student1);
        studentDAO.addStudent(student2);

        Book book1 = new Book(1, "Java Programming", "John Doe", "12335", 2023);
        Book book2 = new Book(2, "Advanced Java", "Jane Doe", "12334", 2023);
        bookDAO.add(book1);
        bookDAO.add(book2);
    }

    @Test
    void testBorrowBook() {
        // Récupérer les données de test
        Student student = studentDAO.getStudentById(1);
        assertNotNull(student, "L'étudiant devrait exister");

        Book book = bookDAO.getBookById(1);
        assertNotNull(book, "Le livre devrait exister");

        // Créer et enregistrer l'emprunt
        Borrow borrow = new Borrow(1, student, book, new Date(), null);
        borrowService.borrowBook(borrow);

        // Vérifier
        List<Borrow> borrows = borrowDAO.getAllBorrows();
        assertFalse(borrows.isEmpty(), "La liste des emprunts ne devrait pas être vide");

        Borrow savedBorrow = borrows.get(0);
        assertEquals(1, savedBorrow.getId(), "L'ID de l'emprunt devrait être 1");
        assertEquals(student.getId(), savedBorrow.getStudent().getId(), "L'ID de l'étudiant devrait correspondre");
        assertEquals(book.getId(), savedBorrow.getBook().getId(), "L'ID du livre devrait correspondre");
        assertNotNull(savedBorrow.getBorrowDate(), "La date d'emprunt ne devrait pas être nulle");
        assertNull(savedBorrow.getReturnDate(), "La date de retour devrait être nulle");
    }

    @Test
    void testReturnBook() {
        // Créer un emprunt
        Student student = studentDAO.getStudentById(1);
        Book book = bookDAO.getBookById(1);
        Borrow borrow = new Borrow(1, student, book, new Date(), null);
        borrowService.borrowBook(borrow);

        // Récupérer l'emprunt
        List<Borrow> borrows = borrowDAO.getAllBorrows();
        assertFalse(borrows.isEmpty(), "La liste des emprunts ne devrait pas être vide");
        Borrow savedBorrow = borrows.get(0);

        // Retourner le livre
        borrowService.returnBook(savedBorrow);

        // Vérifier
        Borrow returnedBorrow = borrowDAO.getAllBorrows().get(0);
        assertNotNull(returnedBorrow.getReturnDate(), "La date de retour ne devrait pas être nulle");
    }

    @Test
    void testBorrowBookStudentNotFound() {
        Book book = bookDAO.getBookById(1);
        assertNotNull(book, "Le livre devrait exister");

        // Créer un étudiant avec un ID qui n'existe pas dans la base
        Student nonExistentStudent = new Student(999, "NonExistent", "none@example.com");
        Borrow borrow = new Borrow(3, nonExistentStudent, book, new Date(), null);

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> borrowService.borrowBook(borrow),
                "Devrait lancer une exception pour un étudiant inexistant"
        );

        assertEquals("Étudiant non trouvé", exception.getMessage());
    }
}