/**
 * 
 */
package kappalekirjasto;

import static kanta.Apufunktiot.rand;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author sanna
 * @version 21.6.2018
 * Kappale-luokka, joka huolehtii yksittäisen kappaleen tiedoista ja osaa tarkistaa 
 * tietojen oikeellisuuden. Osaa myös muuttaa merkkijonon kappaleen tiedoiksi ja antaa
 * merkkijonon i:nnen kentän tiedot sekää asettaa tiedon merkkijonon i:nneksi kentäksi.
 */
public class Kappale implements Cloneable {

    private int           ID; 
    private String        kappaleenNimi    = "";
    private String        esittajanNimi    = "";
    private int           julkaisuVuosi; 
    private String        genre            = "";
    private String        kommentti        = "";

    
    /**
     * Asettaa kommentin kappalleelle
     * @param kommentti asetettava kommentti 
     * @return null (ei tarvetta oikeellisuustarkistukselle)
     */
    public String setKommentti(String kommentti) {
        this.kommentti = kommentti;
        return null;
    }
    
    
    /**
     * Asettaa kappaleelle nimen 
     * @param kappaleenNimi asetettava nimi
     * @return null, jos nimi ei ole tyhjä - muuten virheilmoituksen
     */
    public String setKappaleenNimi(String kappaleenNimi) {
        if (kappaleenNimi == null || kappaleenNimi.equals("")) return "Kappaleen nimi pitää antaa!";
        
        this.kappaleenNimi = kappaleenNimi;
        return null;
    }


    /**
     * Asettaa esittäjän nimen 
     * @param esittajanNimi asetettava nimi
     * @return null (ei tarvetta oikeellisuustarkistukselle)
     */
    public String setEsittajanNimi(String esittajanNimi) {
        this.esittajanNimi = esittajanNimi;
        return null;
    }


    /**
     * Asettaa julkaisuvuoden
     * @param julkaisuVuosi asetettava vuosi
     * @return null, jos vuosi ok. Jos liian suuri, palautetaan tieto tästä. 
     */
    public String setJulkaisuVuosi(String julkaisuVuosi) {
        int vuosi = Mjonot.erotaInt(julkaisuVuosi, 0);
        if (vuosi > 2050) return "Vuosi ei voi olla näin suuri!"; 
        
        this.julkaisuVuosi = vuosi; 
        return null;
    }


    /**
     * Asettaa kappaleelle genren 
     * @param genre asetettava genre
     * @return null (ei oikeellisuustarkistusta)
     */
    public String setGenre(String genre) {
        this.genre = genre;
        return null;
    }

    
    /**
     * @return palauttaa kappaleen esittäjän nimen 
     */
    public String getEsittajanNimi() {
        return esittajanNimi;
    }


    /**
     * @return palauttaa kappaleen julkaisuvuoden
     */
    public int getJulkaisuVuosi() {
        return julkaisuVuosi;
    }


    /**
     * @return palauttaa kappaleen genren
     */
    public String getGenre() {
        return genre;
    }


    /**
     * @return palauttaa kappaleen kommentit
     */
    public String getKommentti() {
        return kommentti;
    }


    /**
     * Palautetaan olion ID.
     * @return olion ID:n
     */
    public int getID() {
        return ID;
    }
    
    
    /**
     * Tarkistetaan kappaleen nimi ja palautetaan se. 
     * @return kappaleen nimen
     */
    public String getNimi() {
        return kappaleenNimi;
    }

        
    /**
     * Seuraavan uuden kappaleen ID. 
    */
    private static int seuraavanID = 1;
    
    
    /**
     * Kappale-luokan parametriton muodostaja. 
     */
    public Kappale() {

    }
        
    
    /**
     * Metodi joka antaa uusille kappaleille ID:n
     * @return kappaleen ID:n
     * <pre name="test">
     *      Kappale a = new Kappale();
     *      a.getID() === 0;
     *      a.rekisteroi();
     *      Kappale b = new Kappale();
     *      b.rekisteroi();
     *      a.rekisteroi();
     *      int n1 = a.getID();
     *      int n2 = b.getID();
     *      n1 === n2 - 1;
     * </pre>
     */
    public int rekisteroi() {    
        if (getID() == 0) {
            ID = seuraavanID;
            seuraavanID++;
        }
        
        return ID;
    }
    
    
    @Override 
    public String toString() {
        return "" + 
                getID() + "|" +
                kappaleenNimi + "|" + 
                esittajanNimi + "|" +
                julkaisuVuosi + "|" + 
                genre + "|" + 
                kommentti;
    }
        
    
    /**
     * Tulostetaan kappaleen tiedot
     * @param out tietovirta johon tulsotetaan 
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", ID, 3) + " " + kappaleenNimi + ", " + esittajanNimi);
        out.println("Julkaisuvuosi: " + julkaisuVuosi);
        out.println("Kappaleen genre: " + genre);
        out.println("Kommentit: " + kommentti);
        out.println("");
    }
    
    
    /**
     * Metodi jolla saadaan kappaleet tulostettua näyttöön 
     * @param os Tietovirta johon tulostetaan 
     */
    public void tulosta(OutputStream os)  {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Erottelee kappaleen tiedot merkkijonosta |- merkin kohdalta.
     * @param rivi kappaleen tiedot merkkijonona
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        ID = Mjonot.erota(sb, '|', getID());
        kappaleenNimi = Mjonot.erota(sb, '|', kappaleenNimi);
        esittajanNimi = Mjonot.erota(sb, '|', esittajanNimi);
        julkaisuVuosi = Mjonot.erota(sb, '|', julkaisuVuosi);
        genre = Mjonot.erota(sb, '|', genre);
        kommentti = Mjonot.erota(sb, '|', kommentti); 
        
        tarkistaSeuraavanID(ID);
    }


    private void tarkistaSeuraavanID(int id) {
        if (id >= seuraavanID) seuraavanID = (id + 1);
    }
    
    @Override
    public Kappale clone() throws CloneNotSupportedException{
        Kappale uusi;
        uusi = (Kappale) super.clone();
        return uusi;
    }

    
    /**
     * Tilapäinen metodi, joka täyttää kappaleen tiedot oletusarvoilla.
     */
    public void taytaKappale()  {
        kappaleenNimi = "Ukko Nooa " + rand(1000, 9999);
        esittajanNimi = "Lasse Laulaja";
        julkaisuVuosi = 1900;
        genre = "Huumori";
        kommentti = "Ukko nooa ukko nooa oli kunnon mies"; 
    }

    
    /**
     * Testi-pääohjelma kappale-luokan testaamiseksi. 
     * @param args ei käytössä 
     */
    public static void main(String[] args) {
        
        Kappale laulu = new Kappale();
        Kappale laulu2 = new Kappale();

        laulu.taytaKappale(); 
        laulu2.taytaKappale(); 
        
        laulu.rekisteroi(); 
        laulu2.rekisteroi(); 

        laulu.tulosta(System.out);    
        laulu2.tulosta(System.out);     
    
    }
}
