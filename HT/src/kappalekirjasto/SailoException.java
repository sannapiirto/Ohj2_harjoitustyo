/**
 * 
 */
package kappalekirjasto;

/**
 * @author sanna
 * @version 21.6.2018
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille 
 */
public class SailoException extends Exception {

    private static final long serialVersionUID = 1L;
    
    
    /**
     * Poikkeusluokan muodostaja, jolle tuodaan poikkeuksessa käytettävä viesti 
     * @param viesti Poikkeuksen viesti
     */
    public SailoException (String viesti) {
       super(viesti);
    }
}
