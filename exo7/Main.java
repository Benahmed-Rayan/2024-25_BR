package exo7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main (String[] args){
        try{
            File fichier = new File("products/liste.txt");
            Scanner lecteur = new Scanner(fichier);
            while (lecteur.hasNextLine()) {
                String ligne = lecteur.nextLine();
                System.out.println(ligne);

            }
            lecteur.close();
        } catch(FileNotFoundException e){
            System.out.println("fichier introuvable");
        }
    }
    
}
