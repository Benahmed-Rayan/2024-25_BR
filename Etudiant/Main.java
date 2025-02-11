package Etudiant;
import java.util.ArrayList;
import java.util.Scanner; 


public class Main {

    private ArrayList<String> etudiant = new ArrayList<>(Etudiant);
    public static void main (String[] args){

        int menu = 0;
        Scanner scanner = new Scanner (System.in);
        do{
            System.out.println("Que voulez vous faire ?");
            System.out.println("1. Voir la liste");
            System.out.println("2. Ajouter un etudiant");
            System.out.println("3. Supprimer un étudiant");
            System.out.println("4. Quitter le programme");
            int cmd = scanner.nextInt();
            System.out.println("Vous voulez" + cmd);

            switch (menu){
                case 1: 
                    System.out.println("liste des etiduants : ");
    
                break;
                
                case 2: 
                    System.out.println("ajouter un etiduant : ");
    
                break;

                case 3: 
                    System.out.println("supprimer un etiduant : ");
    
                break;

                default:
                    System.out.println("Action non reconnu");
                    menu = 5;
                    break;
        }
        scanner.close();
    }
 
    
}
