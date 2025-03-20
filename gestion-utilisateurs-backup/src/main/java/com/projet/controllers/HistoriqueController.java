package com.projet.controllers;

import com.projet.Connexion;
import com.projet.Historique;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class HistoriqueController {
    @FXML private TableView<Historique> tableHistorique;
    @FXML private TableColumn<Historique, Integer> colId;
    @FXML private TableColumn<Historique, Integer> colUtilisateurId;
    @FXML private TableColumn<Historique, String> colDescription;
    @FXML private TableColumn<Historique, LocalDateTime> colDate;

    private final ObservableList<Historique> historiqueList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUtilisateurId.setCellValueFactory(new PropertyValueFactory<>("utilisateurId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateModification"));

        chargerHistorique();
    }

    private void chargerHistorique() {
        String query = "SELECT * FROM historique";

        try (Connection conn = new Connexion().getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Historique historique = new Historique(
                    rs.getInt("id"),
                    rs.getInt("utilisateur_id"),
                    rs.getString("description"),
                    rs.getTimestamp("date_modification").toLocalDateTime()
                );
                historiqueList.add(historique);
            }

            tableHistorique.setItems(historiqueList);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de l'historique : " + e.getMessage());
        }
    }
}
