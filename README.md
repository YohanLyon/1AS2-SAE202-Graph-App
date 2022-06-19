# SAE 202 - Coloration challenge 

## Présentation 

Cette SAE a pour objectif de proposer un défi de coloration de graphes, en utilisant certaines des méthodes vues en cours. Le projet sera découpé en deux parties: coloration et modélisation.

**Coloration**

Pour un graphe donné, un nombre de couleurs $k_{max}$ maximum sera défini. Par exemple, si $k_{max}=5$, cela signifie que vous ne pourrez colorier le graphe qu'avec les couleurs $0,1,\ldots,4$. Bien entendu, dans certains cas, il ne sera pas possible de proposer une coloration légale avec $k_{max}$ couleurs. Votre objectif sera donc de **colorier le graphe en minimisant le nombre de conflits**. Pour rappel, un conflit est une arête dont les deux extrémités sont coloriées avec la même couleur. Dans le cas où vous réussissez à colorier le graphe sans conflit, cela signifie que vous avez trouvé une coloration légale. 

Vous coderez un ou plusieurs algorithmes de coloration de graphe qui devront s'exécuter dans un temps imparti le jour de l'évaluation. 

**Modélisation**

Certains des graphes que vous aurez à colorier sont issus d'une modélisation d'affectation de plans de vol d'avions. Vous devrez coder une fonction qui transforme des horaires de vols entre différents aéroports français, en un graphe de conflits.

Tous les graphes vous seront fournis dans des fichiers. 

Nous décrivons maintenant plus en détails les deux parties de ce projet.

## Coloration

### Import d'un fichier 

Un fichier graphe sera un fichier d'extension .txt formaté comme suit:

- ligne 1: nombre de couleurs maximum (noté $k_{max}$)
- ligne 2: nombre de sommets du graphe. 
- toutes les lignes suivantes décrivent les arêtes (une ligne par arête), avec les identifiants des 2 sommets extrémités séparés par un espace.

Pour la lecture de ces fichiers et leur chargement dans une instance ```Graph``` de graphstream, nous vous donnons la  fonction ci-dessous (déjà codée):
````Java
Graph charger_graphe(String nom_fichier);
/* reçoit un nom de fichier et charge le fichier ``Donnees/nom_fichier`` */
/* retourne le graphe correspondant */
/* Note: nom_fichier est affecté comme identifiant du graphe retourné */
````


Pour connaitre la valeur de $k_{max}$ de chaque graphe, il est stocké dans l'attribut dynamique ```nb_couleurs_max``` par la fonction de chargement.


Sous Claroline, nous vous donnons une base de tests de 20 graphes (Test.zip). Vous pouvez décompresser et ouvrir les fichiers pour voir à quoi ressemblent les graphes et ensuite les charger avec la méthode précédente (voir plus bas où placer vos fichiers de données).

### Algorithmes de coloration

On vous demande de coder une fonction avec le prototype ci-dessous. Elle stockera la coloration finale du graphe $g$ dans un attribut dynamique ```couleur_opt``` que vous ajouterez à tous les sommets du graphe.

````Java
int colorier(Graph g, int millisecondes);
/* colorie le graphe g avec au plus kmax couleurs en stockant votre coloration dans un attribut dynamique "couleur_opt" */
/* votre fonction doit s'exécuter en un temps inférieur au nombre de millisecondes*/
/* retourne le nombre de conflits détectés */
````

Cette fonction pourra appeler différents algorithmes de coloration, notamment ceux vus en cours de graphes. Nous en rappelons certains ci-dessous. Nous vous conseillons de coder les différents algorithmes de coloration dans des fonctions distinctes, et d'utiliser des noms d'attributs dynamiques différents pour chaque type de coloration. 

Dans la fonction ``colorier``, le nombre de millisecondes correspond au temps imparti pour colorier le graphe. Pour calculer le temps d'exécution (en millisecondes) d'une partie de votre code, il suffit d'appeler la fonction ```System.currentTimeMillis();``` au début et à la fin et de faire la différence entre les deux valeurs.

Testez vos différents algos et expérimentez par rapport à la base de tests pour voir ceux qui semblent fonctionner le mieux. Votre méthode ```colorier``` choisira d'appeler vos différentes fonctions de façon optimisée et retournera le nombre de conflits dans votre meilleure coloration (càd celle stockée dans ```couleur_opt```). Pour cela, nous vous fournissons une fonction calculant le nombre de conflits d'un graphe.

````Java
int compte_nb_conflits(Graph g, String attribut);
/* attribut: l'attribut de coloration à vérifier sur le graphe */
/* retourne le nombre de conflits dans le graphe g */
````

Attention, cette fonction parcourt toutes les arêtes donc à utiliser avec parcimonie.


#### Coloration aléatoire

Pour commencer facilement, vous pouvez faire une fonction qui colorie les sommets aléatoirement entre 0 et $k_{max}-1$. Sur les graphes de la base de test, le nombre de conflits pour ce type de coloration devrait être élevé.

#### Coloration Welsh Powell

Vous avez découvert cet algorithme en cours de graphes. Il est déjà implémenté sous GraphStream et vous trouverez la documentation [ici](https://graphstream-project.org/doc/Algorithms/Welsh-Powell/).

Attention, par défaut il utilise un nombre de couleurs qui n'est pas forcément inférieur ou égal à $k_{max}$. Pour les couleurs de valeur supérieure à $k_{max}$, vous les remplacerez par des couleurs inférieures (ce qui engendrera des conflits, ce qui est normal).

#### Coloration DSAT

Idem que pour Welsh-Powell sauf que l'algorithme n'est pas implémenté dans GraphStream. Nous vous le fournissons dans nos archives et vous l'utiliserez de la même façon que Welsh Powell.

#### Descente

La méthode de descente consiste à parcourir tous les voisins d'une coloration et à prendre la meilleure, et de recommencer l'opération jusqu'à tomber dans un minimum local (c'est à dire où tous les voisins sont "pires" que la coloration courante). 

Pour le voisinage d'une coloration, vous pourrez choisir de remplacer la couleur d'un sommet par une autre couleur. Tester tous les voisins d'une coloration revient donc à tester tous les sommets, et pour chaque sommet, tester toutes les autres couleurs que sa couleur actuelle (entre 1 et $k_{max}$), et prendre la combinaison (sommet,couleur) qui a le moins de conflits. 

Pour la coloration de départ (avant de commencer la descente), vous pouvez opter pour une des colorations vues ci-dessus.

Dans votre fonction de coloration, nous vous conseillons de passer en paramètre le nombre de millisecondes restant et de vérifier régulièrement que vous ne dépassez pas le temps imparti.


#### Recuit simulé

Vous pouvez coder un recuit simulé comme vu en cours de graphes. Votre fonction $risque_t$ pourra être de la forme $risque_t=\lambda^t * T_0$, avec $\lambda$ et $T_0$ qui seront des paramètres que vous ferez varier en essayant de choisir les meilleures valeurs possibles. Là encore, votre fonction pourra prendre comme paramètre le temps restant pour savoir quand arrêter les itérations.


Pour $T_0$, essayez des valeurs entre 30 et 1000. Pour $\lambda$, cela peut être des valeurs entre 0.5 et 0.99.


Dans les manières d'optimiser votre algorithme, vous pourrez arrêter le recuit actuel et en recommencer un nouveau si vous constater qu'il n'y a pas eu d'amélioration depuis un moment (à vous de le définir).

Par ailleurs, dans votre algorithme vu en cours, un paramètre supplémentaire pourra être de rajouter une boucle de taille constante $l$ (par ex. $l=20$ ou $30$) pendant laquelle $t$ ne varie pas (c'est à dire que vous ne faites pas t=t+1 à chaque itération mais seulement toutes les $l$ itérations). Dans votre algorithme vu en cours, cela correspond à $l=1$. Mais vous pouvez essayer avec d'autres valeurs pour voir si cela fonctionne mieux.

#### Méthode à compléter

Dans la classe ``GroupeX``, vous devez compléter la méthode ``public void challenge(String prefixeFichiers, Integer nbFichiers, Long millisecondes)`` qui devra calculer les colorations pour les graphes donnés. Cette fonction
- reçoit en entrée 3 paramètres
    - ``String prefixeFichiers``: vous aurez différents fichiers à traiter, notés ``prefixeFichiersY.csv``, $0\leq Y < nbFichiers$. Nous vous fournissons plusieurs fichiers pour tester votre fonction (notés ``colo-test0.txt, colo-test1.txt,...`` donc ``prefixeFichiers="colo-test"``). Le jour de l'évaluation, nous vous fournirons d'autres fichiers dont le préfixe sera différent. Voir ci-dessus pour le détail des fichiers de données en entrée.
    - ``Integer nbFichiers``: le nombre de fichiers à traiter (les indices des fichiers débuteront à 0).
    - ``Long millisecondes``: le temps maximum pour calculer les colorations et créer les fichiers résultats.
- crée en sortie, plusieurs fichiers:
    - un fichier csv dans le dossier ``Resultats`` du projet, nommé ``coloration-groupeX.csv`` contenant sur chaque ligne les informations suivantes: ``nomFichier.txt;nbre conflits``
    - pour chaque fichier traité, un fichier contenant toute la coloration de votre graphe (noeud par noeud). On vous fournit une fonction ``void sauver_coloration(Graph g, String attribut, int numero_groupe)`` qui crée ce fichier dans le dossier ``Resultats`` du projet. La fonction écrit la valeur de l'``attribut`` de chaque noeud du graphe ``g`` dans un fichier automatiquement nommé ``groupeX-coloration-nomFichier.txt``, avec ``X`` le ``numero_groupe`` et le nom du fichier stocké dans l'identifiant du graphe (voir ``charger_graphe``).


Le jour du rendu, les fichiers seront nommés ``colo-eval0.txt``...``colo-eval19.txt``.
Vous aurez **400 secondes** au total pour les 20 instances.


## Modélisation

Le calcul d'une coloration de graphe efficace peut être appliqué à des problèmes concrets. Nous prendrons ici un problème de gestion du trafic aérien. A partir de l'ensemble des vols d'un espace aérien donné, votre objectif est de minimiser le nombre de niveaux d'altitude nécessaires pour éviter les collisions en plein vol. Le problème global se décomposerait alors en plusieurs parties:
- *modélisation du problème*: à partir de la liste des aéroports et des vols, on construit un graphe chargé de modéliser les collisions éventuelles entre deux vols (deux vols susceptibles de se croiser en temps et en espace). Ainsi les sommets représenteront les vols, et il y aura une arête entre deux vols susceptibles d'entrer en collision. 
- *résolution du problème*: les algorithmes de coloration implémentés dans la première partie permettent de résoudre ces collisions (du moins minimiser les collisions possibles afin d'aider l'opérateur dans sa recherche de solutions). En effet, deux vols susceptibles d'entrer en collision devront avoir des couleurs différentes (puisqu'ils sont reliés dans la modélisation). Chaque couleur représentera donc un niveau d'altitude pour les avions. Deux vols susceptibles de se croiser en temps et en espace se situent à des hauteurs différentes. Nous utilisons des algorithmes visant à minimiser le nombre de couleurs car nous cherchons à limiter le nombre de niveaux d'altitude.

Dans cette seconde partie du projet, on ne traitera que la génération des graphes (la partie coloration aura ses propres jeux de tests). Pour cela, nous vous fournirons plusieurs jeux de tests (plusieurs fichiers) que vous devrez importer afin de créer les graphes de collisions correspondants. Pour évaluer si les graphes générés sont corrects, plusieurs caractéristiques des graphes seront calculées (un code calculant ces caractéristiques sera fourni).

![](https://codimd.math.cnrs.fr/uploads/upload_c3c358e313e7e21d80653505ad3062c9.png)


Dans notre projet, nous allons considérer l'espace aérien français (France métropolitaine), de sorte à rendre négligeable la forme sphérique de la Terre (on considèrera les villes dans un plan 2D). Cela permet également de ne pas prendre en compte les décalages horaires dans vos calculs.

Lorsque nous allons coder notre modèle, l'essentiel est de savoir si deux vols donnés sont susceptibles d'entrer en collision ou pas. Pour détecter ceci, vous aurez tout d'abord besoin de trouver les coordonnées cartésiennes de chaque aéroport dans le plan. La manipulation est la suivante: 
- convertissez tout d'abord longitude et latitude en degrés décimaux: $valeur=coef \times (deg + minutes/60 + secondes/3600)$ avec $coef=1$ si orientation={N ou E} et $coef=-1$ si orientation={S ou O} (cf. Wikipédia – système sexagésimal),
- puis convertissez cette valeur en radians,
- pour trouver les coordonnées (x,y) de l'aéroport en fonction de la latitude et longitude en radians, vous utiliserez ces formules:
$x=R\times \cos(latitude)\times \sin(longitude)$
$y=R\times \cos(latitude)\times \cos(longitude)$
où $R$ est le rayon de la terre (vous prendrez 6371 km).

Une fois que vous avez réussi à obtenir les coordonnées cartésiennes des aéroports, vous considérerez les vols comme des segments de droite dans le plan. Vous  négligerez les temps de décollage et d'atterrissage, et supposerez donc que les avions volent à vitesse constante entre le départ et l'arrivée.

Deux vols seront susceptibles d'entrer en collision si:
- les segments de droite associés s'intersectent  dans le plan **et**
- l'écart entre les horaires de passage théoriques de chacun des deux avions au point d'intersection n'excède pas 15 minutes (<15mn).


**Exemple** (données du fichier ``model-test0.csv``)
Les 2 vols sont
AF000090;MRS;BES;7;33;81
AF000132;LYS;BOD;7;34;47 

Ces données correspondent à
vol n°AF000090, départ Marseille, arrivée Brest, départ à 7h33, durée du vol 81 mn
vol n°AF000132, départ Lyon, arrivée Bordeaux, départ à 7h34, durée du vol 47 mn

Les coordonnées cartésiennes des aéroports sont
MRS 43°26'8''N=43.4355556°, 5°12'49''E=5.21361111°, soit (420.387068;4607.15017)
BES 48°26'52''N=48.4477778°, 4°25'6''O=4.41833333°, soit (-325.555103;4213.34092)
LYS 45°43'35''N=45.7263889°, 5°5'27''E=5.09083333°, soit (394.648936;4429.95907)
BOD 44°49'42''N=44.8283333°, 0°42'56''O=0.715555556°, soit (-56.4286114;4518.10228)

Les 2 segments intersectent en X, de coordonnées (168.472108;4474.15535).

Le premier vol arrive au point X à 8h00 et le second vol arrive au point X à 7h57. Les 2 vols sont en risque de collision. Les sommets correspondants à ces vols dans le graphe seront reliés par une arête.


### Codage de la modélisation

#### Code fourni

Avant de voir la fonction à coder, nous vous fournissons plusieurs éléments:
- des fichiers de données
    - le fichier ``aeroports.csv`` contient les informations de tous les aéroports français sous le format suivant: ``code_aéroport ; nom de la ville ; latitude (degrés) ; latitude (minutes) ; latitude (secondes) ; latitude (hémisphère N/S) ; longitude (degrés) ; longitude (minutes) ; longitude (secondes) ; longitude (orientation E/O)``
    - les fichiers de vols (notés ``model-testY.csv``, avec $0\leq Y\leq 9$) contiennent les informations de vols permettant de créer les graphes des collisions. Ces données sont au format: ``identifiant du vol ; code_aéroport départ ; code_aéroport arrivée ; heure départ ; minutes départ ; durée du vol (en minutes)``
- une méthode qui calcule différentes caractéristiques d'un graphe. Cette méthode est donnée dans la librairie ``SAE202_Lib.jar`` (dont la documentation est fournie) et son prototype est ``public static HashMap<String, Object> getCaracteristiques(Graph graphe)``. La fonction
    - reçoit en entrée un ``Graph`` (classe de ``GraphStream``)
    - fournit en sortie une ``HashMap<String,Object>`` contenant les résultats des différents paramètres. La clé est de type ``String`` (le nom du paramètre, attention à l'orthographe et à la casse quand vous les récupèrerez), la valeur est de type ``Object`` car les résultats seront soit des ``int`` soit des ``double`` (vous casterez la valeur dans le bon type pour la récupérer). Les paramètres (et donc leurs noms) sont les suivants:
        - ``nbNoeuds``, de type ``int``: donne le nombre de sommets (ou noeuds) du graphe,
        - ``nbAretes``, de type ``int``: donne le nombre d'arêtes du graphe,
        - ``degreMoyen``, de type ``double``: donne le degré moyen du graphe,
        - ``nbComposantes``, de type ``int``: donne le nombre de composantes connexes du graphe (nombre de composantes connexes = nombre de morceaux de graphes distincts),
        - ``diametre``, de type ``int``: donne le diamètre du graphe (dans un graphe, la *distance* entre 2 sommets est le plus court chemin entre ces sommets; le diamètre d'un graphe est la plus grande distance entre 2 sommets du graphe).

A noter, la méthode de calcul des caractéristiques est ``static``. Il est donc possible de l'appeler directement: ``CaracteristiquesGraphe.getCaracteristiques(g);`` (où ``g`` est votre graphe).

#### Méthode à compléter

Dans la classe ``GroupeX``, vous devez compléter la méthode ``public void modelisation(String prefixeFichiers, Integer nbFichiers, Long millisecondes)`` qui devra calculer 10 graphes à partir de 10 fichiers de vols à traiter. Cette fonction
- reçoit en entrée 2 paramètres
    - ``String prefixeFichiers``: vous aurez différents fichiers à traiter, notés ``prefixeFichiersY.csv``, avec $0\leq Y \leq 9$. Nous vous fournissons 10 fichiers pour tester votre fonction (notés ``model-test0.csv, model-test1.csv,..., model-test9.csv``, donc ``prefixeFichiers="model-test"``). Le jour de l'évaluation, nous vous fournirons d'autres fichiers dont le préfixe sera différent. Voir ci-dessus pour le détail des fichiers de données en entrée.
    - ``Integer nbFichiers``: le nombre de fichiers à traiter (les indices des fichiers débuteront à 0).
    - ``Long millisecondes``: le temps maximum pour calculer les 10 graphes et créer le fichier résultat.
- crée en sortie, un fichier csv dans le dossier ``Resultats`` du projet, nommé ``modelisation-groupeX.csv`` contenant sur chaque ligne les informations suivantes:
``groupeX;nomFichier.csv;nbNoeuds;nbAretes;degreMoyen;nbComposantes;diametre``

Ci-dessous, l'export du fichier que vous devriez obtenir pour les 10 fichiers de tests fournis:
groupe0;model-test0.csv;2;1;1.0;1;1
groupe0;model-test1.csv;2;0;0.0;2;0
groupe0;model-test2.csv;5;5;2.0;1;3
groupe0;model-test3.csv;12;17;2.8333332538604736;1;4
groupe0;model-test4.csv;20;36;3.5999999046325684;1;6
groupe0;model-test5.csv;24;23;1.9166666269302368;3;5
groupe0;model-test6.csv;279;426;3.0537633895874023;4;27
groupe0;model-test7.csv;349;593;3.3982808589935303;4;23
groupe0;model-test8.csv;605;1299;4.294214725494385;1;46
groupe0;model-test9.csv;333;934;5.609609603881836;20;9

représentant les résultats suivants
```csvpreview {header="true"}
Fichier,nbNoeuds,nbAretes,degreMoyen,nbComposantes,diametre
model-test0.csv,2,1,1.0,1,1
model-test1.csv,2,0,0.0,2,0
model-test2.csv,5,5,2.0,1,3
model-test3.csv,12,17,2.8333332538604736,1,4
model-test4.csv,20,36,3.5999999046325684,1,6
model-test5.csv,24,23,1.9166666269302368,3,5
model-test6.csv,279,426,3.0537633895874023,4,27
model-test7.csv,349,593,3.3982808589935303,4,23
model-test8.csv,605,1299,4.294214725494385,1,46
model-test9.csv,333,934,5.609609603881836,20,9
```

L'*algorithme* de votre fonction sera donc:
- charger le fichier ``aeroports.csv`` (voir ci-dessous)
- Pour ``Y`` de 0 à 9
    - construire un graphe $G$ à partir du fichier de vols ``prefixeFichiersY.csv``
    - calculer les caractéristiques du graphe via la méthode ``CaracteristiquesGraphe.getCaracteristiques(G);``
- Ecrire le fichier de sortie contenant toutes les caractéristiques de tous les graphes.


Le temps maximum pour générer les 10 graphes et créer le fichier résultat est de **10 secondes**.



Pour information, une image des graphes de tests pour la modélisation sont donnés sur Claco.
**Attention** Le jour de l'évaluation, votre programme **ne doit pas** afficher les graphes.


## Contraintes de programmation

Pour chacune des parties du projet, vous devez fournir un fichier contenant le résultat de votre algorithme. Cependant, vos codes pourront être récupérés et exécutés. Cela demande quelques contraintes dans vos programmes:
- A partir du squelette fourni, vous devez
    - renommer le dossier ``Groupes.GroupeX`` en remplaçant le ``X`` par votre numéro de groupe (ne pas introduire de 0 inutiles: pour le groupe 1 par exemple, le dossier se nommera ``Groupes.Groupe1``) (pensez à utiliser le refactoring: clic droit -> Refactor -> Rename),
    - renommer la classe ``GroupeX`` en remplaçant le ``X`` par votre numéro de groupe (ne pas introduire de 0 inutiles: pour le groupe 1 par exemple, la classe se nommera ``Groupe1``) (pensez à utiliser le refactoring: clic droit -> Refactor -> Rename).
- Vous devez compléter les méthodes de la classe ``GroupeX`` (c'est-à-dire ``public void challenge(String, Integer, Long)`` et ``public void modelisation(String, Integer, Long)``) car ce sont elles qui seront exécutées lors des tests.
- Toutes les classes que vous créerez **devront être dans le dossier ``Groupes.GroupeX``**. Lors du transfert de votre code, seul ce dossier sera copié. Si vous codez des classes hors de ce dossier, elles ne seront plus reconnues. Le code ne pourra pas être testé (et donc non évalué).
- Vous ne devez avoir que du code qui fonctionne
    - si le code ne compile pas, il ne pourra pas être testé (et donc non évalué),
    - si le code est trop long, il sera interrompu. Si aucun fichier résultat n'est écrit, les résultats ne pourront pas être évalués.
- Les fichiers de données sont à placer dans le dossier ``Donnees`` et les fichiers générés doivent être placés dans le dossier ``Resultats`` (voir en-dessous). Lors de l'exécution de votre code dans le projet contenant l'évaluation, les fichiers seront lus et récupérés dans ces dossiers.


Avec ces contraintes, votre programme principal pourrait ressembler à ceci:
````java
GroupeX obj=new GroupeX();
obj.challenge("colo-test",11,Long.valueOf(600000));
obj.modelisation("model-test",10,Long.valueOf(10000));
````

## A rendre

- Le projet zippé avec un .jar dans le dossier dist.
- Une javadoc dans le dossier correspondant du projet.
- les fichiers ``csv`` et ``txt`` décrits dans les 2 parties précédentes:
    - Coloration: 1 fichier ``csv`` regroupant les résultats pour l'ensemble des fichiers à tester + 1 fichier ``txt`` par fichier à tester contenant la coloration obtenue,
    - Modélisation: 1 fichier ``csv`` regroupant toutes les caractérisitques de tous les graphes générés.

## Structure du projet et bilan sur les méthodes données et à compléter

Vous trouverez sur Claco le squelette d'un projet Netbeans comportant différents éléments. Le fichier à décompresser est ``SAE202_etu.zip``.

### Structure du dossier du projet

![](https://codimd.math.cnrs.fr/uploads/upload_403aec311773e63dd2b77d10fcb15ec5.png)

**1**: le dossier à renommer contenant tout votre code.
**2**: le dossier contenant toutes les données. Les fichiers de données que l'on vous donne seront à placer dans ce dossier.
**3**: le dossier contenant les résultats de vos traitements.
**4**: le dossier contenant toutes les librairies fournies (GraphStream, les fonctions déjà codées, la javadoc des fonctions codées).
**5**: le dossier qui sera créé lors de génération de votre fichier jar (Netbeans -> Clean & Build) et de votre documentation (Netbeans -> générer javadoc).

Exemple de ce que vous devriez avoir dans le dossier ``Donnees`` en début de programme et dans le dossier ``Resultats`` en fin de programme

![](https://codimd.math.cnrs.fr/uploads/upload_7bf293f0bdc24c8b95088eceffa303cb.png)


### Structure du projet Netbeans

![](https://codimd.math.cnrs.fr/uploads/upload_fa0b82c5fccc89805d7bb10f152818bf.png)

**1** : renommez le dossier pour remplacer ``X`` par votre numéro de groupe (utilisez Refactor). **Attention**, toutes vos classes doivent être dans ce dossier.
**2**: renommez la classe pour remplace ``X`` par votre numéro de groupe (utilisez Refactor). 
**3**: librairie contenant 2 classes (voir javadoc de la librairie, donnée dans le dossier ``mesLibrairies``)
- classe ``SAE202_Algos``, contient des fonctions utilisables pour votre travail:
    - ``public static Graph charger_graphe(String nom_fichier)``: fonction qui charge un graphe à partir d'un fichier. Le fichier est chargé depuis le dossier ``Donnees`` (c'est-à-dire, charge le fichier ``Donnees/nom_fichier``).
    - ``public static int compte_nb_conflits(Graph graphe, String attribut)``: fonction qui compte le nombre de conflits dans un graphe (conflit = 2 sommets voisins avec la même valeur d'attribut).
    - ``public static void sauver_coloration(Graph graphe, String attribut, int numero_groupe)``: fonction qui sauvegarde la coloration d'un graphe (c'est-à-dire l'``attribut`` spécifié) dans un fichier texte directement dans le dossier ``Resultats`` (c'est à dire, crée un fichier ``Resultats/groupeX-coloration-nom_fichier.txt``) 
    - ``public static HashMap<String, Object> calcul_caracteristiques(Graph graphe)``: fonction qui calcule des caractéristiques d'un graphe. La fonction retourne une ``HashMap<String,Object>`` contenant les résultats: "nbNoeuds" (en int), "nbAretes" (en int), "degreMoyen" (en double), "nbComposantes" (en int), "diametre" (en int). Si une caractéristique ne peut pas être calculée, elle est mise à une valeur par défaut (-1).
- interface ``SAE202_Interface``, contient les 2 méthodes que votre classe ``GroupeX`` implémente:
    - ``public void challenge(String prefixeFichier, Integer nbFichiers, Long millisecondes);`` La méthode de challenge de coloration. La fonction doit écrire plusieurs fichiers de sortie: un fichier ``csv`` donnant le nombre de conflits pour chaque fichier traité, un export de la coloration de chaque fichier. Les paramètres sont
        - ``prefixeFichier``: le préfixe des fichiers à tester,
        - ``nbFichiers``: nombre de fichiers à traiter (le premier fichier sera ``prefixeFichier0``)
        - ``millisecondes``: temps alloué pour traiter tous les fichiers
    - ``public void modelisation(String prefixeFichier, Integer nbFichiers, Long millisecondes);`` La méthode construit les graphes de conflits de vols à partir de fichiers de données. Les paramètres sont les mêmes que pour la fonction précédente.
- classe ``DSat`` contient le code permettant de colorer un graphe avec l'algorithme DSAT (ou DSATUR). L'algorithme s'utilise comme l'algorithme Welsh Powell:
````Java
String attribut="nom_coloration";
Grapj g= new ...
DSat ds = new DSat(attribut);
ds.init(g);
ds.compute();
````
 
**4**: librairies Graphstream

## Evaluation

Une partie de l'évaluation de la SAE S202 se fera dans la ressource R207 (Graphes). Concernant ce projet, la répartition des points se fera ainsi:
- Coloration, 13 points. Les points seront attribués en fonction des classements des groupes (meilleur classement = plus de points). Le classement se fait sur le nombre de conflits calculés. Tous les groupes classés ont des points. Les groupes non évalués ne sont pas classés.
- Modélisation, 5 points. Les points sont attribués en fonctions des caractéristiques calculées (pas de challenge entre les groupes).
- Documentation Java, 2 points. 

