package com.monprojet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private static final String URL = "jdbc:mysql://localhost:3306/mabasegr1";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "root";
    private Connection connexion = null;

    public Connexion() {
        try {
            this.connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
            System.out.println("Connexion établie avec succès !");
        } catch (SQLException ex) {
            System.err.println("Erreur de connexion à la base de données : " + ex.getMessage());
        }
    }

    public Connection getConnexion() {
        return this.connexion;
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
