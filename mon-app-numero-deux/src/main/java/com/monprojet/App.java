package com.monprojet;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("Bienvenue dans le système de gestion des utilisateurs !");

        Connexion connexion = new Connexion();
        Scanner scanner = new Scanner(System.in);
        GestionUtilisateur gestionUtilisateur = new GestionUtilisateur();
        int choix;

        do {
            System.out.println("\nMenu Principal :");
            System.out.println("1 - Ajouter un utilisateur");
            System.out.println("0 - Quitter");
            System.out.print("Votre choix : ");
            
            while (!scanner.hasNextInt()) {
                System.out.println("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    gestionUtilisateur.ajouterUtilisateur(connexion, scanner);
                    break;
                case 0:
                    System.out.println("Fermeture de l'application...");
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
                    break;
            }
        } while (choix != 0);

        connexion.fermer();
        scanner.close();
        System.out.println("Application terminée.");
        System.exit(0);
    }
}
