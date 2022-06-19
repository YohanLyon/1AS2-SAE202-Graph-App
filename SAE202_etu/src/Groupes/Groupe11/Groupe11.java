/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Groupes.Groupe11;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.algorithm.coloring.WelshPowell;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import packMesClassesEtInterfaces.DSat;
import static packMesClassesEtInterfaces.SAE202_Algos.*;
import packMesClassesEtInterfaces.SAE202_Interface;


/**
 * Cette classe charge les différents graphes et implementes les différentes collorations possible
 * 
 */
public class Groupe11 implements SAE202_Interface{

    static int tempsTotal = 0;
    int conflitTotal = 0;
    static int TOTAL_SOMMETS = 0;

    /**
     * Cette fonction permet d'initialiser les différents graphes dans le dossier Donnees et leur attribuer une coloration pour chacun des algorythmes.
     * @param prefixeFichier Le prefix du nom de fichier
     * @param nbFichiers Le nombre de fichier à traiter
     * @param millisecondes Le temps minimum avant de terminer l'execution 
     */
    @Override
    public void challenge(String prefixeFichier, Integer nbFichiers, Long millisecondes) {

        ArrayList<Graph> graphes = new ArrayList<>();
        int[] nbSommets = new int[nbFichiers];
        TOTAL_SOMMETS = 0;
        tempsTotal = 0;
        conflitTotal = 0;
        for (int i = 0; i < nbFichiers; i++) {
            Graph current = charger_graphe(prefixeFichier + i + ".txt");
            graphes.add(current);
            nbSommets[i] = current.getNodeCount();
            TOTAL_SOMMETS += current.getNodeCount();
        }

        File file = new File("Resultats/coloration-groupe11.csv");
        if(!file.exists()) try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Groupe11.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            FileWriter fw = new FileWriter(file);
            fw.write("");
            fw.flush();
        } catch (IOException ex) {
            Logger.getLogger(Groupe11.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < nbFichiers; i++) {
            System.out.println(prefixeFichier + i);
            ExportCSV(file, prefixeFichier + i + ".txt", colorier(graphes.get(i), (int) (((float) (nbSommets[i]) / (float) (TOTAL_SOMMETS)) * millisecondes)));
        }

        System.out.println("TEMPS TOTAL: " + tempsTotal);
        System.out.println("CONFLIT MOYEN: " + ((double) (conflitTotal) / (double) (nbFichiers)));
        System.out.println("CONFLIT TOTAL: " + conflitTotal);

    }

    /**
     * Cette méthode initialise les couleurs à prendre pour chaque noeuds du
     * graphe en mettant la couleur dans l'attribut "ui.style".
     *
     * @param g Le graphe sur lequel la couleur doit être appliquée
     * @param attribut Attribut contenant la couleur des noeuds
     *
     */
    public static void colorierGraphe(Graph g, String attribut) {
        int max = g.getAttribute("nb_couleurs_max");
        Color[] cols = new Color[(int) (g.getAttribute("nb_couleurs_max")) + 1];
        for (int i = 0; i <= max; i++) {
            cols[i] = Color.getHSBColor((float) (Math.random()), 0.8f, 0.9f);
        }

        for (Node n : g) {
            int col = n.getAttribute(attribut);
            if (n.hasAttribute("ui.style")) {
                n.setAttribute("ui.style", "fill-color:rgba(" + cols[col].getRed() + "," + cols[col].getGreen() + "," + cols[col].getBlue() + ",200);");
            } else {
                n.addAttribute("ui.style", "fill-color:rgba(" + cols[col].getRed() + "," + cols[col].getGreen() + "," + cols[col].getBlue() + ",200);");
            }
        }
    }

    /**
     * Cette méthode colorie le graph en un temps apparti et calcul le meilleur
     * résultat des 3 algos
     *
     * Le meilleur algorithme est défini en fonction du plus faible conflit
     *
     * @param g Graphe sur lequel la coloration va être appliquée
     * @param millisecondes Temps apparti en millisecondes
     * @return renvoie le nombre minimum de conflits
     */
    int colorier(Graph g, int millisecondes) {
        int timeDep = (int) System.currentTimeMillis();
        System.out.println("----------");
        int tempsParAlgo = (int) (((float) (millisecondes) / 5.0));

        boolean coloRdmFait = colorationRandom(g, tempsParAlgo);
        boolean coloDsatFait = colorationDSAT(g, tempsParAlgo);
        boolean coloWPFait = colorationWelshPowell(g, tempsParAlgo);

        String selectedAlgo = "couleur_opt";

        for (Node noeud : g) {
            noeud.setAttribute("couleur_opt", 0);
        }

        int conflitInitial = compte_nb_conflits(g, selectedAlgo);

        int conflitsRandom = conflitInitial;
        if (coloRdmFait) {
            conflitsRandom = compte_nb_conflits(g, "couleur_random");
            System.out.println("Conflits random: " + conflitsRandom);
        }

        int conflitsDsat = conflitInitial;
        if (coloDsatFait) {
            conflitsDsat = compte_nb_conflits(g, "couleur_dsat");
            System.out.println("Conflits dsat: " + conflitsDsat);
        }

        int conflitsWp = conflitInitial;
        if (coloWPFait) {
            conflitsWp = compte_nb_conflits(g, "couleur_wp");
            System.out.println("Conflits welsh powell: " + conflitsWp);
        }

        int lastConflit = 0;
        if (conflitsRandom > conflitsDsat) {
            if (conflitsDsat > conflitsWp) {
                selectedAlgo = "couleur_wp";
                conflitTotal += conflitsWp;
                lastConflit = conflitsWp;
            } else {
                selectedAlgo = "couleur_dsat";
                conflitTotal += conflitsDsat;
                lastConflit = conflitsDsat;
            }
        } else {
            selectedAlgo = "couleur_random";
            conflitTotal += conflitsRandom;
            lastConflit = conflitsRandom;
        }

        if (colorationDescente(g, tempsParAlgo, selectedAlgo)) {
            int conflitsDescente = compte_nb_conflits(g, "couleur_descente");
            System.out.println("Conflits Descente: " + conflitsDescente);
            if (conflitsDescente < compte_nb_conflits(g, selectedAlgo)) {
                selectedAlgo = "couleur_descente";
                conflitTotal -= lastConflit;
                conflitTotal += conflitsDescente;
                lastConflit = conflitsDescente;

            }
        }

        if (colorationRecuitSimule(g, selectedAlgo, tempsParAlgo)) {
            int conflitsRS = compte_nb_conflits(g, "couleur_rs");
            System.out.println("Conflits Rcuit Simulé: " + conflitsRS);
            if (conflitsRS < compte_nb_conflits(g, selectedAlgo)) {
                selectedAlgo = "couleur_rs";
                conflitTotal -= lastConflit;
                conflitTotal += conflitsRS;
            }
        }

        tempsTotal += (int) System.currentTimeMillis() - timeDep;
        System.out.print("Temps: " + ((int) tempsTotal) + " [ALGO: " + selectedAlgo + "]\n");
        System.out.println("MILLISECODES: " + millisecondes);
        if ((int) System.currentTimeMillis() - timeDep > millisecondes) {
            return -1;
        }
        System.out.println("----------");

        for (Node noeud : g) {
            noeud.setAttribute("couleur_opt", (int) (noeud.getAttribute(selectedAlgo)));
        }
        sauver_coloration(g, selectedAlgo, 11);
        return compte_nb_conflits(g, "couleur_opt");
    }

    /**
     * Cette méthode applique une coloration aléatoire au graphe
     *
     * @param graphe Graphe sur lequel la coloration est appliquée
     * @param tempsParAlgo Le temps maximum pour lequel l'algorithme doit
     * s'exécuter
     * @return Renvoie true si l'algorithme à réussi à s'exécuté dans le temps
     * apparti, false sinon.
     */
    public static boolean colorationRandom(Graph graphe, int tempsParAlgo) {
        int kmax = (int) (graphe.getAttribute("nb_couleurs_max"));
        double timeDep = System.currentTimeMillis();
        for (Node n : graphe) {
            if (System.currentTimeMillis() - timeDep > tempsParAlgo / 2.0) {
                return false;
            }
            n.addAttribute("couleur_random", 0);
        }
        for (Node n : graphe) {
            if (System.currentTimeMillis() - timeDep > tempsParAlgo / 2.0) {
                return false;
            }
            n.setAttribute("couleur_random", (int) (Math.random() * (kmax - 1)));
        }
        return true;
    }

    /**
     * Cette méthode redéfini les couleurs qui sont supérieurs à kmax - 1 de la
     * façon suivante: Parcours chaque sommet du graphe, puis, pour chaque
     * sommets qui ont une couleur supérieur à kmax - 1: Redéfini la couleur du
     * sommet par celle qui est la moins présente dans ses voisins
     *
     * @param graphe Graphe sur laquel la réduction est effectuée
     * @param attribut attribut de la couleur à redéfinir
     * @param kmax Nombre de couleurs maximum
     *
     */
    private static boolean reduireCouleurs(Graph graphe, String attribut, int kmax, int tempsParAlgo) {
        int[] nombreCouleurs;
        double timeDep = System.currentTimeMillis();
        for (Node noeud : graphe) {
            if ((int) (noeud.getAttribute(attribut)) > (kmax - 1)) {
                nombreCouleurs = new int[kmax];
                for (int i = 0; i < kmax; i++) {
                    nombreCouleurs[i] = 0;
                }

                Iterator voisinIt = noeud.getNeighborNodeIterator();
                while (voisinIt.hasNext()) {
                    if ((System.currentTimeMillis() - timeDep) > (tempsParAlgo)) {
                        return false;
                    }
                    Node voisin = (Node) voisinIt.next();
                    int voisinCouleur = (int) (voisin.getAttribute(attribut));
                    if (voisinCouleur < kmax) {
                        nombreCouleurs[voisinCouleur]++;
                    }
                }

                int minCouleur = nombreCouleurs[0];
                int indiceMin = 0;
                for (int i = 1; i < kmax; i++) {
                    if (minCouleur > nombreCouleurs[i]) {
                        minCouleur = nombreCouleurs[i];
                        indiceMin = i;
                    }
                }
                noeud.setAttribute(attribut, indiceMin);
            }
        }
        return true;
    }

    /**
     * Cette méthode applique une coloration de Welsh Powell au graphe
     *
     * @param graphe Graphe sur lequel la coloration est appliquée
     * @param tempsParAlgo Le temps maximum pour lequel l'algorithme doit
     * s'exécuter
     * @return Renvoie true si l'algorithme à réussi à s'exécuté dans le temps
     * apparti, false sinon.
     */
    public static boolean colorationWelshPowell(Graph graphe, int tempsParAlgo) {
        WelshPowell welshPowell = new WelshPowell("couleur_wp");
        welshPowell.init(graphe);
        welshPowell.compute();

        int kmax = (int) (graphe.getAttribute("nb_couleurs_max"));
        if (welshPowell.getChromaticNumber() > kmax) {
            return reduireCouleurs(graphe, "couleur_wp", kmax, tempsParAlgo);
        }
        return true;
    }

    /**
     * Cette méthode applique une coloration de DSAT au graphe
     *
     * @param graphe Graphe sur lequel la coloration est appliquée
     * @param tempsParAlgo Le temps maximum pour lequel l'algorithme doit
     * s'exécuter
     * @return Renvoie true si l'algorithme à réussi à s'exécuté dans le temps
     * apparti, false sinon.
     */
    public static boolean colorationDSAT(Graph graphe, int tempsParAlgo) {
        DSat dsat = new DSat("couleur_dsat");
        dsat.init(graphe);
        dsat.compute();

        int kmax = (int) (graphe.getAttribute("nb_couleurs_max"));
        if (dsat.getChromaticNumber() > kmax) {
            return reduireCouleurs(graphe, "couleur_dsat", kmax, tempsParAlgo);
        }
        return true;
    }

    /**
     * Cette fonction collorie le graphe en prenant en référence la coloration
     * déjà effectué avec le moins de conflits. Suite à cela, il va changer la
     * couleur de chaques noeuds qui sera considèré comme conflit Si le cout est
     * moins élevé que le précédent, il garde la coloration. L'algorythme va
     * durer pendant un temps qui lui est définit.
     *
     * @param graphe Le graphe à colorier
     * @param timeMax Le temps maximum d'execution de la fonction
     * @param attributMeilleurAlgo prend l'attribut de l'algorithme avec le cout
     * le plus faible (celui sur lequel la descente sera appliqué)
     * @return Renvoie true si l'algorithme à réussi à s'exécuté dans le temps
     * apparti, false sinon.
     */
    public static boolean colorationDescente(Graph graphe, int timeMax, String attributMeilleurAlgo) {

        int timeDep2 = (int) System.currentTimeMillis();

        int kmax = (int) (graphe.getAttribute("nb_couleurs_max"));
        int cout = 0;

        cout = compte_nb_conflits(graphe, attributMeilleurAlgo);
        for (Node n : graphe) {
            n.addAttribute("couleur_descente", (int) n.getAttribute(attributMeilleurAlgo));
        }

        boolean etat = false;
        int couleur;

        int temps = (int) System.currentTimeMillis() - timeDep2;
        int tempsMax = timeMax - (timeMax * (4 / 10));
        while (temps < tempsMax && cout != 0) {
            for (Node n : graphe) {

                Iterator it = n.getNeighborNodeIterator();

                while (it.hasNext()) {
                    Node u = (Node) it.next();

                    if (etat == false && (int) u.getAttribute("couleur_descente") == (int) n.getAttribute("couleur_descente")) {
                        etat = true;
                    }
                }

                if (etat == true) {
                    etat = false;
                    couleur = Integer.parseInt(n.getAttribute("couleur_descente").toString());

                    n.setAttribute("couleur_descente", (int) (Math.random() * (kmax - 1)));

                    if (cout >= compte_nb_conflits(graphe, "couleur_descente")) {
                        cout = compte_nb_conflits(graphe, "couleur_descente");
                    } else {
                        n.setAttribute("couleur_descente", (int) (Math.random() * (kmax - 1)));

                        if (cout >= compte_nb_conflits(graphe, "couleur_descente")) {
                            cout = compte_nb_conflits(graphe, "couleur_descente");
                        } else {
                            n.setAttribute("couleur_descente", (int) (Math.random() * (kmax - 1)));

                            if (cout >= compte_nb_conflits(graphe, "couleur_descente")) {
                                cout = compte_nb_conflits(graphe, "couleur_descente");
                            } else {
                                n.setAttribute("couleur_descente", couleur);
                            }
                        }
                    }
                }

            }
            temps = (int) System.currentTimeMillis() - timeDep2;
        }
        colorierGraphe(graphe, "couleur_descente");
        return true;
    }

    /**
     * Cette méthode applique une coloration de DSAT au graphe
     * 
     * @param graphe Le graphe à colorier
     * @param meilleurAttribut Le temps maximum d'execution de la fonction
     * @param tempsParAlgo Le temps maximum pour lequel l'algorithme doit
     * s'exécuter
     * @return Renvoie true lorsque l'algorithme est terminé (quand le temps est dépassé ou algo est fini)
     */
    public static boolean colorationRecuitSimule(Graph graphe, String meilleurAttribut, int tempsParAlgo) {
        for (Node noeud : graphe) {
            noeud.setAttribute("couleur_rs", (int) (noeud.getAttribute(meilleurAttribut)));
        }

        if (compte_nb_conflits(graphe, meilleurAttribut) == 0) {
            return true;
        }
        int i = 0;
        int t = 30;
        int imax = 500;
        double risque = 1;
        int l = 0;
        int lastCout = -1;
        int kmax = (int) (graphe.getAttribute("nb_couleurs_max"));
        double timeDep = System.currentTimeMillis();
        int nombreDeSommets = graphe.getNodeCount();
        while (i < imax) {
            i++;
            t++;
            if (t >= 1000) {
                t--;
            }
            l = 0;
            for (Node noeud : graphe) {
                Iterator voisinIt = noeud.getNeighborNodeIterator();
                int[] nombreCouleurs = new int[kmax];
                for (int j = 0; j < kmax; j++) {
                    nombreCouleurs[j] = 0;
                }

                while (voisinIt.hasNext()) {
                    if ((System.currentTimeMillis() - timeDep) > (tempsParAlgo)) {
                        return true;
                    }
                    Node voisin = (Node) voisinIt.next();
                    int voisinCouleur = (int) (voisin.getAttribute("couleur_rs"));
                    if (voisinCouleur < kmax) {
                        nombreCouleurs[voisinCouleur]++;
                    }

                }
                int minCouleur = nombreCouleurs[0];
                int indiceMin = 0;
                for (int j = 1; j < kmax; j++) {
                    if (minCouleur > nombreCouleurs[j]) {
                        minCouleur = nombreCouleurs[j];
                        indiceMin = j;
                    }
                }

                int newColor = indiceMin;
                noeud.setAttribute("couleur_rs", newColor);
                int coutS = compte_nb_conflits(graphe, meilleurAttribut);
                int coutT = compte_nb_conflits(graphe, "couleur_rs");
                if (coutT >= coutS) {
                    double H = Math.random();
                    risque = 0.99 * t;
                    if (H >= Math.exp((float) (coutS - coutT) / risque)) {
                        noeud.setAttribute("couleur_rs", (int) (noeud.getAttribute(meilleurAttribut)));
                    }
                }

                coutS = compte_nb_conflits(graphe, meilleurAttribut);
                coutT = compte_nb_conflits(graphe, "couleur_rs");
                if (coutS > coutT) {
                    noeud.setAttribute("couleur_rs", newColor);

                } else {
                    noeud.setAttribute("couleur_rs", (int) (noeud.getAttribute(meilleurAttribut)));

                }
                if (compte_nb_conflits(graphe, "couleur_rs") == 0) {
                    return true;
                }

                if (lastCout == -1) {
                    lastCout = compte_nb_conflits(graphe, "couleur_rs");
                } else if (lastCout == compte_nb_conflits(graphe, "couleur_rs")) {
                    l++;
                } else {
                    l = 0;
                }
                if (l >= 20) {
                    break;
                }
                lastCout = compte_nb_conflits(graphe, "couleur_rs");

            }

        }
        return true;
    }

    /**Cette fonction exporte les données dans un .CSV
     * 
     * @param file fichier dans lequel il faut écrire
     * @param fileName préfix du fichier du graphe
     * @param nombreDeConflit nombre de conflit du graphe
     */
    public void ExportCSV(File file, String fileName, int nombreDeConflit) {
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(fileName + ";" + nombreDeConflit + "\n");
            fw.flush();
        } catch (IOException ex) {
            System.out.println("fichier introuvable");
        }
    }

    /**
     * Méthode permettant de réaliser une modélisation des vols qui vont se croiser
     * @param prefixeFichier prefixe du fichier qui reste le meme pour parcourir tout les fichier 
     * @param nbFichiers nombre de fichier a traiter 
     * @param millisecondes temps imposer pour l'execution de cette fonction
     */
@Override
    public void modelisation(String prefixeFichier, Integer nbFichiers, Long millisecondes) {
        ArrayList<Aeroport> listA = lectureCSV();
        coordonneeAeroport(listA);
        FileWriter fw;
        File f = new File("Resultats/modelisation-Groupe11.csv");
        try {
            fw = new FileWriter(f);
            fw.append("Fichier;nbAretes;diametre;nbNoeuds;degreMoyen;nbComposantes\n");
            for (int i = 0; i < nbFichiers; i++) {
                Graph g = new SingleGraph("GraphVol");
                ArrayList<Vol> listVol = new ArrayList<>();
                Scanner read = new Scanner(new File("Donnees/"+prefixeFichier+i+".csv"));
                fw.append(prefixeFichier+i+".csv;");
                read.useDelimiter(";");
                while(read.hasNext()){
                    Vol v = new Vol();
                    read.next();
                    String s1 = read.next();
                    String s2 = read.next();
                    for (Aeroport index : listA) {
                        if(index.getCode().equals(s1)){
                            v.setA1(index);
                        }
                    }
                    for (Aeroport index : listA) {
                        if(index.getCode().equals(s2)){
                            v.setA2(index);
                        }
                    }
                    if(v.getA1() != null & v.getA2() != null){
                        v.setM((v.getA2().getY()-v.getA1().getY())/(v.getA2().getX()-v.getA1().getX()));
                        v.setP(v.getA1().getY()-v.getM()*v.getA1().getX());
                        listVol.add(v);
                    }
                    v.sethDep(Double.parseDouble(read.next()));
                    v.setMinDep(Double.parseDouble(read.next()));
                    read.nextLine();
                }
                int count = 0;
                for (Vol index : listVol) {
                    g.addNode(Integer.toString(count));
                    count++;
                }
                for (int j = 0; j < listVol.size(); j++) {
                    for (int k = j+1; k < listVol.size(); k++) {
                        if(listVol.get(k).getM() != listVol.get(j).getM()){
                            //System.out.println("Pas parrallele");
                            //Point d'intersection de coord x,y
                            double x = (listVol.get(k).getP() - listVol.get(j).getP())/(listVol.get(k).getM()-listVol.get(j).getM());
                            double y = listVol.get(j).getM() * x + listVol.get(j).getP();
                            //use math max et math min 
                            double XMax = Math.max(Math.max(listVol.get(k).getA1().getX(),listVol.get(k).getA2().getX()),Math.max(listVol.get(j).getA1().getX(),listVol.get(j).getA2().getX() ));
                            double YMax = Math.max(Math.max(listVol.get(k).getA1().getY(),listVol.get(k).getA2().getY()),Math.max(listVol.get(j).getA1().getY(),listVol.get(j).getA2().getY() ));

                            double XMin = Math.min(Math.min(listVol.get(k).getA1().getX(),listVol.get(k).getA2().getX()),Math.min(listVol.get(j).getA1().getX(),listVol.get(j).getA2().getX() ));
                            double YMin = Math.min(Math.min(listVol.get(k).getA1().getY(),listVol.get(k).getA2().getY()),Math.min(listVol.get(j).getA1().getY(),listVol.get(j).getA2().getY() ));


                            //Modifier condition ca beug 

                            if(XMax >= x & XMin <= x){
                                if(YMax >= y & YMin <= y){
                                    //System.out.println("Coordonnee valide");
                                    final int v  = 828; // vitesse par defaut des avions de ligne
                                    double d = Math.sqrt(Math.pow(Math.abs(listVol.get(j).getA1().getX()-x), 2) + Math.pow(Math.abs(listVol.get(j).getA1().getY()-y), 2));
                                    double t = d / v;
                                    double croisementV1 = (listVol.get(j).gethDep()*60+listVol.get(j).getMinDep()+t*60);
                                    d = Math.sqrt(Math.pow(Math.abs(listVol.get(k).getA1().getX()-x), 2) + Math.pow(Math.abs(listVol.get(k).getA1().getY()-y), 2));
                                    t = d / v;
                                    double croisementV2 = (listVol.get(k).gethDep()*60+listVol.get(k).getMinDep())+t*60;
                                    double timeDiff = Math.abs(croisementV1 - croisementV2);
                                    if(timeDiff < 15){
                                        g.addEdge(Integer.toString(j) + Integer.toString(k), j, k);
                                        //System.out.println("add Edge");
                                    }
                                }
                            }   
                        }
                    }
                }
                HashMap<String, Object> map = calcul_caracteristiques(g);
                int counter = 0;
                for(Map.Entry mapentry : map.entrySet()){
                    if(counter == 4){
                        fw.append(mapentry.getValue()+"\n");
                        fw.flush();
                        counter =0;
                    }
                    else{
                        fw.append(mapentry.getValue()+";");
                    }
                    counter++;
                }
                //System.out.println(g.getEdgeCount() + "  " + g.getNodeCount());
            }
            fw.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Groupe11.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Groupe11.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Méthode permettant de retouner une liste d'aeroport à partir d'un fichier 
     * @return une liste d'aeroport en provenance d'un fichier 
     */
    
    public ArrayList<Aeroport> lectureCSV(){
        ArrayList<Aeroport> listAero = new ArrayList<>();
        Scanner read;
        try {
            read = new Scanner(new File("Donnees/aeroports.csv"));
            read.useDelimiter(";");
            while (read.hasNext()){
                Aeroport a =new Aeroport();
                a.setCode(read.next());
                a.setNom(read.next());
                a.setLatDeg(Double.parseDouble(read.next()));
                a.setLatMin(Double.parseDouble(read.next()));
                a.setLatSec(Double.parseDouble(read.next()));
                a.setLatNS(read.next());
                a.setLongDeg(Double.parseDouble(read.next()));
                a.setLongMin(Double.parseDouble(read.next()));
                a.setLongSec(Double.parseDouble(read.next()));
                a.setLongEO(read.nextLine());
                listAero.add(a);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Groupe11.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listAero;
    }
    
    /**
     * modifie les coordonées en degré et radian des aeroports a partir d'une liste d'aeroport
     * @param listA liste d'aeroport dans laquel les coordonées seront modifiés
     */
    
    public void coordonneeAeroport(ArrayList<Aeroport> listA){
        for (Aeroport index : listA) {
            int coef;
            if("N".equals(index.getLatNS())){
                coef = 1;
            }
            else{
                coef = -1;
            }
            double latitude  = coef*(index.getLatDeg()+(index.getLatMin()/60)+(index.getLatSec()/3600));
            //System.out.println(latitude);
            index.setLatitude(Math.toRadians(latitude));
            if(";E".equals(index.getLongEO())){
                coef = 1;
            }
            else{
                coef = -1;
            }
            double longitude  = coef*(index.getLongDeg()+(index.getLongMin()/60)+(index.getLongSec()/3600));
            //System.out.println(longitude);
            index.setLongitude(Math.toRadians(longitude));
            //System.out.println(index.getLatitude()+"  "+index.getLongitude());
            index.setX(6371*(Math.cos(index.getLatitude()))*Math.sin(index.getLongitude()));
            index.setY(6371*(Math.cos(index.getLatitude()))*Math.cos(index.getLongitude()));
            //System.out.println(index.getX()+"   "+index.getY());
        }
    }
}
