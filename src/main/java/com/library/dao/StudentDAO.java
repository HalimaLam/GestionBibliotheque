package com.library.dao;

import com.library.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDAO {

    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(StudentDAO.class.getName());

    // Constructeur avec connexion
    public StudentDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La connexion à la base de données ne peut pas être null.");
        }
        this.connection = connection;
    }

    // Constructeur sans connexion (optionnel, mais doit être utilisé avec prudence)
    public StudentDAO() {
        // Ajouter ici l'initialisation de la connexion si nécessaire
        // Exemple : throw new UnsupportedOperationException("Connexion non initialisée");
    }

    /**
     * Ajoute un étudiant dans la base de données.
     */
    public void addStudent(Student student) throws SQLException {
        if (student == null) {
            throw new IllegalArgumentException("L'étudiant ne peut pas être null.");
        }

        String query = "INSERT INTO students (id, name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getId());
            statement.setString(2, student.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de l'étudiant", e);
            throw e;
        }
    }

    /**
     * Récupère un étudiant par son ID.
     */
    public Student getStudentById(int id) throws SQLException {
        String query = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Student(resultSet.getInt("id"), resultSet.getString("name"));
                } else {
                    LOGGER.log(Level.WARNING, "Aucun étudiant trouvé avec l'ID : {0}", id);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de l'étudiant", e);
            throw e;
        }
        return null;
    }

    /**
     * Récupère la liste de tous les étudiants.
     */
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                students.add(new Student(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des étudiants", e);
            throw e;
        }
        return students;
    }

    /**
     * Met à jour les informations d'un étudiant.
     */
    public void updateStudent(Student student) throws SQLException {
        if (student == null) {
            throw new IllegalArgumentException("L'étudiant ne peut pas être null.");
        }

        String query = "UPDATE students SET name = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, student.getName());
            statement.setInt(2, student.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                LOGGER.log(Level.WARNING, "Aucune mise à jour n'a été effectuée, l'étudiant n'existe pas.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'étudiant", e);
            throw e;
        }
    }

    /**
     * Supprime un étudiant par son ID.
     */
    public void deleteStudent(int id) throws SQLException {
        String query = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                LOGGER.log(Level.WARNING, "Aucun étudiant supprimé avec l'ID : {0}", id);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'étudiant", e);
            throw e;
        }
    }

    /**
     * Méthode pour fermer la connexion (optionnelle).
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la fermeture de la connexion", e);
            }
        }
    }
}
