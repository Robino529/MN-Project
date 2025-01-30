* Objectif : 

- L’agent doit apprendre à minimiser le temps d’attente des processus tout en maximisant l’utilisation du CPU.

- scheduler.mch : contient l'ordonnanceur

- scheduler_main.mch : utilise l'ordonnanceur en introduisant les notions de temps d'attente et temps d'exécution.

* La fonction de reward est à définir . Il faut justifier vos choix. Voici un exemple pour vous aider à démarrer, mais ce n'est pas le meilleur.

-- Positive (+1) si un processus est exécuté immédiatement après son arrivée.
-- Négative (-1) pour chaque unité de temps où un processus reste en attente.
-- Négative (-5) si active = {} (CPU inutilisé).
-- Bonus (+10) si un processus est terminé avec un faible temps d’attente (autour d'une moyenne).