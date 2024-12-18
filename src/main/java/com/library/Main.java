package com.library;
import com.library.dao.BookDAO;
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
        Scanner scanner = new Scanner(System.in);

        // Création des services
        Connection connection = DbConnection.getConnection();

        // Instancier BookDAO
        BookDAO bookDAO = new BookDAO(connection);

        // Passer BookDAO à BookService
        BookService bookService = new BookService(bookDAO);
        StudentService studentService = new StudentService();
        BorrowDAO borrowDAO = new BorrowDAO();  // Création de BorrowDAO
        BorrowService borrowService = new BorrowService(borrowDAO);  // Passer BorrowDAO au constructeur de BorrowService

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
                    System.out.print("Entrez l'isbn du livre: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Entrez la date du livre: ");
                    String year = scanner.nextLine();

                    // Vérification de l'ISBN avant de continuer
                    if (isbn == null || isbn.trim().isEmpty()) {
                        System.out.println("L'ISBN ne peut pas être vide.");
                        break;  // Revenir au menu sans ajouter le livre
                    }

                    Book book = new Book(title, author, Integer.parseInt(year),isbn);  // Création d'un nouveau livre
                    bookService.addBook(book);  // Ajout du livre au service
                    break;

                case 2:
                    bookService.displayBooks();  // Affichage des livres via le service
                    break;

                case 3:
                    System.out.print("Entrez le nom de l'étudiant: ");
                    String studentName = scanner.nextLine();
                    Student student = new Student(studentName);  // Création d'un étudiant
                    studentService.addStudent(student);  // Ajout de l'étudiant au service
                    break;

                case 4:
                    studentService.displayStudents();  // Affichage des étudiants
                    break;

                case 5:
                    System.out.print("Entrez l'ID de l'étudiant: ");
                    int studentId = scanner.nextInt();
                    System.out.print("Entrez l'ID du livre: ");
                    int bookId = scanner.nextInt();
                    Student studentForBorrow = studentService.findStudentById(studentId);  // Récupérer l'étudiant
                    Book bookForBorrow = bookService.findBookById(bookId);  // Récupérer le livre
                    if (studentForBorrow != null && bookForBorrow != null) {
                        // Créer un objet Borrow avec les informations nécessaires
                        Borrow borrow = new Borrow(0,studentForBorrow, bookForBorrow, new Date(), null);
                        borrowService.borrowBook(borrow);  // Emprunter le livre via le service
                    } else {
                        System.out.println("Étudiant ou livre introuvable.");
                    }
                    break;

                case 6:
                    borrowService.displayBorrows();  // Affichage des emprunts
                    break;

                case 7:
                    running = false;  // Quitter le programme
                    System.out.println("Au revoir!");
                    break;

                default:
                    System.out.println("Option invalide.");
            }
        }

        scanner.close();
    }
}
