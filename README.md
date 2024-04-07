# Application MES DÉPUTÉS

## Prérequis
- Android Studio
- **Min. SDK** : API 30 ("R", Android 11)

## Installation

1. Cloner le dépot

```

git clone https://github.com/dbslm31/MesDeputes.git

```

2. Ouvrir le projet dans Android Studio
3. Build le projet
4. Ouvrir sur un émulateur ou un appareil Android

## API externes
Utilisation de l'API [https://www.nosdeputes.fr](https://www.nosdeputes.fr/recherche/) pour récupérer les JSON nécessaires à l'execution de l'application.

Le format est lee suivant `https://nosdeputes.fr/nom-de-la-page/` + `?format=json` 

## Fonctionnalités

1. ### Recherche de députés par nom ou par ville
   
Un input de recherche permet de d'effectuer une recherche par nom ou par ville afin d'obtenir la liste des députés d'une circonscription en particulier ou de trouver des informations sur l'activité parlementaire d'un député en particulier.


2. ### Affichage des détails de chaque députés

Affiche les informations concernant l'activité parlementaire d'un député sur la période 2017 - 2022 : son nom, sa circonscription, son parti politique, ses informations de contact, ses responsabilités et ses derniers votes.

> [!NOTE] 
> L'affichage des responsabilités a été limité à 2 ; celui des votes à 3 dans un soucis d'optimisation du temps de chargement du MVP

## Captures d'écran
![Screenshot of the search feature](https://i.ibb.co/V9G9w6c/Capture-d-e-cran-2024-04-07-a-09-15-29.png) ![Screenshot of the deputy activity](https://iili.io/JObSnLP.png)




