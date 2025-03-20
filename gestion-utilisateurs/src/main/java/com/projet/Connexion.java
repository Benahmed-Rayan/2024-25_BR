package com.projet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private static final String URL = "jdbc:mysql://localhost:3306/projet_java";
    private static final String UTILISATEUR = "root";  
    private static final String MOT_DE_PASSE = "";  

    private Connection connexion;

    public Connexion() {
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion
            this.connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
            System.out.println("Connexion établie avec succès !");
        } catch (SQLException ex) {
            System.err.println("Erreur de connexion à la base de données : " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Le driver JDBC n'a pas été trouvé : " + e.getMessage());
        }
    }

    public Connection getConnexion() {
        try {
            if (this.connexion == null || this.connexion.isClosed()) {
                this.connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
                System.out.println("Connexion établie.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la connexion : " + e.getMessage());
        }
        return this.connexion;
    }

    public boolean isConnectionValid() {
        try {
            return this.connexion != null && !this.connexion.isClosed();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            return false;
        }
    }

    public void fermer() {
        if (this.connexion != null) {
            try {
                this.connexion.close();
                System.out.println("Connexion fermée correctement.");
            } catch (SQLException ex) {
                System.err.println("Problème lors de la fermeture de la connexion : " + ex.getMessage());
            }
        }
    }
}
