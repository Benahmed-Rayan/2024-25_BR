package com.monprojet;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class GestionUtilisateur {
    private ArrayList<String> utilisateurs = new ArrayList<>();

    public void ajouterUtilisateur(Connexion connexion, Scanner scanner) {
        scanner.nextLine();
        System.out.print("Nom de l'utilisateur : ");
        String nom = scanner.nextLine();

        System.out.print("Prénom de l'utilisateur : ");
        String prenom = scanner.nextLine();
        
        System.out.print("Email de l'utilisateur : ");
        String email = scanner.nextLine();
        
        try {
            String requete = "INSERT INTO utilisateurs (prenom, nom, email) VALUES (?, ?, ?)";
            PreparedStatement statement = connexion.getConnexion().prepareStatement(requete);
            statement.setString(1, prenom);
            statement.setString(2, nom);
            statement.setString(3, email);

            int lignesModifiees = statement.executeUpdate();
            System.out.println("Insertion réussie ! Lignes ajoutées : " + lignesModifiees);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'insertion : " + ex.getMessage());
        }
    }
}