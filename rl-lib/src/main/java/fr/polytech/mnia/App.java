package fr.polytech.mnia;

public class App 
{
    public static void main( String[] args ) throws Exception{
        System.out.println("\n\n######### SIMPLE ###########");
        SimpleRunner sr = new SimpleRunner() ;
        sr.execSequence() ;

        System.out.println("\n\n######### YOUTUBE ###########");
        YTRunner yt = new YTRunner() ;
        yt.execSequence() ;

//         SchedulerRunner sc = new SchedulerRunner() ;
//         sc.execSequence();

//        TicTacToeRunner tr = new TicTacToeRunner() ;
//        tr.execSequence();
               
        System.exit(0);
    }    
}
