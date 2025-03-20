package com.projet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionUtilisateur {
    
    private Connexion connexion;

    public GestionUtilisateur(Connexion connexion) {
        this.connexion = connexion;
    }

    // Ajouter un utilisateur
    public boolean ajouterUtilisateur(String prenom, String nom, String email) {
        String sql = "INSERT INTO utilisateurs (prenom, nom, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connexion.getConnexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, prenom);
            stmt.setString(2, nom);
            stmt.setString(3, email);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        ajouterHistorique(userId, "Utilisateur ajouté : " + prenom + " " + nom);
                    }
                }
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout : " + e.getMessage());
            return false;
        }
    }

    public List<Utilisateur> getUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT id, prenom, nom, email, created_at, updated_at FROM utilisateurs";
        try (Statement stmt = connexion.getConnexion().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                utilisateurs.add(new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération : " + e.getMessage());
        }
        return utilisateurs;
    }

    // Supprimer un utilisateur
    public boolean supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try (PreparedStatement stmt = connexion.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                ajouterHistorique(id, "Utilisateur supprimé (ID: " + id + ")");
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
            return false;
        }
    }

    // Modifier un utilisateur
    public boolean modifierUtilisateur(int id, String prenom, String nom, String email) {
        String sql = "UPDATE utilisateurs SET prenom = ?, nom = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = connexion.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, prenom);
            stmt.setString(2, nom);
            stmt.setString(3, email);
            stmt.setInt(4, id);
            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                ajouterHistorique(id, "Utilisateur modifié : " + prenom + " " + nom);
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification : " + e.getMessage());
            return false;
        }
    }

    public List<Utilisateur> rechercherUtilisateurs(String recherche, String critere) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        
        // Définir la colonne selon le critère choisi
        String colonne;
        switch (critere) {
            case "prénom":
                colonne = "prenom";
                break;
            case "email":
                colonne = "email";
                break;
            default:
                colonne = "nom"; // Par défaut, recherche par nom
        }
    
        String sql = "SELECT id, prenom, nom, email, created_at, updated_at FROM utilisateurs WHERE " + colonne + " LIKE ?";
    
        try (PreparedStatement stmt = connexion.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, "%" + recherche + "%");
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    utilisateurs.add(new Utilisateur(
                            rs.getInt("id"),
                            rs.getString("prenom"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche : " + e.getMessage());
        }
    
        return utilisateurs;
    }

    // Ajouter une entrée dans l'historique
    public void ajouterHistorique(int utilisateurId, String description) {
        String sql = "INSERT INTO historique (utilisateur_id, description) VALUES (?, ?)";
        try (PreparedStatement stmt = connexion.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            stmt.setString(2, description);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout dans l'historique : " + e.getMessage());
        }
    }

    // Récupérer l'historique d'un utilisateur
    public List<Historique> getHistorique(int utilisateurId) {
        List<Historique> historiqueList = new ArrayList<>();
        String sql = "SELECT id, utilisateur_id, description, date_modification FROM historique WHERE utilisateur_id = ? ORDER BY date_modification DESC";
        try (PreparedStatement stmt = connexion.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    historiqueList.add(new Historique(
                            rs.getInt("id"),
                            rs.getInt("utilisateur_id"),
                            rs.getString("description"),
                            rs.getTimestamp("date_modification").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique : " + e.getMessage());
        }
        return historiqueList;
    }
}
