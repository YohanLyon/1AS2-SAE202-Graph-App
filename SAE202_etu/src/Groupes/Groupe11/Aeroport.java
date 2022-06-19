/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Groupes.Groupe11;

/**
 * Classe qui crée des aeroport stockan le code, le nom, les latitudes en radian, degré, seconde, et Orientation avec les points cardinaux pareille pour la longitude ainsi que les coordonees des aeroports
 * @author khali
 */
public class Aeroport {
    private String code,nom;
    private double latDeg,latMin,latSec,longDeg,longMin,longSec;
    private String latNS,longEO;
    private double longitude,latitude;
    private double x,y;
    /**
     * Constructeur par defaut qui initialise a 0 ou ""
     */
    public Aeroport() {
        this.code = "";
        this.nom = "";
        this.latDeg = 0;
        this.latMin = 0;
        this.latSec = 0;
        this.longDeg = 0;
        this.longMin = 0;
        this.longSec = 0;
        this.latNS = "";
        this.longEO = "";
        this.longitude = 0.0;
        this.latitude = 0.0;
        this.x = 0.0;
        this.y = 0.0;
    }
    /**
     * Remplace la coordonnée x
     * @param x coordonnée a remplacé 
     */
    public void setX(double x) {
        this.x = x;
    }
    /**
     * Remplace la coordonnée y
     * @param y coordonnée a remplacé 
     */
    public void setY(double y) {
        this.y = y;
    }
    /**
     * retourne la coordonnée X
     * @return retourne la coordonnée X
     */
    public double getX() {
        return x;
    }
    /**
     * retourne la coordonnée Y 
     * @return retourne la coordonnée Y 
     */
    public double getY() {
        return y;
    }
    /**
     * Modifie la longitude en radians 
     * @param longitude longitudes en radians 
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
/**
 * modifie latitude en radians 
 * @param latitude latitude en radians 
 */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
/**
 * retourne la longitude en radians 
 * @return retourne la longitude en radians
 */
    public double getLongitude() {
        return longitude;
    }
/**
 * retourne la latitude en radians 
 * @return retourne la latitudes en radians
 */
    public double getLatitude() {
        return latitude;
    }
/***
 * modifie le code de l'aeroports 
 * @param code code de l'aeroport 
 */
    public void setCode(String code) {
        this.code = code;
    }
/**
 * modifie le Nom avec le parametre 
 * @param nom nom del'aeroport 
 */
    public void setNom(String nom) {
        this.nom = nom;
    }
/**
 * modifie la latitude en degré
 * @param latDeg latitude en degré 
 */
    public void setLatDeg(double latDeg) {
        this.latDeg = latDeg;
    }
/**
 * modifie la latitude en minute 
 * @param latMin latitude en minute
 */
    public void setLatMin(double latMin) {
        this.latMin = latMin;
    }
/**
 * modifie la latitude en seconde
 * @param latSec latitude en seconde
 */
    public void setLatSec(double latSec) {
        this.latSec = latSec;
    }
/**
 * modifie longitude en degré 
 * @param longDeg longitude en degré 
 */
    public void setLongDeg(double longDeg) {
        this.longDeg = longDeg;
    }
/**
 * modifie longitude en minute 
 * @param longMin longitude en minute 
 */
    public void setLongMin(double longMin) {
        this.longMin = longMin;
    }
/**
 * modifie longitude en seconde 
 * @param longSec longitude en seconde 
 */
    public void setLongSec(double longSec) {
        this.longSec = longSec;
    }
/**
 * modifie la latitude Nord Sud 
 * @param latNS point cardinaux nord ou sud 
 */
    public void setLatNS(String latNS) {
        this.latNS = latNS;
    }
/**
 * modifie longitude Est Ouest
 * @param longEO point cardinaux est ou ouest
 */
    public void setLongEO(String longEO) {
        this.longEO = longEO;
    }
/**
 * retourne le code de l'aeroport 
 * @return retourne le code de l'aeroport 
 */
    public String getCode() {
        return code;
    }
/**
 * retourne le nom de l'aeroport 
 * @return retourne le nom de l'aeroport 
 */
    public String getNom() {
        return nom;
    }
/**
 * retourne la latitude en degré de l'aeroport 
 * @return retourne la latitude en degré de l'aeroport 
 */
    public double getLatDeg() {
        return latDeg;
    }
/***
 * retourne la latitude en minute de l'aeroport
 * @return retourne la latitude en minute de l'aeroport
 */
    public double getLatMin() {
        return latMin;
    }
/**
 * retourne la latitude en seconde de l'aeroport
 * @return retourne la latitude en seconde de l'aeroport
 */
    public double getLatSec() {
        return latSec;
    }
/**
 * retourne la longitude en degré de l'aeroport
 * @return retourne la longitude en degré de l'aeroport
 */
    public double getLongDeg() {
        return longDeg;
    }
/**
 * retourne la longitude en minute de l'aeroport
 * @return retourne la longitude en minute de l'aeroport
 */
    public double getLongMin() {
        return longMin;
    }
/**
 * retourne la longitude en seconde de l'aeroport
 * @return retourne la longitude en seconde de l'aeroport
 */
    public double getLongSec() {
        return longSec;
    }
/**
 * retourne la latitude Nord ou Sud
 * @return retourne la latitude Nord ou Sud
 */
    public String getLatNS() {
        return latNS;
    }
/**
 * retourne la latitude Est ou Ouest
 * @return retourne la latitude Est ou Ouest
 */
    public String getLongEO() {
        return longEO;
    }
/**
 * Surcharge de to String affichant tout les attributs
 * @return tout les attributs
 */
    @Override
    public String toString() {
        return "Aeroport{" + "code=" + code + ", nom=" + nom + ", latDeg=" + latDeg + ", latMin=" + latMin + ", latSec=" + latSec + ", longDeg=" + longDeg + ", longMin=" + longMin + ", longSec=" + longSec + ", latNS=" + latNS + ", longEO=" + longEO + '}';
    }
    
    
}
