package com.library.service;

import com.library.dao.BookDAO; // Importation de BookDAO
import com.library.model.Book; // Importation de Book
import java.util.List;

public class BookService {

    private final BookDAO bookDAO; // Utilisation de DAO pour la gestion des livres

    // Constructeur qui initialise l'objet BookDAO
    public BookService(BookDAO bookDAO) {
        if (bookDAO == null) {
            throw new IllegalArgumentException("BookDAO ne peut pas être null");
        }
        this.bookDAO = bookDAO;
    }

    // Ajouter un livre
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Le livre ne peut pas être null");
        }

        // Exemple de logique métier : vérifier si le titre ou l'ISBN sont vides
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Le titre du livre ne peut pas être vide");
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new IllegalArgumentException("L'ISBN du livre ne peut pas être vide");
        }

        // Appeler la méthode `add` du DAO
        bookDAO.add(book);
    }

    // Afficher tous les livres
    public void displayBooks() {
        List<Book> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("Aucun livre disponible.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    // Trouver un livre par ID
    public Book findBookById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID doit être positif");
        }

        Book book = bookDAO.getBookById(id);
        if (book == null) {
            System.out.println("Aucun livre trouvé avec l'ID : " + id);
        }
        return book;
    }

    // Supprimer un livre par ID
    public void deleteBook(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID doit être positif");
        }

        bookDAO.delete(id);
        System.out.println("Livre supprimé avec succès (ID : " + id + ").");
    }

    // Mise à jour des informations d'un livre
    public void updateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Le livre ne peut pas être null");
        }
        if (book.getId() <= 0) {
            throw new IllegalArgumentException("L'ID du livre doit être positif");
        }

        // Appeler la méthode `update` du DAO
        bookDAO.update(book);
        System.out.println("Livre mis à jour avec succès (ID : " + book.getId() + ").");
    }

    // Supprimer la méthode inutile ou incompréhensible
    // Si nécessaire, vous pouvez créer une méthode nommée correctement et correspondant au contexte
    // public void updateBook(int id, String title, String author, boolean available) {
    //     Book book = findBookById(id);
    //     if (book != null) {
    //         book.setTitle(title);
    //         book.setAuthor(author);
    //         book.setAvailable(available);
    //         updateBook(book);
    //     }
    // }
}
