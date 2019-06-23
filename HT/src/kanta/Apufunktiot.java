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
     * Arpoo kokonaisluvun annetulta lukuvšliltš
     * @param alku pienin mahdollinen luku
     * @param loppu suurin mahdollinen luku 
     * @return satunnaisen kokonaisluvun annetulta lukuvšliltš
     */
    public static int rand(int alku, int loppu) {
        double n = (loppu-alku)*Math.random() + alku;
        return (int)Math.round(n);
    }
    
}
