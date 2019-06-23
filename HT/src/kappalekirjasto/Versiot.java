/**
 * 
 */
package kappalekirjasto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Versiot-luokka joka yll‰pit‰‰ versio-rekisteri‰. Osaa lis‰t‰ ja poistaa version. Huolehtii
 * version kirjoittamisesta tiedostoon. Lis‰ksi osaa lajitella ja etsi‰ versioita. 
 * @author sanna
 * @version 28.6.2018
 */
public class Versiot implements Iterable<Versio> {
    
    private String tiedostonPerusNimi = "";
    private boolean muutettu = false;

    private final Collection<Versio> alkiot = new ArrayList<Versio>(); 
    
    
    /**
     * Versiot-luokan muodostaja
     */
    public Versiot() {

    }
   
    
    /**
     * asettaa tiedoston perusnimen
     * @param tiedostonPerusNimi tiedoston perusnimen
     */
    public void setTiedostonPerusNimi(String tiedostonPerusNimi) {
        this.tiedostonPerusNimi = tiedostonPerusNimi;
    }

    
    /**
     * @return palauttaa tiedoston perusnimen 
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }

    
    /**
     * @return palauttaa tiedoston nimen dat-p‰‰ttell‰ 
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }

    
    /**
     * Lis‰‰ version versio-rekisteriin 
     * @param versio versio, joka halutaan lis‰t‰ 
     */
    public void lisaa(Versio versio) {
        alkiot.add(versio);    
        muutettu = true;
    }
    
    
    /**
     * Poistaa version tietorakenteesta
     * @param versio poistettava versio
     */
    public void poista(Versio versio) {
        alkiot.remove(versio);
        muutettu = true;
    }
    
    
    /**
     * Palauttaa versiolistassa olevien alkioiden lukum‰‰r‰n 
     * @return versio-listassa olevien alkioiden lukum‰‰r‰n 
     * <pre name="test">
     *      Versiot versiot = new Versiot();
     *      Versio a = new Versio();
     *      Versio b = new Versio();
     *      a.rekisteroi();
     *      b.rekisteroi();
     *      versiot.getLkm() === 0;
     *      versiot.lisaa(a);
     *      versiot.getLkm() === 1;
     *      versiot.lisaa(b);
     *      versiot.getLkm() === 2;
     *      versiot.lisaa(a);
     *      versiot.lisaa(b);
     *      versiot.getLkm() === 4;
     * </pre>
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    
    /**
     * Palauttaa tietyn kappaleen versiot
     * @param kappaleTunnus kappale, jonka versiot halutaan 
     * @return tietyn kappaleen versiot
     * <pre name="test">
     *      #import java.util.*;
     *      
     *      Versiot versiot = new Versiot();
     *      Versio a2 = new Versio(2); versiot.lisaa(a2);
     *      Versio b1 = new Versio(1); versiot.lisaa(b1);
     *      Versio a1 = new Versio(1); versiot.lisaa(a1);
     *      Versio b2 = new Versio(2); versiot.lisaa(b2);
     *      Versio c1 = new Versio(1); versiot.lisaa(c1);
     *      Versio a3 = new Versio(3); versiot.lisaa(a3);
     *      
     *      List<Versio> loytyneet; 
     *      loytyneet = versiot.annaVersiot(4);
     *      loytyneet.size() === 0;
     *      loytyneet = versiot.annaVersiot(1);
     *      loytyneet.size() === 3;
     *      loytyneet.get(0) == b1 === true;
     *      loytyneet.get(1) == a1 === true;
     *      loytyneet = versiot.annaVersiot(2);
     *      loytyneet.size() === 2;
     *      loytyneet = versiot.annaVersiot(3);
     *      loytyneet.size() === 1;
     *      loytyneet.get(0) == a3 === true;
     * </pre>
     */
    public List<Versio> annaVersiot(int kappaleTunnus) {
        List<Versio> loytyneet = new ArrayList<Versio>();
        
        for (Versio ver : alkiot) {
            if (ver.getKappaleTunnus() == kappaleTunnus) loytyneet.add(ver);
        }
        
        return loytyneet;
    }


    /**
     * Lukee versiot tiedostosta
     * @param tied tiedoston nimen alkuosa 
     * @throws SailoException jos tiedoston lukemisessa menee jotain pieleen 
     */
    public void lueVersiot(String tied) throws SailoException {
        setTiedostonPerusNimi(tied); 
        
        try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
            
            String rivi; 
            
            while ((rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
                Versio versio = new Versio();
                versio.parse(rivi);
                lisaa(versio);
            }
            
            muutettu = false;
            
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea"); 
        } catch (IOException ex) {
            throw new SailoException("Tiedoston kanssa ongelma: " + ex.getMessage());
        }
        
    }
    
    
    /**
     * Luetaan tiedot aiemmin nimetyst‰ tiedostosta
     * @throws SailoException jos tiedoston lukeminen ei onnistu
     */
    public void lueVersiot() throws SailoException {
        lueVersiot(getTiedostonPerusNimi());
    }

    
    /**
     * Tallentaa version tiedot
     * @throws SailoException jos tiedoston tallentaminen 
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        
        File tiedosto = new File(getTiedostonNimi());
        
        try ( PrintWriter fo = new PrintWriter(tiedosto.getCanonicalPath())) {
            for (Versio versio : this ) {
                fo.println(versio.toString());
        }
        } catch ( FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + tiedosto.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + tiedosto.getName() + " kirjoittamisessa ongelmia");
        }
        
        muutettu = false; 
        
        // TODO: tiedoston kirjoittaminen UTF-8- muotoon 

    }
    
    @Override
    public Iterator<Versio> iterator() {
        return alkiot.iterator();
    }
    
    
    /**
     * Korvaa olemassa olevan version, tai lis‰‰ kokonaan uuden mik‰li versio poistettiin muokkauksen 
     * aikana
     * @param versio korvattava/lis‰tt‰v‰ versio
     */
    public void korvaaTaiLisaa(Versio versio) {
        int id = versio.getVersioTunnus();
        
        for (Versio ver : alkiot) {
            if (ver.getVersioTunnus() == id) {
                muutettu = true;
                return;
            }
        }
        lisaa(versio);
    }


    /**
     * Testip‰‰ohjelma Versiot-luokalle. 
     * @param args ei k‰ytˆss‰ 
     */
    public static void main(String[] args) {
        
        Versiot versiot = new Versiot();
        
        Versio ver1 = new Versio();
        Versio ver2 = new Versio();
        Versio ver3 = new Versio();
        Versio ver4 = new Versio();
        Versio ver5 = new Versio();
        
        ver1.rekisteroi();
        ver2.rekisteroi();
        ver3.rekisteroi();
        ver4.rekisteroi();
        ver5.rekisteroi();
        
        ver1.taytaVersio(3);
        ver2.taytaVersio(1);
        ver3.taytaVersio(1);
        ver4.taytaVersio(2);
        ver5.taytaVersio(1);
        
  
        versiot.lisaa(ver1);
        versiot.lisaa(ver2);
        versiot.lisaa(ver3);
        versiot.lisaa(ver4);
        versiot.lisaa(ver5);

        /*
         * Testi tulostus, jotta n‰hd‰‰n tallentuivatko lis‰tyt versiot rekisteriin 
         */
        System.out.println("======================== Versiot testi ==========================");
        
        List<Versio> versiot2 = versiot.annaVersiot(1);
        
        for (Versio ver : versiot2)  {  
            System.out.print(ver.getVersioTunnus() + " ");   // tulostaa version tunnuksen
            ver.tulosta(System.out);                         // tulostaa version tiedot
        }
    
    }
}

