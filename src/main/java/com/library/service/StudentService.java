package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.util.DbConnection;

import java.sql.SQLException;
import java.util.List;

import java.sql.Connection;

public class StudentService {
    private StudentDAO studentDAO;

    // Constructeur par défaut
    public StudentService() {
        try {
            // Récupérer une connexion à la base de données
            Connection connection = DbConnection.getConnection();
            this.studentDAO = new StudentDAO(connection);  // Passer la connexion au DAO
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    // Constructeur personnalisé
    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    // Ajouter un étudiant
    public void addStudent(Student student) {
        try {
            studentDAO.addStudent(student);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage des étudiants : " + e.getMessage());
        }
    }

    // Afficher tous les étudiants
    public void displayStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();  // Cela peut lancer une SQLException
            for (Student student : students) {
                System.out.println("ID: " + student.getId() + " | Nom: " + student.getName());
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage des étudiants : " + e.getMessage());
        }
    }

    // Trouver un étudiant par ID
    public Student findStudentById(int id) {
        try {
            return studentDAO.getStudentById(id);  // Cela peut lancer une SQLException
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'étudiant par ID : " + e.getMessage());
        }
        return null;
    }

    public void addStudent(int i, String alice, String mail) {

    }

    public void updateStudent(int i, String aliceSmith, String mail) {
    }

    public void deleteStudent(int i) {
    }
}