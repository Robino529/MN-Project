package fr.polytech.mnia;

public class App 
{
    public static void main( String[] args ) throws Exception{           
        
        // SimpleRunner sr = new SimpleRunner() ;
        // sr.execSequence() ; 

        SchedulerRunner sc = new SchedulerRunner() ;
        sc.execSequence();
        
        System.exit(0);
    }    
}
