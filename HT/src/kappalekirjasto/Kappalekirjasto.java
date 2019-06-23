/**
 * 
 */
package kappalekirjasto;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @author sanna
 * @version 21.6.2018
 * Kappalekirjasto-luokka, joka hoitaa kappaleiden ja versioiden v‰lisen yhteistyˆn.
 * Lukee ja kirjoittaa kappalekirjaston tiedostoon pyyt‰m‰ll‰ apua avustajiltaan 
 * (Kappale ja Versio)
 */
public class Kappalekirjasto {
    
    private Kappaleet kappaleet = new Kappaleet();
    private Versiot versiot = new Versiot();

    /**
     * Antaa tietyn kappaleen tiedot
     * @param i halutun kappaleen indeksi 
     * @return indeksiss‰ i olevan kappaleen tiedot 
     * @throws IndexOutOfBoundsException jos kysyt‰‰n tyhj‰n kappaleen tietoja
     */
    public Kappale annaKappale(int i) throws IndexOutOfBoundsException {
        return kappaleet.anna(i);
    }

    
    /**
     * Tarkistaa montako kappaletta kappalekirjastossa on.
     * @return kappalekirjastossa olevien kappaleiden lukum‰‰r‰n
     */
    public int getKappaleita() {
        return kappaleet.getLkm();
    }

    
    
    
    /**
     * Antaa tietyn kappaleen versiot listan muodossa
     * @param kappale kappale, jonka versiot halutaan selvitt‰‰  
     * @return tietyn kappaleen versiot
     * <pre name="test">
     *      #import java.util.*;
     *      Kappalekirjasto kappalekirjasto = new Kappalekirjasto();
     *      
     *      Kappale kappale1 = new Kappale(), kappale2 = new Kappale(), kappale3 = new Kappale();
     *      kappale1.rekisteroi(); kappale2.rekisteroi(); kappale3.rekisteroi();
     *      int id1 = kappale1.getID();
     *      int id2 = kappale2.getID();
     *      Versio a1 = new Versio(id1);   kappalekirjasto.lisaa(a1);
     *      Versio b1 = new Versio(id1);   kappalekirjasto.lisaa(b1); 
     *      Versio a2 = new Versio(id2);   kappalekirjasto.lisaa(a2);
     *      Versio b2 = new Versio(id2);   kappalekirjasto.lisaa(b2);
     *      Versio c2 = new Versio(id2);   kappalekirjasto.lisaa(c2);
     *      Versio d2 = new Versio(id2);   kappalekirjasto.lisaa(d2);
     *      
     *      List<Versio> loytyneet; 
     *      loytyneet = kappalekirjasto.annaVersiot(kappale3);
     *      loytyneet.size() === 0;
     *      loytyneet = kappalekirjasto.annaVersiot(kappale1);
     *      loytyneet.size() === 2;
     *      loytyneet.get(0) == a1 === true;
     *      loytyneet.get(1) == b1 === true;
     *      loytyneet = kappalekirjasto.annaVersiot(kappale2);
     *      loytyneet.size() === 4;
     *      loytyneet.get(0) == a2 === true;    
     * </pre> 
     */
    public List<Versio> annaVersiot(Kappale kappale) {
        return versiot.annaVersiot(kappale.getID());
    }

    
    /**
     * Tarkistaa montako versiota kappalekirjastossa on.
     * @return kappalekirjastossa olevien versioiden lukum‰‰r‰n
     */
    public int getVersiot() {
        return versiot.getLkm();
    }

        
    /** 
     * Lis‰t‰‰n kappaleet kappalekirjastoon 
     * @param kappale lis‰tt‰v‰ kappale
     * <pre name="test">
     *      Kappalekirjasto kappalekirjasto = new Kappalekirjasto();
     *      kappalekirjasto.getKappaleita() === 0;
     *      Kappale a = new Kappale();
     *      Kappale b = new Kappale();
     *      kappalekirjasto.lisaa(a);     kappalekirjasto.getKappaleita()  === 1;
     *      kappalekirjasto.lisaa(b);     kappalekirjasto.getKappaleita()  === 2;
     *      kappalekirjasto.annaKappale(0)       === a;
     *      kappalekirjasto.annaKappale(1)       === b;
     *      kappalekirjasto.annaKappale(0) == b  === false;
     *      kappalekirjasto.lisaa(a);     kappalekirjasto.getKappaleita()  === 3;  
     *      kappalekirjasto.annaKappale(3);      #THROWS IndexOutOfBoundsException
     *      kappalekirjasto.lisaa(a);     kappalekirjasto.getKappaleita()  === 4;  
     *      kappalekirjasto.lisaa(b);     kappalekirjasto.getKappaleita()  === 5;  
     *      kappalekirjasto.lisaa(a);     
     * </pre>
     */
    public void lisaa(Kappale kappale) {
        kappaleet.lisaa(kappale); 
    }
    
    
    /**
     * Lis‰‰ version versio-rekisteriin 
     * @param versio lis‰tt‰v‰ versio 
     */
    public void lisaa(Versio versio) {
        versiot.lisaa(versio);
    }
    
    
    /**
     * Lukee tiedostot. 
     * @param kirjastonNimi kirjasto, jonka tiedostot luetaan 
     * @throws SailoException jos lukeminen ep‰onnistuu
     */
    public void lueTiedostot(String kirjastonNimi) throws SailoException {
        kappaleet = new Kappaleet();
        versiot = new Versiot();
        
        setTiedosto(kirjastonNimi);
        kappaleet.lueKappaleet();
        versiot.lueVersiot();
    }
    
    
    private void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi + "/";
        
        kappaleet.setTiedostonPerusNimi(hakemistonNimi + "kappaleet");
        versiot.setTiedostonPerusNimi(hakemistonNimi + "versiot");
    }
    
    
    /**
     * Tallentaa kappalekirjaston tiedot tiedostoon 
     * Jos kappaleiden tallentaminen ep‰onnistuu, yritet‰‰n versiot 
     * silti tallentaa ennen kuin heitet‰‰n poikkeus 
     * @throws SailoException jos tallentaminen ei onnistu 
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        
        try {
            kappaleet.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }
        
        try {
            versiot.tallenna();
        } catch ( SailoException ex ) {
            virhe.concat(" ja " + ex.getMessage());
        }
        
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }


    /**
     * Palauttaa hakuehdon mukaisen kappalelistan alkaen halutusta indeksist‰
     * @param hakuehto hakuehto, jota noudatetaan 
     * @param k indeksi josta alkaen etsit‰‰n 
     * @return kappaleet ArrayListina 
     * @throws SailoException jos kappaleiden etsimisess‰ tulee jokin virhe 
     */
    public Collection<Kappale> etsi(String hakuehto, int k) throws SailoException {
        return kappaleet.etsi(hakuehto, k);
    }


    /**
     * Korvaa tai lis‰‰ kappaleen tietorakenteeseen
     * @param kappale korvattava/lis‰tt‰v‰ kappale
     */
    public void korvaaTaiLisaa(Kappale kappale) {
        kappaleet.korvaaTaiLisaa(kappale);
    }

    
    /**
     * Korvaa tai lis‰‰ version tietorakenteeseen
     * @param versio korvattava/lis‰tt‰v‰ versio
     */
    public void korvaaTaiLisaa(Versio versio) {
        versiot.korvaaTaiLisaa(versio);
    }
    
    
    /**
     * Poistaa version tietorakenteesta
     * @param versio poistettava versio
     */
    public void poistaVersio(Versio versio) {
        versiot.poista(versio);
    }
    
    
    /**
     * Poistaa kappalleen tietorakenteesta
     * @param id poistettavan kappaleen id
     */
    public void poistaKappale(int id) {
        kappaleet.poistaKappale(id);
    }


    /**
     * Testip‰‰ohjelma, jolla testataan kappalekirjasto-luokan toimintaa. 
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
    
        Kappalekirjasto kappalekirjasto = new Kappalekirjasto();
        Kappale a = new Kappale(), b = new Kappale();
        
        a.rekisteroi(); 
        b.rekisteroi();
        
        a.taytaKappale();
        b.taytaKappale();
        
        int id1 = a.getID();
        int id2 = b.getID();
        
        Versio ver1 = new Versio(); ver1.rekisteroi(); ver1.taytaVersio(id1);  // kappalekirjasto.lisaa(ver1); 
        Versio ver2 = new Versio(); ver2.rekisteroi(); ver2.taytaVersio(id1);  // kappalekirjasto.lisaa(ver2); 
        Versio ver3 = new Versio(); ver3.rekisteroi(); ver3.taytaVersio(id1);  // kappalekirjasto.lisaa(ver3); 
        Versio ver4 = new Versio(); ver4.rekisteroi(); ver4.taytaVersio(id2);  // kappalekirjasto.lisaa(ver4); 
        Versio ver5 = new Versio(); ver5.rekisteroi(); ver5.taytaVersio(id2);  // kappalekirjasto.lisaa(ver5);        
        
    
        kappalekirjasto.lisaa(a);
        kappalekirjasto.lisaa(b);
    
        System.out.println("========================= Kappalekirjasto testi ====================");
        
        for (int i = 0; i < kappalekirjasto.getKappaleita(); i++) {
            Kappale kappale = kappalekirjasto.annaKappale(i);
            System.out.println("Kappale paikassa: " + i);
            kappale.tulosta(System.out);
            List<Versio> loytyneet;
            
            loytyneet = kappalekirjasto.annaVersiot(kappale);

            for (Versio ver : loytyneet) {
                ver.tulosta(System.out);
                System.out.println("");
                System.out.println("");
            }
        }        
    }


}
