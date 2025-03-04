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
            System.out.println("2 - Lister les utilisateurs");
            System.out.println("3 - Supprimer un utilisateur");
            System.out.println("4 - Modifier un utilisateur");
            System.out.println("5 - Rechercher un utilisateur");
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
                case 2:
                    gestionUtilisateur.listerUtilisateurs(connexion);
                    break;
                case 3:
                    gestionUtilisateur.supprimerUtilisateur(connexion, scanner);
                    break;
                case 4:
                    gestionUtilisateur.modifierUtilisateur(connexion, scanner);
                    break;
                case 5:
                    gestionUtilisateur.rechercherUtilisateur(connexion, scanner);
                    break;
                case 0:
                    System.out.println("Fermeture de l'application...");
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
                    break;
            }
        } while (choix != 0);

        // Ferme la connexion uniquement qnd l'user choisi de quitter
        connexion.fermer();
        scanner.close();
        System.out.println("Application terminée.");
        System.exit(0);
    }
}
