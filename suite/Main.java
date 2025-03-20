package suite;

public class Main {
    public static void main(String[] args) {
        Transport voiture = new Voiture();
        Transport avion = new Avion();

        voiture.deplacer(); // Affiche: La voiture roule sur la route.
        avion.deplacer();   // Affiche: L'avion vole dans le ciel.
    }
}
