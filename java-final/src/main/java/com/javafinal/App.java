package com.monprojet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
 n 
    @Override    
    public void start(Stage primaryStage) throws Exception {
        // Charger l'interface FXML
        VBox root = FXMLLoader.load(getClass().getResource("/layouts/App.fxml"));

        Button button = (Button) root.lookup("#clickButton");
        Label label = (Label) root.lookup("#messageLabel");

        button.setOnAction(event -> {
            label.setText("Connexion réussie !");
        });

        // Ajouter le CSS
        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        primaryStage.setTitle("Gestion des Utilisateurs");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
