/**
 * 
 */
package kappalekirjasto;

import static kanta.Apufunktiot.*;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;



/**
 * Versio-luokka joka huolehtii version tiedoista ja osaa mm. antaa tietyn kentän tiedot pyydettäessä.
 * Tarkistaa tietojen oikeellisuuden ja osaa muuttaa merkkijonon version tiedoiksi ja päinvastoin. 
 * @author sanna
 * @version 28.6.2018
 */
public class Versio implements Cloneable {
    
    private int versioTunnus; 
    private int kappaleTunnus;
    private String savellaji = "";
    private int alkuperainen; 
    private int valmis;
    private String kommentti = "";
    private String soinnut = "";
    
    
    /**
     * @return palauttaa Version sävellajin
     */
    public String getSavellaji() {
        return savellaji;
    }


    /**
     * Asettaa version sävellajin 
     * @param savellaji asetettava sävellaji
     * @return null, jos on ok. Muuten virheilmoitus. 
     */
    public String setSavellaji(String savellaji) {
        if (savellaji.length() > 3) return "Syöte on liian pitkä sävellajiksi";
        
        this.savellaji = savellaji;
        return null; 
    }


    /**
     * @return tiedon siitä onko versio alkuperäinen (1= alkuperäinen, 0= ei ole)
     */
    public int getAlkuperainen() {
        return alkuperainen;
    }


    /**
     * Asettaa tiedon kappaleen alkuperäisyydestä
     * @param alkuperainen onko alkuperäinen
     * @return null (ei oikeellisuustarkistusta)
     */
    public String setAlkuperainen(int alkuperainen) {
        this.alkuperainen = alkuperainen;
        return null;
    }


    /**
     * @return tiedon siitä onko versio valmis (1=kyllä, 0=ei)
     */
    public int getValmis() {
        return valmis;
    }


    /**
     * Asettaa tiedon siitä onko versio valmis
     * @param valmis onko valmis vai ei
     * @return null (ei oikeellisuustarkistusta)
     */
    public String setValmis(int valmis) {
        this.valmis = valmis;
        return null;
    }


    /**
     * @return version kommentit
     */
    public String getKommentti() {
        return kommentti;
    }


    /**
     * Asettaa versiolle kommentin
     * @param kommentti asetettava kommentti
     * @return null (ei oikeellisuustarkistusta)
     */
    public String setKommentti(String kommentti) {
        this.kommentti = kommentti;
        return null;
    }


    /**
     * @return version soinnut
     */
    public String getSoinnut() {
        return soinnut;
    }


    /**
     * Asettaa kappaleelle soinnut
     * @param soinnut asetettava soinnut
     * @return null (ei toistaikseksi oikeellisuustarkistuksia)
     */
    public String setSoinnut(String soinnut) {
        this.soinnut = soinnut;
        return null;
    }


    /**
     * Asettaa version tunnuksen 
     * @param versioTunnus asetettava tunnus
     */
    public void setVersioTunnus(int versioTunnus) {
        this.versioTunnus = versioTunnus;
    }


    /**
     * Asettaa kappaletunnuksen
     * @param kappaleTunnus asetettava kappaletunnus
     */
    public void setKappaleTunnus(int kappaleTunnus) {
        this.kappaleTunnus = kappaleTunnus;
    }

    
    /**
     * Tarkistaa versio-olion tunnuksen ja palauttaa sen 
     * @return version tunnuksen 
     */
    public int getVersioTunnus() {
        return versioTunnus;
    }
    

    /**
     * Tarkistaa versio-olio kappaletunnuksen ja palauttaa sen
     * @return version kappaletunnuksen eli mille kappaleelle versio kuuluu 
     */
    public int getKappaleTunnus() {
        return kappaleTunnus;
    }


    /**
     * Seuraavan version ID versio-taulukossa 
     */
    private static int seuraavanTunnus = 1;
    
    /**
     * Versio-luokan muodostaja
     */
    public Versio() { }
    
    
    /**
     * Versio-luokan muodostaja, joka alustaa kappaletunnuksen 
     * @param kappaleTunnus arvo, jolla kappaletunnus alustetaan 
     */
    public Versio(int kappaleTunnus) {
        this.kappaleTunnus = kappaleTunnus;
    }

    
    /**
     * Työstämisvaiheen metodi, joka täyttää version. Tämä poistetaan harjoitustyön edetessä.
     * @param kappaleenNro sen kappaleen tunnus, jolle versio kuuluu 
     */
    public void taytaVersio(int kappaleenNro) {
        kappaleTunnus = kappaleenNro;
        savellaji = "Am";
        kommentti = "blaa blaa blaa " + rand(1000, 2000);
        soinnut = "Am C F G";
    }

    
    /**
     * Tulostaa version tiedot näyttöön 
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Tulostaa version tiedot näyttöön 
     * @param out tietovirta johon tulostetaan 
     */
    public void tulosta(PrintStream out) {
        out.println("Sävellaji: " + savellaji);
        out.println("Alkuperäinen (1=kyllä): "+ alkuperainen + " Valmis (1= kyllä): " + valmis);
        out.println("Kommentit: " + kommentti);
        out.println("Soinnut: " + soinnut);
    }
    
    
    /**
     * Rekisteröi versio antamalla sille versioTunnuksen
     * @return versiolle annetun id:n 
     * <pre name="test">
     *     Versio a = new Versio();
     *     Versio b = new Versio();
     *     a.getVersioTunnus() === 0;
     *     a.rekisteroi();
     *     b.rekisteroi();
     *     a.rekisteroi();
     *     int n1 = a.getVersioTunnus();
     *     int n2 = b.getVersioTunnus();
     *     n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        if (versioTunnus == 0) {
            versioTunnus = seuraavanTunnus;
            seuraavanTunnus++;
        }
        
        return versioTunnus;
    }    

    
    /**
     * Erottelee version tiedot merkkijonosta
     * @param s version tiedot merkkijonona
     */
    public void parse(String s) {
        StringBuilder sb = new StringBuilder(s);
        versioTunnus = Mjonot.erota(sb, '|', getVersioTunnus());
        kappaleTunnus = Mjonot.erota(sb, '|', getKappaleTunnus());
        savellaji = Mjonot.erota(sb, '|', savellaji);
        alkuperainen = Mjonot.erota(sb, '|', alkuperainen);
        valmis = Mjonot.erota(sb, '|', valmis);
        kommentti = Mjonot.erota(sb, '|', kommentti);
        soinnut = Mjonot.erota(sb, '|', soinnut);
        
        tarkistaSeuraavanTunnus(getVersioTunnus());
    }


    private void tarkistaSeuraavanTunnus(int id) {
        if (id >= seuraavanTunnus) seuraavanTunnus = (id + 1);        
    }

    
    @Override
    public String toString() {
        return  "" + 
                getVersioTunnus()  + "|" +
                getKappaleTunnus() + "|" + 
                savellaji          + "|" + 
                alkuperainen       + "|" + 
                valmis             + "|" + 
                kommentti          + "|" + 
                soinnut;
    }

    
    @Override
    public Versio clone() throws CloneNotSupportedException{
        Versio uusi;
        uusi = (Versio) super.clone();
        return uusi;
    }

        
    /**
     * Testipääohjelma Versio-luokalle
     * @param args ei käytössä 
     */
    public static void main(String[] args) {
        Versio ver1 = new Versio();
        Versio ver2 = new Versio();

        ver1.taytaVersio(3); 
        ver2.taytaVersio(1); 
        
        ver1.rekisteroi(); 
        ver2.rekisteroi(); 

        ver1.tulosta(System.out);    
        ver2.tulosta(System.out); 
    
    }
}
