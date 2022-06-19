
package Groupes.Groupe11;


/**
 * Type de coloration disponible
 */
public enum TypeGraphe {

    RANDOM,WelshPowell,DSAT;

    @Override
    public String toString() {
        switch (this) {
            case RANDOM:
                return "Random";
            case WelshPowell:
                return "WelshPowell";
            case DSAT:
                return "DSAT";
           
        }
        return "";
    }

    
}

