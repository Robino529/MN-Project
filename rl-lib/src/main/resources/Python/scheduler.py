from typing import Set, Any, List, Tuple
import random

# Définition de l'ensemble PID (processus)
class PID:
    PROCESS1 = "process1"
    PROCESS2 = "process2"
    PROCESS3 = "process3"

    @staticmethod
    def all() -> Set[str]:
        """ Retourne l'ensemble des processus possibles """
        return {PID.PROCESS1, PID.PROCESS2, PID.PROCESS3}

# Classe représentant la MACHINE B "scheduler"
class Scheduler:
    def __init__(self):
        """ Initialise les variables comme dans l'INITIALISATION en B """
        self.active: Set[str] = set()
        self.ready_processes: Set[str] = set()
        self.waiting: Set[str] = set()

    def new(self, pp: str):
        """ Ajoute un processus à la file d'attente `waiting` s'il n'existe pas ailleurs """
        if pp in PID.all() and pp not in self.active and pp not in self.ready_processes and pp not in self.waiting:
            self.waiting.add(pp)

    def del_process(self, pp: str):
        """ Supprime un processus de `waiting` """
        if pp in self.waiting:
            self.waiting.remove(pp)

    def ready(self, rr: str):
        """ Déplace un processus de `waiting` vers `ready_processes`, ou l'active si aucun processus n'est actif """
        if rr in self.waiting:
            self.waiting.remove(rr)
            if not self.active:  # Si aucun processus actif, on l'active
                self.active.add(rr)
            else:
                self.ready_processes.add(rr) 

    def swap(self):
        """
        Effectue un échange :
        - Déplace le processus actif vers `waiting`
        - Active un processus de `ready_processes` si disponible
        """
        if self.active:
            # Déplacer l'actif vers `waiting`
            self.waiting.update(self.active)
            self.active.clear()

            if self.ready_processes:
                # Convertir en liste et mélanger pour garantir une pseudo-aléatorisation
                ready_list = list(self.ready_processes)
                random.shuffle(ready_list)
            
                # Sélectionner un processus aléatoire
                pp = ready_list.pop()
                self.active.add(pp)
                self.ready_processes.remove(pp)

    def get_state(self) -> dict:
        """ Retourne l'état actuel du scheduler """
        return {
            "active": set(self.active),
            "ready": set(self.ready_processes),  
            "waiting": set(self.waiting)
        }

    def get_available_actions(self) -> List[Tuple[str, Any]]:
        """ Détermine la liste des actions possibles en fonction de l'état actuel """
        actions = []

        # On peut ajouter un nouveau processus si un processus de PID n'est pas dans active, ready ou waiting
        for process in PID.all():
            if process not in self.active and process not in self.ready_processes and process not in self.waiting:
                actions.append(("new", process))

        # On peut passer un processus de waiting à ready
        for process in self.waiting:
            actions.append(("ready", process))

        # On peut supprimer un processus de waiting
        for process in self.waiting:
            actions.append(("del_process", process))

        # On peut swap si un processus est actif
        if self.active:
            actions.append(("swap",))

        # On peut toujours afficher l'état
        actions.append(("get_state",))

        return actions

# Fonction pour exécuter dynamiquement une action
def execute_action(class_instance: Any, method_name: str, *args):   
    method = getattr(class_instance, method_name, None)

    if not callable(method):
        raise AttributeError(f"La méthode '{method_name}' n'existe pas dans {class_instance.__class__.__name__}")

    return method(*args)

# Création d'une instance du scheduler
scheduler = Scheduler()

# Liste des actions à tester
actions = [
    ("new", PID.PROCESS1),      # Ajouter process1 dans waiting
    ("new", PID.PROCESS2),      # Ajouter process2 dans waiting
    ("ready", PID.PROCESS1),    # Déplacer process1 de waiting à active
    ("new", PID.PROCESS3),      # Ajouter process3 dans waiting
    ("ready", PID.PROCESS2),    # Déplacer process2 de waiting à ready_processes
    ("ready", PID.PROCESS3),    # Déplacer process3 de waiting à ready_processes
    ("swap",),                  # Échange des processus
    ("get_state",)              # Récupérer l'état actuel
]

# Exécution dynamique des actions avec affichage des actions possibles
for action, *params in actions:
    try:
        result = execute_action(scheduler, action, *params)
        if result is not None:
            print(f"Action: {action}({', '.join(map(str, params))}) → Résultat: {result}")
        else:
            print(f"Action: {action}({', '.join(map(str, params))}) exécutée.")

        # Affichage des actions possibles après cette exécution
        available_actions = scheduler.get_available_actions()
        print(f"Actions possibles après '{action}': {[f'{a[0]}({a[1]})' if len(a) > 1 else f'{a[0]}()' for a in available_actions]}")

    except AttributeError as e:
        print(f"ERREUR: {e}")

# Vérification finale de l'état du scheduler
print("État final du scheduler:", scheduler.get_state())
