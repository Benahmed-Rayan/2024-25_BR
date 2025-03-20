package com.projet.controllers;

import com.projet.GestionUtilisateur;
import com.projet.Connexion;
import com.projet.Utilisateur;
import com.projet.data.CsvExporter; // Import ajouté
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurController {

    @FXML private ChoiceBox<String> choiceCritere;
    @FXML private TextField inputRecherche;
    @FXML private Button btnRechercher;
    @FXML private Button btnAjouter;
    @FXML private TableView<Utilisateur> tableUtilisateurs;
    @FXML private TableColumn<Utilisateur, Integer> colId;
    @FXML private TableColumn<Utilisateur, String> colPrenom;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colEmail;
    @FXML private TableColumn<Utilisateur, Timestamp> colCreatedAt;
    @FXML private TableColumn<Utilisateur, Timestamp> colUpdatedAt;
    @FXML private TableColumn<Utilisateur, Void> colActions;
    @FXML private Button btnActualiser;
    @FXML private Button btnHistorique;
    @FXML private Button btnExporterCsv; // Bouton pour exporter en CSV

    private final GestionUtilisateur gestionUtilisateur;

    public UtilisateurController() {
        Connexion connexion = new Connexion();
        gestionUtilisateur = new GestionUtilisateur(connexion);
    }

    @FXML
    public void initialize() {
        // Initialiser la ChoiceBox
        initializeChoiceBox();

        // Associer les colonnes aux attributs de l'objet Utilisateur
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        // Configurer la colonne Actions
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox container = new HBox(5, btnModifier, btnSupprimer);

            {
                btnModifier.setOnAction(event -> {
                    Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                    modifierUtilisateur(utilisateur);
                });

                btnSupprimer.setOnAction(event -> {
                    Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                    supprimerUtilisateur(utilisateur);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        // Associer les actions aux boutons
        btnActualiser.setOnAction(event -> chargerUtilisateurs());
        btnAjouter.setOnAction(event -> afficherFormulaireAjout());
        btnRechercher.setOnAction(event -> rechercherUtilisateur());
        btnHistorique.setOnAction(event -> afficherHistorique());
        btnExporterCsv.setOnAction(event -> exporterUtilisateursEnCsv()); // Ajout de l'action pour exporter en CSV

        // Charger les utilisateurs au démarrage
        chargerUtilisateurs();
    }

    /**
     * Exporte les utilisateurs en CSV.
     */
    private void exporterUtilisateursEnCsv() {
        List<Utilisateur> utilisateurs = gestionUtilisateur.getUtilisateurs();
        if (utilisateurs.isEmpty()) {
            afficherAlerte("Aucun utilisateur à exporter.", Alert.AlertType.WARNING);
            return;
        }

        // Préparer les données pour le CSV
        List<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{"ID", "Prénom", "Nom", "Email", "Créé le", "Mis à jour le"}); // En-tête

        for (Utilisateur utilisateur : utilisateurs) {
            String[] row = {
                String.valueOf(utilisateur.getId()),
                utilisateur.getPrenom(),
                utilisateur.getNom(),
                utilisateur.getEmail(),
                utilisateur.getCreatedAt().toString(),
                utilisateur.getUpdatedAt().toString()
            };
            csvData.add(row);
        }

        // Exporter en CSV
        String filePath = "data_sur_users.csv"; // Chemin du fichier CSV
        CsvExporter.exportToCsv(csvData, filePath);

        afficherAlerte("Export CSV réussi : " + filePath, Alert.AlertType.INFORMATION);
    }

    /**
     * Affiche une alerte à l'utilisateur.
     */
    private void afficherAlerte(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche la fenêtre de l'historique des modifications.
     */
    private void afficherHistorique() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Historique.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("Le fichier FXML 'Historique.fxml' est introuvable.");
            }
            Parent root = loader.load();

            // Vérifier si le contrôleur est bien chargé
            HistoriqueController controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("Le contrôleur HistoriqueController est introuvable.");
            }

            Stage stage = new Stage();
            stage.setTitle("Historique des Modifications");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture de l'historique : " + e.getMessage());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    /**
     * Initialise la ChoiceBox avec les critères de recherche.
     */
    private void initializeChoiceBox() {
        if (choiceCritere != null) {
            ObservableList<String> criteres = FXCollections.observableArrayList("Nom", "Prénom", "Email");
            choiceCritere.setItems(criteres);
            choiceCritere.setValue("Nom"); // Sélection par défaut
        }
    }

    /**
     * Charge la liste des utilisateurs dans la TableView.
     */
    private void chargerUtilisateurs() {
        List<Utilisateur> listeUtilisateurs = gestionUtilisateur.getUtilisateurs();
        ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList(listeUtilisateurs);
        tableUtilisateurs.setItems(utilisateurs);
    }

    /**
     * Recherche un utilisateur en fonction du critère et de la valeur saisie.
     */
    private void rechercherUtilisateur() {
        String recherche = inputRecherche.getText().trim();
        String critere = choiceCritere.getValue().toLowerCase();

        if (recherche.isEmpty()) {
            chargerUtilisateurs(); // Recharge tous les utilisateurs si le champ est vide
            return;
        }

        List<Utilisateur> resultats = gestionUtilisateur.rechercherUtilisateurs(recherche, critere);
        ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList(resultats);
        tableUtilisateurs.setItems(utilisateurs);
    }

    /**
     * Ouvre la fenêtre de modification d'un utilisateur.
     */
    private void modifierUtilisateur(Utilisateur utilisateur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/projet/views/ModifierUtilisateur.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et lui passer l'utilisateur à modifier
            ModifierUtilisateurController controller = loader.getController();
            controller.setUtilisateur(utilisateur);

            Stage stage = new Stage();
            stage.setTitle("Modifier un utilisateur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprime un utilisateur.
     */
    private void supprimerUtilisateur(Utilisateur utilisateur) {
        gestionUtilisateur.supprimerUtilisateur(utilisateur.getId());
        chargerUtilisateurs(); // Rafraîchir la table
    }

    /**
     * Ouvre la fenêtre d'ajout d'un utilisateur.
     */
    private void afficherFormulaireAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/projet/views/AjoutUtilisateur.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un utilisateur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}