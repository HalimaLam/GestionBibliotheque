package com.library;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Student;
import com.library.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BorrowServiceTest {
    private BorrowService borrowService;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() throws SQLException {
        // Vous devez obtenir une connexion valide ou utiliser une base de données en mémoire pour les tests.
        // Ex : Connection connection = DbConnection.getTestConnection();
        // Ici, on part du principe que bookDAO et studentDAO gèrent déjà la connexion.

        bookDAO = new BookDAO();
        borrowService = new BorrowService(bookDAO, studentDAO);

        // Ajouter un étudiant
        studentDAO.addStudent(new Student(1, "Alice", "alice@example.com"));
        studentDAO.addStudent(new Student(2, "Bob", "bob@example.com"));

        // Ajouter des livres
        bookDAO.addBook(new Book(1, "Java Programming", "John Doe", true));
        bookDAO.addBook(new Book(2, "Advanced Java", "Jane Doe", true));
    }

    @Test
    void testBorrowBook() {
        // Test d'emprunt d'un livre
        assertEquals("Livre emprunté avec succès!", borrowService.borrowBook(1, 1));

        Optional<Book> book = Optional.ofNullable(bookDAO.getBookById(1));
        assertTrue(book.isPresent());  // Vérification que le livre existe
        assertFalse(book.get().isAvailable());  // Vérification que le livre n'est plus disponible après emprunt
    }

    @Test
    void testReturnBook() {
        // Test d'emprunt puis de retour d'un livre
        borrowService.borrowBook(1, 1);

        assertEquals("Livre retourné avec succès!", borrowService.returnBook(1, 1));

        Optional<Book> book = Optional.ofNullable(bookDAO.getBookById(1));
        assertTrue(book.isPresent());  // Vérification que le livre existe toujours
        assertTrue(book.get().isAvailable());  // Vérification que le livre est disponible après retour
    }

    @Test
    void testBorrowBookNotAvailable() {
        // Test pour un livre déjà emprunté
        borrowService.borrowBook(1, 1);  // Emprunt du premier livre
        assertEquals("Le livre n'est pas disponible.", borrowService.borrowBook(2, 1));  // Tentative d'emprunter le même livre
    }

    @Test
    void testBorrowBookStudentNotFound() {
        // Test pour un étudiant inexistant
        assertEquals("Étudiant ou livre non trouvé.", borrowService.borrowBook(3, 1));  // Tentative avec un ID d'étudiant inexistant
    }
}
