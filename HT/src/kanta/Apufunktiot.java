/**
 * 
 */
package kanta;

/**
 * @author sanna
 * @version 21.6.2018
 *
 */
public class Apufunktiot {

    /**
     * Arpoo kokonaisluvun annetulta lukuv�lilt�
     * @param alku pienin mahdollinen luku
     * @param loppu suurin mahdollinen luku 
     * @return satunnaisen kokonaisluvun annetulta lukuv�lilt�
     */
    public static int rand(int alku, int loppu) {
        double n = (loppu-alku)*Math.random() + alku;
        return (int)Math.round(n);
    }
    
}
