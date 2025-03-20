package com.projet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GestionUtilisateur {
    
    private final Connexion connexion;
    private static final String EMAIL_REGEX = "^[^@]+@[^@]+\\.[^@]+$";

    
    public void executerSQL(String requete) {
        try (Connection con = connexion.getConnexion();
             Statement statement = con.createStatement()) {
            statement.executeUpdate(requete);
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }
    

    public GestionUtilisateur(Connexion connexion) {
        this.connexion = connexion;
    }

    private boolean estEmailValide(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    // Ajouter un utilisateur
    public boolean ajouterUtilisateur(String prenom, String nom, String email) {
        if (!estEmailValide(email)) {
            System.err.println("Erreur : Email invalide !");
            return false;
        }

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

    public boolean modifierUtilisateur(int id, String prenom, String nom, String email) {
        if (!estEmailValide(email)) {
            System.err.println("Erreur : Email invalide !");
            return false;
        }

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
        
        String colonne = switch (critere.toLowerCase()) {
            case "prénom" -> "prenom";
            case "email" -> "email";
            default -> "nom";
        };
    
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

    public void ajouterHistorique(int utilisateurId, String description) {
        String checkUserQuery = "SELECT COUNT(*) FROM utilisateurs WHERE id = ?";
    
        try (PreparedStatement checkStmt = connexion.getConnexion().prepareStatement(checkUserQuery)) {
            checkStmt.setInt(1, utilisateurId);
            ResultSet rs = checkStmt.executeQuery();
    
            boolean userExists = false;
            if (rs.next() && rs.getInt(1) > 0) {
                userExists = true;
            }
    
            // Nouvelle requête pour insérer dans l'historique
            String sql = "INSERT INTO historique (utilisateur_id, description) VALUES (?, ?)";
            try (PreparedStatement stmt = connexion.getConnexion().prepareStatement(sql)) {
                if (userExists) {
                    stmt.setInt(1, utilisateurId); // On garde l'ID si l'utilisateur existe
                    stmt.setString(2, description);
                } else {
                    stmt.setNull(1, Types.INTEGER); // Met utilisateur_id à NULL
                    stmt.setString(2, "[Ancien ID: " + utilisateurId + "] " + description);
                }
                stmt.executeUpdate();
            }
    
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout dans l'historique : " + e.getMessage());
        }
    }
    
    

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
