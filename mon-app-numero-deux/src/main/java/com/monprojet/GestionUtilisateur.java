package com.monprojet;

import java.sql.*;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class GestionUtilisateur {

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

        System.out.print("mail : ");
        String email = scanner.nextLine().trim();

        // Vérif si vide
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            System.out.println("Les champs ne peuvent pas être vides. Reessayez.");
            return;
        }

        try {
            String requete = "INSERT INTO utilisateurs (prenom, nom, email) VALUES (?, ?, ?)";
            PreparedStatement statement = connexion.getConnexion().prepareStatement(requete);
            statement.setString(1, prenom);
            statement.setString(2, nom);
            statement.setString(3, email);

            int lignesModifiees = statement.executeUpdate();
            System.out.println("Utilisateur ajté avec succès !" + lignesModifiees);
        } catch (SQLException ex) {
            System.err.println("Erreur d'ajout utilisateur" + ex.getMessage());
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
        scanner.nextLine(); 
        executeUpdate(connexion, "DELETE FROM utilisateurs WHERE id = ?", id);
    }


    public void modifierUtilisateur(Connexion connexion, Scanner scanner) {
        System.out.print("Entrez l'ID de l'utilisateur à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nouveau prénom (sinon remmettre l'ancien) : ");
        String prenom = scanner.nextLine().trim();

        System.out.print("Nouveau nom (sinon remmettre l'ancien) : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Nouvel email (sinon remmettre l'ancien) : ");
        String email = scanner.nextLine().trim();

        // Vérif de si le champ est vide
        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty()) {
            System.out.println("Les champs ne peuvent pas être vides. Veuillez réessayer.");
            return;
        }

        String requete = "UPDATE utilisateurs SET prenom = ?, nom = ?, email = ? WHERE id = ?";
        executeUpdate(connexion, requete, prenom, nom, email, id);
    }

    public void rechercherUtilisateur(Connexion connexion, Scanner scanner) {
        System.out.print("Entrez l'mail (ou vide pour ignore) : ");
        String email = scanner.nextLine().trim();
        System.out.print("Entrez le nom (ou vide pour ignore) : ");
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


    public void exporterUtilisateursCSV(Connexion connexion) {
        String fichierCSV = "utilisateurs.csv"; // Nom du fichier CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter(fichierCSV))) {
            // Ecrire l'en-tête du fichier CSV
            writer.println("ID,Prénom,Nom,Email");

            // Récupérer les utilisateurs de la base de données
            String requete = "SELECT * FROM utilisateurs";
            try (PreparedStatement statement = connexion.getConnexion().prepareStatement(requete);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    // Récupérer les informations de chaque utilisateur
                    int id = resultSet.getInt("id");
                    String prenom = resultSet.getString("prenom");
                    String nom = resultSet.getString("nom");
                    String email = resultSet.getString("email");

                    // Ecrire les informations dans le fichier CSV
                    writer.printf("%d,%s,%s,%s%n", id, prenom, nom, email);
                }
            }
            System.out.println("Les utilisateurs ont été exportés dans le fichier : " + fichierCSV);
        } catch (SQLException | IOException ex) {
            System.err.println("Erreur lors de l'exportation des utilisateurs : " + ex.getMessage());
        }
    }



}

