package com.projet.controllers;

import com.projet.GestionUtilisateur;
import com.projet.Connexion;
import com.projet.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierUtilisateurController {

    @FXML private TextField inputPrenom;
    @FXML private TextField inputNom;
    @FXML private TextField inputEmail;
    @FXML private Button btnConfirmerModification;

    private final GestionUtilisateur gestionUtilisateur;
    private Utilisateur utilisateur;

    public ModifierUtilisateurController() {
        Connexion connexion = new Connexion();
        gestionUtilisateur = new GestionUtilisateur(connexion);
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        inputPrenom.setText(utilisateur.getPrenom());
        inputNom.setText(utilisateur.getNom());
        inputEmail.setText(utilisateur.getEmail());
    }

    @FXML
    public void initialize() {
        btnConfirmerModification.setOnAction(event -> modifierUtilisateur());
    }

    private void modifierUtilisateur() {
        String prenom = inputPrenom.getText().trim();
        String nom = inputNom.getText().trim();
        String email = inputEmail.getText().trim();

        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty()) {
            System.out.println("Tous les champs doivent être remplis !");
            return;
        }

        boolean success = gestionUtilisateur.modifierUtilisateur(utilisateur.getId(), prenom, nom, email);
        if (success) {
            System.out.println("Utilisateur modifié avec succès !");
            fermerFenetre();
        } else {
            System.out.println("Erreur lors de la modification.");
        }
    }

    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) inputPrenom.getScene().getWindow();
        stage.close();
    }
}
