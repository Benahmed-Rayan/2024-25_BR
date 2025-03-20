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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load(); // Utilisation de Parent pour éviter le cast problématique
            Scene scene = new Scene(root, 1000, 500);
            primaryStage.setTitle("Gestion des Utilisateurs");
            primaryStage.setScene(scene);

            
            //primaryStage.setMaxWidth(1600); 
            //primaryStage.setMaxHeight(1000);

            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(400);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // Quitter proprement en cas d'erreur
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
