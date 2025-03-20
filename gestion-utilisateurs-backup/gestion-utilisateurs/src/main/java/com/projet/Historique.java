package com.projet;

import java.time.LocalDateTime;

public class Historique {
    private int id;
    private int utilisateurId;
    private String description;
    private LocalDateTime dateModification;

    public Historique(int id, int utilisateurId, String description, LocalDateTime dateModification) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.description = description;
        this.dateModification = dateModification;
    }

    public int getId() {
        return id;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }
}
