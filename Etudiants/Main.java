package Etudiants;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestionEtudiant gestion = new GestionEtudiant();
        Scanner scanner = new Scanner(System.in);
        int choix;

        do {
            System.out.println("\nMenu Principal:");
            System.out.println("1. Voir la liste des étudiants");
            System.out.println("2. Ajouter un étudiant");
            System.out.println("3. Supprimer un étudiant");
            System.out.println("4. Quitter");

            System.out.print("Choisissez une option : ");
            choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    gestion.affiche();
                    break;
                case 2:
                    gestion.ajoute();
                    break;
                case 3:
                    gestion.supprime();
                    break;
                case 4:
                    System.out.println("Programme terminé.");
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        } while (choix != 4);

        scanner.close();
    }
}
