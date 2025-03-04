package com.monprojet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class GestionUtilisateur {
    private ArrayList<String> utilisateurs = new ArrayList<>();

    public void ajouterUtilisateur(Connexion connexion, Scanner scanner) {
        if (connexion.getConnexion() == null) {
            System.err.println("Impossible d'ajouter un utilisateur : connexion à la base de données non établie.");
            return;
        }

        System.out.print("Nom de l'utilisateur : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Prénom de l'utilisateur : ");
        String prenom = scanner.nextLine().trim();

        System.out.print("Email de l'utilisateur : ");
        String email = scanner.nextLine().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            System.out.println("Les champs ne peuvent pas être vides. Veuillez réessayer.");
            return;
        }

        try {
            Connection dbConnexion = connexion.getConnexion();
            String requete = "INSERT INTO utilisateurs (prenom, nom, email) VALUES (?, ?, ?)";
            PreparedStatement statement = dbConnexion.prepareStatement(requete);
            statement.setString(1, prenom);
            statement.setString(2, nom);
            statement.setString(3, email);

            int lignesModifiees = statement.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès ! Lignes affectées : " + lignesModifiees);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'insertion de l'utilisateur : " + ex.getMessage());
        }
    }
}
