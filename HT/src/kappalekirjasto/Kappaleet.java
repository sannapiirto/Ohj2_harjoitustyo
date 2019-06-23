/**
 * 
 */
package kappalekirjasto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author sanna
 * @version 21.6.2018
 * Kappaleet-luokka, joka pit‰‰ yll‰ kappalerekisteri‰ eli osaa poistaa ja lis‰t‰ 
 * kappaleita. Huolehtii kappalelistan kirjoittamisesta tiedostoon. Lis‰ksi osaa 
 * etsi‰ ja lajitella kappaleita.  
 */
public class Kappaleet implements Iterable<Kappale> {
    
    private static final int MAX_KAPPALEITA      = 5;
    private int              lkm                 = 0;
    private String           tiedostonPerusNimi  = "nimet";
    private Kappale[]        alkiot              = new Kappale[MAX_KAPPALEITA];
    private boolean          muutettu            = false;
    
    
    /**
     * Kappaleet-luokan muodostaja.
     */
    public Kappaleet() {
        
    }
    
    
    /**
     * @return palauttaa tiedoston perusnimen tiedostop‰‰ttell‰ dat
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }

    
    /**
     * @return palauttaa tiedoston perusnimen 
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    

    /**
     * Asettaa tiedoston perusnimen
     * @param tiedostonPerusNimi tiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tiedostonPerusNimi) {
        this.tiedostonPerusNimi = tiedostonPerusNimi;
    }


    /**
     * Lis‰t‰‰n kappale kappalerekisteriin
     * @param kappale kappalekirjastoon lis‰tt‰v‰ kappale 
     * <pre name="test">
     *      Kappaleet kappaleet = new Kappaleet();
     *      Kappale a = new Kappale(), b = new Kappale();
     *      kappaleet.getLkm() === 0;;
     *      kappaleet.lisaa(a);             kappaleet.getLkm() === 1;
     *      kappaleet.lisaa(b);             kappaleet.getLkm() === 2;
     *      kappaleet.lisaa(a);             kappaleet.getLkm() === 3;
     *      kappaleet.anna(0) === a;
     *      kappaleet.anna(1) === b;
     *      kappaleet.anna(2) === a;
     *      kappaleet.anna(1) == a === false;
     *      kappaleet.anna(1) == b === true;
     *      kappaleet.anna(3) === a;        #THROWS IndexOutOfBoundsException
     *      kappaleet.lisaa(a);             kappaleet.getLkm() === 4;
     *      kappaleet.lisaa(b);             kappaleet.getLkm() === 5;
     *      kappaleet.lisaa(a);             kappaleet.getLkm() === 6;
     * </pre>
     */
    public void lisaa (Kappale kappale) {
        if (lkm >= alkiot.length) kasvataTaulukkoa(); 
        alkiot[lkm] = kappale; 
        lkm++;
        
        muutettu = true;
    }
    
    
    private void kasvataTaulukkoa() {
        Kappale[] uusiTaulukko = new Kappale[alkiot.length * 2];
        
        for (int i = 0; i < alkiot.length; i++) {
            uusiTaulukko[i] = alkiot[i];
        }
        
        alkiot = uusiTaulukko;
    }


    /**
     * Tarkistetaan montako kappaletta kappalerekisteriss‰ on 
     * @return kappalerekisteriss‰ olevien kappaleiden m‰‰r‰n 
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Etsit‰‰n tietyn kappaleen tiedot kappalerekisterist‰
     * @param i etsitt‰v‰n kappaleen indeksi
     * @throws IndexOutOfBoundsException jos yritet‰‰n hakea tietoa tyhj‰st‰ paikasta  
     * @return kappaleen tiedot
     */
    public Kappale anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || i >= lkm ) {
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        }
                    
        return alkiot[i];
    }
    
    
    /**
     * Lukee kappale-tiedoston tiedot. Alustaa kappalleet tiedoston tiedoilla. 
     * @param tied tiedoston perusnimi
     * @return null, jos lukeminen ok. Jos ep‰onnistuu, palautetaan virheilmoitus. 
     * @throws SailoException jos tiedosto ei aukea
     */
    public String lueKappaleet(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        
        try (BufferedReader fi = new BufferedReader(new InputStreamReader
                (new FileInputStream(getTiedostonNimi()), "UTF-8"))) { // new FileReader(getTiedostonNimi()))) {  
            
            String rivi; 
            while ((rivi = fi.readLine()) != null ) {
                 rivi = rivi.trim();
                 if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
                 Kappale kappale = new Kappale();
                 kappale.parse(rivi);
                 lisaa(kappale);
            }
            
            muutettu = false;
                           
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea"); 
        
        } catch (IOException ex) {
            throw new SailoException("Ongelmia tiedoston kanssa " + ex.getMessage());
        }
        
        return null;
    }
    

    /**
     * Luetaan aiemmin nimetty tiedosto 
     * @throws SailoException jos tiedoston lukemisessa menee jotain pieleen 
     */
    public void lueKappaleet() throws SailoException {
        lueKappaleet(getTiedostonPerusNimi());
    }
    
    
    /** 
     * Tallentaa kappaleet tiedostoon 
     * Dokumentaatio kesken 
     * @throws SailoException jos tallentaminen ei onnistu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;
        
        /* File fbak = new File(getBakNimi());
        File tiedosto = new File(getTiedostonPerusNimi() + ".txt");
        fbak.delete();
        tiedosto.renameTo(fbak);
        */
        
        File tiedosto = new File(getTiedostonNimi());
        
        try ( PrintWriter fo = new PrintWriter(tiedosto.getCanonicalPath())) {
            for (Kappale kappale : this ) {
                fo.println(kappale.toString());
        }
        } catch ( FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + tiedosto.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + tiedosto.getName() + " kirjoittamisessa ongelmia");
        }
        
        muutettu = false; 
    }

    
    /**
     * KappaleetIterator joka k‰y Kappaleet-tietorakenteen l‰pi
     * @author sanna
     * @version 15.7.2018
     *
     */
    public class KappaleetIterator implements Iterator<Kappale> {
        
        private int osoitin = 0;

        @Override
        public boolean hasNext() {
            return osoitin < getLkm();
        }

        @Override
        public Kappale next() throws NoSuchElementException {
            if (!hasNext()) throw new NoSuchElementException("Ei ole olemassa");
            return anna(osoitin++);
        }
        
        
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Iteraattori ei poista kappaleita");
        }
        
    }

    @Override
    public Iterator<Kappale> iterator() {
        return new KappaleetIterator();
    }

    
    /**
     * Palauttaa hakuehdon mukaiset kappaleet collectionissa 
     * @param hakuehto hakuehto
     * @param k mink‰ kent‰n perusteella haetaan  
     * @return hakuehtoon sopivat kappaleet collectionina 
     */
    public Collection<Kappale> etsi(String hakuehto, @SuppressWarnings("unused") int k) {
        Collection<Kappale> loytyneet = new ArrayList<Kappale>();
        String hakuehtoPienena = hakuehto.toLowerCase();
        for (Kappale kappale : this) {
            String kappaleNimiPienena = kappale.getNimi().toLowerCase();
            if(kappaleNimiPienena.startsWith(hakuehtoPienena)) {
                loytyneet.add(kappale);
            }
        }
        
        return loytyneet;
        
        
        // TODO: hakukent‰n vaihtaminen puuttuu viel‰ 
    }
    
    
    /**
     * Korvaa olemassa olevan kappaleen tai lis‰‰ kokonaan uuden kappaleen, mik‰li 
     * ehdittiin muokkaamisen aikana poistaa
     * @param kappale lis‰tt‰v‰/korvattava kappale
     */
    public void korvaaTaiLisaa(Kappale kappale) {
        int id = kappale.getID();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getID() == id) {
                alkiot[i] = kappale;
                muutettu = true;
                return;
            }
        }
        
        lisaa(kappale);
    }
    
    
    /**
     * Poistaa kappaleen 
     * @param id poistettavan kappaleen id
     */
    public void poistaKappale(int id) {
        int index = 0;
        
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getID() == id) {
                index = i;
                lkm--;
                muutettu = true;
                break;
            }
        }
        
        for (int i = index; i < lkm; i++) {
            alkiot[i] = alkiot[i + 1];
        }
        
        alkiot[lkm] = null;
    }

        
    /**
     * Testip‰‰ohjelma kappaleet-luokan testaamiseksi 
     * @param args ei k‰ytˆss‰ 
     */
    public static void main(String[] args) {
    
        Kappaleet kappaleet = new Kappaleet();
        
        Kappale laulu1 = new Kappale();
        Kappale laulu2 = new Kappale();
        Kappale laulu3 = new Kappale();
        
        laulu1.rekisteroi();
        laulu2.rekisteroi();
        laulu3.rekisteroi();
        
        laulu1.taytaKappale();
        laulu2.taytaKappale();
        laulu3.taytaKappale();
        

        kappaleet.lisaa(laulu1);
        kappaleet.lisaa(laulu2);
        kappaleet.lisaa(laulu3);

        /*
         * Testi tulostus, jotta n‰hd‰‰n tallentuivatko lis‰tyt kappaleet rekisteriin 
         */
        System.out.println("======================== Kappaleet testi ==========================");
            
        for (int i = 0; i < kappaleet.getLkm(); i++)  {
            Kappale kappale = kappaleet.anna(i);        // haetaan kappaleen tiedot uuteen olioon 
            System.out.println("Kappale nro: " + i);    // tulostetaan otsikko 
            kappale.tulosta(System.out);                // tulostetaan uuteen olioon tallennettu kappale
        }
    }
}
