/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Groupes.Groupe11;

/**
 * Classe permettant de crée des vols avec m et p pour l'equation de la droite du vol ainsi que l'heure de depart, la minute et la durée du trajet et pour finir stock les aeroports de départ et d'arrivé
 * @author khali
 */
public class Vol {
    private double m,p;
    private double hDep, minDep,duree;
    private Aeroport a1,a2;

    /**
     * Constructeur par defaut de la classe qui initialise tout à 0 ou null 
     */
    public Vol() {
        this.m = 0;
        this.p = 0;
        this.a1 = null;
        this.a2 = null;
        this.hDep = 0;
        this.minDep = 0;
        this.duree = 0;
    }
    /**
     * Modifie l'heure de depart par le parametre
     * @param hDep heure de départ à remplacer 
     */
    public void sethDep(double hDep) {
        this.hDep = hDep;
    }
    /**
     * Modifie les minutes de depart par le parametre
     * @param minDep minutes de départ à remplacer 
     */
    public void setMinDep(double minDep) {
        this.minDep = minDep;
    }
    /**
     * Modifie la duree du vol par le parametre
     * @param duree minutes du vol à remplacer
     */
    public void setDuree(double duree) {
        this.duree = duree;
    }
    /**
     * retourne l'heure de départ du vol
     * @return retourne l'heure de départ du vol
     */
    public double gethDep() {
        return hDep;
    }
    /**
     * retourne les minutes de départ du vol
     * @return retourne les minutes de départ du vol
     */
    public double getMinDep() {
        return minDep;
    }
    /**
     * retourne la duree du vol
     * @return retourne la duree du vol
     */
    public double getDuree() {
        return duree;
    }
    /**
     * remplace le m par le parametre
     * @param m le m a remplacer
     */
    public void setM(double m) {
        this.m = m;
    }
/**
 * remplace le p par le parametre
 * @param p le p a remplacer 
 */
    public void setP(double p) {
        this.p = p;
    }
/**
 * modifie l'aeroport de départ
 * @param a1 aeroport de départ 
 */
    public void setA1(Aeroport a1) {
        this.a1 = a1;
    }
/**
 * modifie l'aeroport d'arrive 
 * @param a2 aeroport d'arrive
 */
    public void setA2(Aeroport a2) {
        this.a2 = a2;
    }
/**
 * retourne le  m du vol 
 * @return retourne le  m du vol
 */
    public double getM() {
        return m;
    }
/**
 * retourne le p du vol
 * @return retourne le p du vol
 */
    public double getP() {
        return p;
    }
/**
 * retourne l'aeroport de depart
 * @return retourne l'aeroport de depart
 */
    public Aeroport getA1() {
        return a1;
    }
/**
 * retourne l'aeroport d'arriver
 * @return retourne l'aeroport d'arriver
 */
    public Aeroport getA2() {
        return a2;
    }

    
    
}
