from enum import Enum
from typing import Any

# Définition des films comme une énumération
class Films(Enum):
    A = "A"
    B = "B"
    C = "C"

# Classe contenant les opérations
class FilmOperations:
    
    def choose(self, film: Films) -> str:
        """
        Simule l'opération `choose(film)` de B en Python.
        
        :param film: Un élément de l'énumération Films.
        :return: "OK" si film est A ou B, "KO" si film est C.
        """
        if not isinstance(film, Films):
            raise ValueError("film doit être une valeur de l'énumération Films")
        if film == Films.A or film == Films.B:
            return "OK"
        elif film == Films.C:
            return "KO"
        else:
            raise ValueError("Film non reconnu")

# Fonction pour exécuter dynamiquement une action
def execute_action(class_instance: Any, method_name: str, *args):
    """
    Exécute dynamiquement une méthode d'une instance de classe.
        :param class_instance: L'instance de la classe sur laquelle exécuter la méthode.
        :param method_name: Le nom de la méthode à appeler.
        :param args: Les arguments à passer à la méthode.
        :return: Le résultat de l'exécution de la méthode.
    """
    method = getattr(class_instance, method_name, None)
    
    if not callable(method):
        raise AttributeError(f"La méthode '{method_name}' n'existe pas dans {class_instance.__class__.__name__}")
    
    return method(*args)

# Création de l'instance de la classe
film_operations = FilmOperations()

# Sequence d'actions à tester
actions = [
    ("choose", Films.A),
    ("choose", Films.B),
    ("choose", Films.C),
]

# Exécution des actions dans une boucle
for action_name, param in actions:
    result = execute_action(film_operations, action_name, param)
    print(f"Action: {action_name}({param.value}) → Résultat: {result}")
