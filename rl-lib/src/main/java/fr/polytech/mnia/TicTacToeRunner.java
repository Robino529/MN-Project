package fr.polytech.mnia;

import fr.polytech.mnia.envs.Env;
import fr.polytech.mnia.envs.EnvTicTacToe;

public class TicTacToeRunner extends Runner{
    /*
     * Le constructeur lance ProB sur la machine tictac.mch
     * ensuite initialise les variables 
     */
    public TicTacToeRunner(){
        super("/TicTacToe/tictac.mch") ;
        this.initialise();
    } 

    /*
     * La méthode exec donne un exemple d'exécution aléatoire de transitions
     * A chaque étape on affiche l'état de la grille avec la méthode
     * prettyPrintTicTacToe. Cette dernière lit l'état de la machine B
     * et l'affiche de manière plus jolie et compréhensible
     */
    public void execSequence() throws Exception {
//        TODO : Implémenter l'utilisation des tests
//        Env envUsed = createEnv("iterValue");
//        Env envUsed = createEnv("iterPolicy");
//        Env envUsed = createEnv("iterPolicyWithImprovement");
//        envUsed.getAgent().learn();

        String win1, win0 ;
        win1 = state.eval("win(1)").toString() ;
        win0 = state.eval("win(0)").toString() ;

        while (
                win1.equals("FALSE") 
                & win0.equals("FALSE")
                & state.getOutTransitions().size() != 0
        ) {
			state = state.anyOperation(null).explore();
            win1 = state.eval("win(1)").toString() ;
            win0 = state.eval("win(0)").toString() ;
            this.prettyPrintTicTacToe() ;

            System.out.println("\nwin(1) " + win1);
            System.out.println("win(0) " + win0 + "\n");
		}
    }

    private void prettyPrintTicTacToe(){
        String input = state.eval("square").toString() ;

        // Grille 3x3 (remplie avec ' ' pour les cases vides)
        String[][] board = {{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};

        // Extraction des valeurs, Supprime tout sauf chiffres et séparateurs
        input = input.replaceAll("[^0-9↦,]", ""); 
        String[] entries = input.split(",");

        for (String entry : entries) {
            String[] parts = entry.split("↦");
            int row = Integer.parseInt(parts[0]) - 1; // Convertir en index (0-2)
            int col = Integer.parseInt(parts[1]) - 1;
            String value = parts[2];  // Garder la valeur en texte (0 ou 1)
            board[row][col] = value;  // Remplir la grille
        }

        // Affichage de la grille
        for (int i = 0; i < 3; i++) {
            System.out.println(" " + board[i][0] + " | " + board[i][1] + " | " + board[i][2]);
            if (i < 2) System.out.println("---+---+---");
        }
    }

    /**
     * Crée un environnement avec le type d'algo donné et le retourne
     * @param typeAlgo type de stratégie à utiliser par l'agent de l'environnement (l'agent est modifiable après création de l'environnement) :
     *                 - "iterValue" => choix de la stratégie epsilon greedy
     *                 - "iterPolicyImprovement" => choix de la stratégie Iterative Policy Evaluation avec amélioration (nécessite une politique déterministe)
     *                 - "iterPolicy" => choix de la stratégie Iterative Policy Evaluation (sans amélioration, et avec une politique uniforme ou e-greedy)
     *                 - une autre chaîne => IllegalArgumentException
     * @return Environnement créé
     */
    private Env createEnv(String typeAlgo) {
        return new EnvTicTacToe(typeAlgo, state, 1000);
    }
}
