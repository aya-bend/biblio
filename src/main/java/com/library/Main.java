package com.library;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.service.BorrowService;
import com.library.service.BookService;
import com.library.service.StudentService;
import com.library.model.Book;
import com.library.model.Student;
import com.library.model.Borrow;
import com.library.dao.BorrowDAO;  // Importer BorrowDAO
import com.library.util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        // Création de la connexion
        Connection connection = DbConnection.getConnection();

        // Création des DAOs
        BookDAO bookDAO = new BookDAO(connection);
        StudentDAO studentDAO = new StudentDAO(connection);
        BorrowDAO borrowDAO = new BorrowDAO(connection);

        // Création des services
        BookService bookService = new BookService(bookDAO);
        StudentService studentService = new StudentService(studentDAO);
        BorrowService borrowService = new BorrowService(studentDAO, borrowDAO);



        Scanner scanner = new Scanner(System.in);

        // Création des services

        Student student1 = new Student(1, "John Doe");
        Book book1 = new Book("Effective Java", "Joshua Bloch", "123456", 2017);
        Borrow borrow1 = new Borrow( student1, book1, new Date(), new Date());
        
        boolean running = true;

        while (running) {
            System.out.println("\n===== Menu =====");
            System.out.println("1. Ajouter un livre");
            System.out.println("2. Afficher les livres");
            System.out.println("3. Ajouter un étudiant");
            System.out.println("4. Afficher les étudiants");
            System.out.println("5. Emprunter un livre");
            System.out.println("6. Afficher les emprunts");
            System.out.println("7. Quitter");

            System.out.print("Choisir une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consommer la ligne restante après l'entier

            switch (choice) {
                case 1:
                    System.out.print("Entrez le titre du livre: ");
                    String title = scanner.nextLine();
                    System.out.print("Entrez l'auteur du livre: ");
                    String author = scanner.nextLine();
                    System.out.print("Entrez ISBN du livre: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Entrez l'année de publication: ");
                    int publishedYear = scanner.nextInt();
                    Book book2 = new Book(1,title, author, isbn, publishedYear);
                    bookService.addBook(book2);
                    break;

                case 2:
                    bookService.displayBooks();
                    break;

                case 3:
                    System.out.print("Entrez le nom de l'étudiant: ");
                    String studentName = scanner.nextLine();
                    System.out.println("Entrez l'email de l'étudiant: ");
                    String studentEmail = scanner.nextLine();
                    Student student = new Student(1,studentName,studentEmail);
                    studentService.addStudent(student);
                    break;

                case 4:
                    studentService.displayStudents();
                    break;

                case 5:
                    try {
                        System.out.print("Entrez l'ID de l'étudiant: ");
                        int studentId = Integer.parseInt(scanner.nextLine());

                        System.out.print("Entrez l'ID du livre: ");
                        int bookId = Integer.parseInt(scanner.nextLine());
                        System.out.println("Entrez la date de retour du livre (yyyy-mm-dd): ");
                        String returnDateString = scanner.nextLine();
                        // Validation de la date
                        Date returnDate;
                        try {
                            returnDate = java.sql.Date.valueOf(returnDateString);
                            // Vérifier si la date est dans le futur
                            if (returnDate.before(new Date())) {
                                System.out.println("La date de retour doit être dans le futur.");
                                break;
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("Format de date invalide. Utilisez le format yyyy-mm-dd");
                            break;
                        }

                        Student studentForBorrow = studentService.findStudentById(studentId);
                        Book bookForBorrow = bookService.findBookById(bookId);

                        if (studentForBorrow == null) {
                            System.out.println("Étudiant non trouvé avec l'ID: " + studentId);
                            break;
                        }

                        if (bookForBorrow == null) {
                            System.out.println("Livre non trouvé avec l'ID: " + bookId);
                            break;
                        }

                        // Créer l'emprunt
                        Borrow borrow = new Borrow(studentForBorrow, bookForBorrow, new Date(), returnDate);

                        try {
                            borrowService.borrowBook(borrow);
                            System.out.println("Emprunt enregistré avec succès!");
                        } catch (RuntimeException e) {
                            System.out.println("Erreur lors de l'enregistrement de l'emprunt: " + e.getMessage());
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Veuillez entrer un nombre valide pour l'ID.");
                    } catch (Exception e) {
                        System.out.println("Une erreur est survenue: " + e.getMessage());
                    }
                    break;

                case 6:
                    borrowService.displayBorrows();
                    break;

                case 7:
                    running = false;
                    System.out.println("Au revoir!");
                    break;

                default:
                    System.out.println("Option invalide.");
            }
        }

        scanner.close();
    }
}
