/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sae202_etu;

import Groupes.Groupe11.Groupe11;
import Groupes.Groupe11.FenetrePrincipale;

/**
 *
 * @author brice.effantin
 */
public class SAE202_etu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Groupe11 obj=new Groupe11();
        //obj.challenge("colo-test",11,Long.valueOf(600000));
        obj.modelisation("model-test",10,Long.valueOf(10000));
        
        //FenetrePrincipale fp=new FenetrePrincipale();
        //fp.setVisible(true);
    }
    
}
