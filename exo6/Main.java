package exo6;

public class Main {
    
    static String[] cours = {"dev java", "management", "cyber"};
    
    public static String getElement (int index) throws ArrayIndexOutOfBoundsException{
        return cours[index];
    }
    
    public static void main (String[] args){
        try{
            System.out.println(getElement(6));
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Erreur capt " + e.getMessage());
        } 
    }
}
