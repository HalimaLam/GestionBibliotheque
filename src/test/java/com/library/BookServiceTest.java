package com.library;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Student;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.util.DbConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookService bookService;
    private BorrowService borrowService;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialisation de la connexion
        connection = DbConnection.getConnection();
        connection.setAutoCommit(false); // Démarre une transaction pour rollback après chaque test

        // Initialisation des DAO et du service
        bookDAO = new BookDAO(connection);
        studentDAO = new StudentDAO(connection);
        borrowService = new BorrowService(bookDAO, studentDAO);



        // Ajout d'étudiants
        studentDAO.addStudent(new Student(1, "Alice", "alice@example.com"));
        studentDAO.addStudent(new Student(2, "Bob", "bob@example.com"));

        // Ajout de livres
        bookDAO.add(new Book(1, "Java Programming", "John Doe", true));
        bookDAO.add(new Book(2, "Advanced Java", "Jane Doe", true));
    }

    @Test
    void testAddBook() {
        Book book = new Book(1, "Java Programming", "John Doe", true);
        bookService.addBook(book);
        assertEquals(1, bookDAO.getAllBooks().size());
        assertEquals("Java Programming", bookDAO.getBookById(1).get().getTitle());
    }

    @Test
    void testUpdateBook() {
        bookDAO.add(new Book(1, "Java Basics", "Author", true));
        Optional<Book> book = Optional.ofNullable(bookDAO.getBookById(1));
        assertTrue(book.isPresent(), "Le livre devrait exister");

        // Mise à jour du livre
        book.get().setTitle("Updated Title");
        assertEquals("Updated Title", book.get().getTitle());
    }


    @Test
    void testDeleteBook() {
        Book book = new Book(1, "Java Programming", "John Doe");
        bookService.addBook(book);
        bookService.deleteBook(1);
        assertTrue(bookDAO.getBookById(1).isEmpty());
    }
}
