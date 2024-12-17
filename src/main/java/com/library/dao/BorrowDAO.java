package com.library.dao;

import com.library.model.Borrow;
import com.library.model.Student;
import com.library.model.Book;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    // Récupérer toutes les emprunts
    public List<Borrow> getAllBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM borrows";
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Créez les objets Student et Book appropriés
                Student student = new Student(rs.getInt("student_id"), rs.getString("student_name"));
                Book book = new Book(rs.getInt("book_id"), rs.getString("book_title"));

                // Créez un objet Borrow avec les données extraites
                Borrow borrow = new Borrow(
                        rs.getInt("id"),
                        student,
                        book,
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date")
                );
                borrows.add(borrow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrows;
    }

    // Ajouter un nouvel emprunt
    public void addBorrow(Borrow borrow) {
        String query = "INSERT INTO borrows (student_id, book_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Insérer les valeurs des objets Student et Book
            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());
            stmt.setDate(3, new java.sql.Date(borrow.getBorrowDate().getTime()));
            stmt.setDate(4, new java.sql.Date(borrow.getReturnDate().getTime()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sauvegarder ou mettre à jour un emprunt
    public void save(Borrow borrow) {
        String query = "UPDATE borrows SET student_id = ?, book_id = ?, borrow_date = ?, return_date = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());
            stmt.setDate(3, new java.sql.Date(borrow.getBorrowDate().getTime()));
            stmt.setDate(4, new java.sql.Date(borrow.getReturnDate().getTime()));
            stmt.setInt(5, borrow.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
