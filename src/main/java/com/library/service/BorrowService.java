
package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.dao.BorrowDAO;
import com.library.model.Borrow;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowService {
    private StudentDAO studentDAO;
    private BorrowDAO borrowDAO;

    // Constructeur avec StudentDAO et BorrowDAO
    public BorrowService(StudentDAO studentDAO, BorrowDAO borrowDAO) {
        this.studentDAO = studentDAO;
        this.borrowDAO = borrowDAO;
    }

    public void borrowBook(Borrow borrow) {
        // Vérifier si l'étudiant existe
        Student student = studentDAO.getStudentById(borrow.getStudent().getId());
        if (student == null) {
            throw new RuntimeException("Étudiant non trouvé");
        }
        System.out.println("Student geted: " + student);

        if (student == null) {
            throw new RuntimeException("Étudiant non trouvé");
        }

        // Vérifier si le livre est disponible
        List<Borrow> activeBookBorrows = borrowDAO.getAllBorrows().stream()
                .filter(b -> b.getBook().getId() == borrow.getBook().getId()
                        && b.getReturnDate() == null)
                .collect(Collectors.toList());
        if (!activeBookBorrows.isEmpty()) {
            throw new RuntimeException("Le livre n'est pas disponible");
        }

        borrowDAO.save(borrow);
    }

    public void returnBook(Borrow borrow) {
        borrow.setReturnDate(new Date()); // Définir la date de retour
        borrowDAO.update(borrow);
    }

    //Afficher les emprunts (méthode fictive, à adapter)
    public void displayBorrows() {
        List<Borrow> borrows = borrowDAO.getAllBorrows();
        for (Borrow borrow : borrows) {
            System.out.println(
                    "ID: " + borrow.getId() +
                            "  Étudiant: " + borrow.getStudent().getName() +
                            "  Livre: " + borrow.getBook().getTitle() +
                            "  Date d'emprunt: " + borrow.getBorrowDate() +
                            "  Date de retour: " + borrow.getReturnDate()
            );
        }
    }
}