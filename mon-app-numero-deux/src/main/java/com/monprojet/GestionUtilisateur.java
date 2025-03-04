package com.monprojet;

import java.sql.*;
import java.util.Scanner;

public class GestionUtilisateur {

    // Modifié pour ne pas fermer la connexion ici, mais la laisser ouverte
    private void executeUpdate(Connexion connexion, String requete, Object... params) {
        try (PreparedStatement statement = connexion.getConnexion().prepareStatement(requete)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            int lignesModifiees = statement.executeUpdate();
            System.out.println(lignesModifiees > 0 ? "Opération réussie." : "Aucune modification effectuée.");
        } catch (SQLException ex) {
            System.err.println("Erreur : " + ex.getMessage());
        }
    }


    public void ajouterUtilisateur(Connexion connexion, Scanner scanner) {
        if (connexion.getConnexion() == null) {
            System.err.println("Impossible d'ajouter un utilisateur : connexion à la base de données non établie.");
            return;
        }

        System.out.print("Nom : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Prénom : ");
        String prenom = scanner.nextLine().trim();

        System.out.print("Email : ");
        String email = scanner.nextLine().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            System.out.println("Les champs ne peuvent pas être vides. Veuillez réessayer.");
            return;
        }

        try {
            String requete = "INSERT INTO utilisateurs (prenom, nom, email) VALUES (?, ?, ?)";
            PreparedStatement statement = connexion.getConnexion().prepareStatement(requete);
            statement.setString(1, prenom);
            statement.setString(2, nom);
            statement.setString(3, email);

            int lignesModifiees = statement.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès ! Lignes affectées : " + lignesModifiees);
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'insertion de l'utilisateur : " + ex.getMessage());
        }
    }

    public void listerUtilisateurs(Connexion connexion) {
        try (PreparedStatement statement = connexion.getConnexion().prepareStatement("SELECT * FROM utilisateurs");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        resultSet.getInt("id"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("email")
                );
                System.out.println(utilisateur);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + ex.getMessage());
        }
    }

    public void supprimerUtilisateur(Connexion connexion, Scanner scanner) {
        System.out.print("Entrez l'ID de l'utilisateur à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Clear buffer
        executeUpdate(connexion, "DELETE FROM utilisateurs WHERE id = ?", id);
    }


    public void modifierUtilisateur(Connexion connexion, Scanner scanner) {
        System.out.print("Entrez l'ID de l'utilisateur à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nouveau prénom : ");
        String prenom = scanner.nextLine().trim();
        System.out.print("Nouveau nom : ");
        String nom = scanner.nextLine().trim();
        System.out.print("Nouvel email : ");
        String email = scanner.nextLine().trim();

        String requete = "UPDATE utilisateurs SET prenom = ?, nom = ?, email = ? WHERE id = ?";
        executeUpdate(connexion, requete, prenom, nom, email, id);
    }

    public void rechercherUtilisateur(Connexion connexion, Scanner scanner) {
        System.out.print("Entrez l'email (ou vide pour ignorer) : ");
        String email = scanner.nextLine().trim();
        System.out.print("Entrez le nom (ou vide pour ignorer) : ");
        String nom = scanner.nextLine().trim();

        StringBuilder requete = new StringBuilder("SELECT * FROM utilisateurs WHERE 1=1");
        if (!email.isEmpty()) requete.append(" AND email = ?");
        if (!nom.isEmpty()) requete.append(" AND nom = ?");

        try (PreparedStatement statement = connexion.getConnexion().prepareStatement(requete.toString())) {

            int index = 1;
            if (!email.isEmpty()) statement.setString(index++, email);
            if (!nom.isEmpty()) statement.setString(index, nom);

            try (ResultSet resultSet = statement.executeQuery()) {
                boolean found = false;
                while (resultSet.next()) {
                    Utilisateur utilisateur = new Utilisateur(
                            resultSet.getInt("id"),
                            resultSet.getString("prenom"),
                            resultSet.getString("nom"),
                            resultSet.getString("email")
                    );
                    System.out.println(utilisateur);
                    found = true;
                }
                if (!found) System.out.println("Aucun utilisateur trouvé.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche : " + ex.getMessage());
        }
    }
}
