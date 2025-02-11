package Etudiant;

import java.util.ArrayList;

public class Etudiant {

    public static void affiche (ArrayList<String> etudiant){
        for (String etudiants : etudiant){
            System.out.println(etudiant);
        }  
    }

    public static void supprime (ArrayList<String> etudiant){
        for (int i = 0; i < etudiant.size(); i++) {
            if (i%3==0){
                System.out.println("Etudiant numero : " + i%3+1);
            }
            System.out.println(etudiant.get(i));
        }
        
    }

    public static void ajoute (ArrayList<String> etudiant){
        int choix;
        String mot;
        if (choix == 1){
            System.out.println("Ajouter un Etudiant !");
            System.out.println("Nom, Prenom, Classe");
            for (int i = 0; i<3; i++){
                Scanner scanner = new Scanner(System.in);
                scanner.next();
                etudiant.add(mot);
            }
        } else {
            System.out.println("tant pis !");
        }
        scanner.close();
    }
}
