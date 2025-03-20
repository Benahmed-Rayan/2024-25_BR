package com.projet.controllers;

import com.projet.GestionUtilisateur;
import com.projet.Connexion;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AjoutUtilisateurController {

    @FXML private TextField inputPrenom;
    @FXML private TextField inputNom;
    @FXML private TextField inputEmail;
    @FXML private Button btnConfirmerAjout;

    private final GestionUtilisateur gestionUtilisateur;

    public AjoutUtilisateurController() {
        Connexion connexion = new Connexion();
        gestionUtilisateur = new GestionUtilisateur(connexion);
    }

    @FXML
    public void initialize() {
        btnConfirmerAjout.setOnAction(event -> ajouterUtilisateur());
    }

    private void ajouterUtilisateur() {
        String prenom = inputPrenom.getText().trim();
        String nom = inputNom.getText().trim();
        String email = inputEmail.getText().trim();
    
        // Vérifier que tous les champs sont remplis
        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty()) {
            System.out.println("Tous les champs doivent être remplis !");
            return;
        }
    
        // Vérifier que le prénom et le nom contiennent au moins 3 caractères
        if (prenom.length() < 3 || nom.length() < 3) {
            System.out.println("Le prénom et le nom doivent contenir au moins 3 caractères !");
            return;
        }
    
        boolean success = gestionUtilisateur.ajouterUtilisateur(prenom, nom, email);
        if (success) {
            System.out.println("Utilisateur ajouté avec succès !");
            fermerFenetre();
        } else {
            System.out.println("Erreur lors de l'ajout de l'utilisateur.");
        }
    }
    

    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) inputPrenom.getScene().getWindow();
        stage.close();
    }
}
