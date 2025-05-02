---
title: "Rapport"
author: Robin Courault, Julien Floyd
geometry: "margin=2cm"
output: pdf_document
---

# Introduction

L'objectif de ce projet était d'aborder le machine learning en se servant de 
différents algorithmes d'apprentissage dans différentes situations. 

# Première Partie : trois algorithmes de base


## Choix d'implémentations
- general:

Nous avons décidé de poser le nombre d'itérations maximales à 10 000 pour que le
programme ne prenne pas trop de temps si la convergence n'est pas atteinte rapidement.
Suivant le même raisonnement, la limite de convergence est placée à 0.00 001 dans 
chaque cas et le programme s'arrête dès que les modifications apportées par
l'apprentissage sont inférieurs à cette valeur.

- $\epsilon$-greedy:

L'implémentation suit pas à pas l'algorithme, le taux d'apprentissage choisi est 0.2,
le facteur discount est fixé à 0.9, et le facteur d'exploration à 0.2.
- UCB

Puisque UCB fonctionne de manière similaire à $\epsilon$-greedy (seul le choix d'action
et l'attribution de récompenses diffèrent) nous avons décidé d'étendre la classe qui
gère $\epsilon$-greedy et d'override la méthode qui gère le choix d'action et celle qui 
gère les récompenses pour les remplacer par celle qui correspondent à UCB. 

- Bandit-Gradient

l'implémentation suit, elle aussi, pas à pas l'algorithme. les baseline reward sont
fixée à 0, et le taux d'apprentissage lui à 0.2.

## Analyse des algorithmes

- Le premier cas : l'environnement simple

Pour le cas simple, le choix de film de l'utilisateur est constant, c'est pour cette
raison qu'après avoir implémenté les trois algorithmes. Il semblerait que pour le cas
simple, il est plus éfficace de choisir $\epsilon$-Greedy, car il fournit des
résultats corrects et qu'il est plus simple à implémenter que les autres.

 - Le deuxième cas : Youtube

Dans ce cas les préférences de l'utilisateur varient au cours du temps, l'algorithme le 
plus performant dans ce cas est Bandit Gradient, car ce dernier s'adapte bien au 
changement de préférence contrairement aux deux autres.







