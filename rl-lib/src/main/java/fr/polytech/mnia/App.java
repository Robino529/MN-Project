package fr.polytech.mnia;

public class App 
{
    public static void main( String[] args ) throws Exception{           
        SimpleRunner sr = new SimpleRunner() ;
        sr.execSequence();
        sr.checkTransition();
        System.exit(0);
    }    
}
