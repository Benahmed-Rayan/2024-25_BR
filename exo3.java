public class exo3 {
    public static void main (String[] args){
        int tab[]={2,3,6,6,4,7,5,7,4,9};
        int somme;
        for(int i=0, i<10, i++){
            somme=somme+tab[i];
        }
        somme=somme/10;
        System.out.println("le moyenne est de : " + somme);

        int tab2[];
        for (int i=0, i<10, i++){
            tab2[i]=somme-tab[i];
            tab2[i]=tab2[i]*tab2[i];
        }
        int ecart;
        for (int i=0, i<10, i++){
            ecart+=tab2[i];
        }
        System.out.println("l ecart-type est : " ecart);
    }
}
