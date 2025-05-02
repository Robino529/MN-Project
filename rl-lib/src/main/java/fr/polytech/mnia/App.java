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

        System.out.println("\n\n######### TIC TAC TOE ###########");
        TicTacToeRunner tr = new TicTacToeRunner() ;
        tr.execSequence();

//         SchedulerRunner sc = new SchedulerRunner() ;
//         sc.execSequence();
               
        System.exit(0);
    }    
}
