package com.library;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.service.BookService;
import com.library.util.DbConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookService bookService;
    private BookDAO bookDAO;

    @BeforeEach
    void setUp() throws SQLException {
        DbConnection.getConnection().prepareStatement("DELETE FROM books").executeUpdate();
        bookDAO = new BookDAO(DbConnection.getConnection());
        bookService = new BookService(bookDAO);
    }

    @Test
    void testAddBook() {
        Book book = new Book(1,"Java Programming", "John Doe","12345", 2023);
        bookService.addBook(book);
        System.out.println("Livre ajouté avec succès");
        List<Book> books = bookDAO.getAllBooks();
        int bookId = books.get(0).getId();
        System.out.println("Récuperer User with id 1 pour confirmer l'ajout: \n" + bookDAO.getBookById(bookId));
        assertEquals(1,bookDAO.getBookById(bookId).getId());
        assertEquals("Java Programming", bookDAO.getBookById(bookId).getTitle());
    }

    @Test
    void displayBooks() {
        Book book1 = new Book(1,"Java Programming", "John Doe", "12345", 2023);
        Book book2 = new Book(2,"Python Programming", "Jane Doe", "12346", 2024);
//        Book book3 = new Book(1,"Java Programming", "John Doe", "12345", 2023);
//
//        Book book4 = new Book(1,"Java Programming", "John Doe", "12345", 2023);
//        Book book5 = new Book(1,"Java Programming", "John Doe", "12345", 2023);
//        Book boo6 = new Book(1,"Java Programming", "John Doe", "12345", 2023);
//        Book book7 = new Book(1,"Java Programming", "John Doe", "12345", 2023);
//        Book book8 = new Book(1,"Java Programming", "John Doe", "12345", 2023);


        bookService.addBook(book1);
        bookService.addBook(book2);
        List<Book> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("Aucun livre n'est disponible dans la bibliothèque.");
            return;
        }
        System.out.println("\n=== Liste des Livres ===");
        for (Book book : books) {
            System.out.println("ID: " + book.getId()
                    + "| Titre: " + book.getTitle()
                    + "| Auteur: " + book.getAuthor()
                    + "| ISBN: " + book.getIsbn()
                    + "| Année de publication: " + book.getPublishedYear()
                    );
        }

    }

    @Test
    void testUpdateBook() {
        Book book = new Book(2,"Java Programming", "John Doe", "12345", 2023);
        bookService.addBook(book);
        System.out.println("Livre ajouté avec succès: \n"+ bookDAO.getBookById(2));

        // Créer un livre mis à jour
        Book bookUpdate = new Book(2,"Advanced Java", "Jane Doe", "12345", 2025);
        bookService.updateBook(bookUpdate);
        System.out.println("Livre mis à jour avec succès: \n"+ bookDAO.getBookById(2));

        Book updatedBook = bookDAO.getBookById(2);
        assertEquals("Advanced Java", updatedBook.getTitle());
        assertEquals("Jane Doe", updatedBook.getAuthor());
    }

    @Test
    void testDeleteBook() {
        Book book = new Book(1,"Java Programming", "John Doe", "12345", 2023);
        bookService.addBook(book);
        // Supprimer le livre
        bookService.deleteBook(1);
        System.out.println("Livre supprimé avec succès");

        // Vérifier que le livre est supprimé
        assertNull(bookDAO.getBookById(1));
    }
}
