package com.projet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger la vue principale
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load(); // Utilisation de Parent pour éviter le cast problématique
            Scene scene = new Scene(root, 1000, 650);

            // Configurer la fenêtre principale
            primaryStage.setTitle("Gestion des Utilisateurs");
            primaryStage.setScene(scene);

            // Définir les tailles minimales et maximales de la fenêtre
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(550);

            // Afficher la fenêtre
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // Quitter proprement en cas d'erreur
        }
    }

    public static void main(String[] args) {
        // Lancer l'application
        launch(args);
    }
}