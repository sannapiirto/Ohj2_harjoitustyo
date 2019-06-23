package fxKappalekirjasto;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import kappalekirjasto.Kappale;
import kappalekirjasto.Kappalekirjasto;
import kappalekirjasto.SailoException;
import kappalekirjasto.Versio;


/**
 * @author sanna
 * @version 6.6.2018
 * Luokka käyttöliittymän tapahtumien hoitamiseksi  
 */
public class KappalekirjastoGUIController implements Initializable{
    
    @FXML private TextField textHaku;
    @FXML private TextField hakuehto;
    @FXML private ScrollPane panelKappale;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    @FXML private ListChooser<Kappale> chooserKappaleet;
    @FXML private StringGrid<Versio> gridVersiot;
    
    @FXML private TextField editNimi;
    @FXML private TextField editEsittaja;
    @FXML private TextField editJulkaisuVuosi;
    @FXML private TextField editGenre;
    @FXML private TextArea editKommentit;

    
    @FXML void handleAbout() {
        naytaTiedot();
    }
    
    
    @FXML void handleApua() {
        apua();
    }
    
    
    @FXML void handleAvaa() {
        avaa();
    }
    
    
    @FXML void handleHaeVersio()  {
        haeVersio();
    }  
    
    
    @FXML void handleHaku() {
        haeKappale();
    }
    
    
    @FXML void handleMuokkaaKappale() {
        muokkaaKappale();
    }
    
    
    @FXML void handleLopeta() {
        lopeta();
    }
    
    
    @FXML void handleMuokkaaVersio() {
        muokkaaVersio();
    }
    
    
    @FXML void handlePoistaKappale() {
        poistaKappale();
    }
    
    
    @FXML void handlePoistaVersio() {
        poistaVersio();
    }
    
    
    @FXML void handleTallenna() {
        tallenna();
    }
    
    
    @FXML void handleTulosta() {
        tulosta();
    }
    
    
    @FXML void handleUusiKappale() {
        uusiKappale();
    }
    
    
    @FXML void handleUusiVersio() {
        uusiVersio();
    }
    
    // ======================================================================================== 
    
    private Kappalekirjasto kappalekirjasto;
    private String kirjastonnimi = "Sanna";
    private Kappale kappaleKohdalla;
    private Versio versioKohdalla;
    private TextField[] edits;  

    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa jäsenten tiedot.
     * Alustetaan myös jäsenlistan kuuntelija
     */
    protected void alusta() {        
        chooserKappaleet.clear();
        gridVersiot.clear();
        chooserKappaleet.addSelectionListener(e -> naytaKappale());
        hakuehto.setOnKeyReleased(e -> hae(1));
        edits = new TextField[]{editNimi, editEsittaja, editJulkaisuVuosi, editGenre};
    }

    
    /**
     * Ohjelman käyttöohjeen näyttäminen kurssin TIM-sivulta
     */
    private void apua()  {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2018k/ht/smpiirto");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }   
    }
    
    
    /**
     * Kysytään tiedoston nimi ja avataan se
     * @return true jos onnistui, false jos ei onnistunut 
     */
    public boolean avaa()  {
        String uusinimi = KappalekirjastonNimiController.kysyNimi(null, kirjastonnimi);
        if (uusinimi == null) return false;
        lueTiedosto(uusinimi);
        return true;
    }
    
    
    /**
     * Hakee kappaleet kappalelistaan hakuehtojen mukaisesti.
     */
    private void haeKappale()  {        
        if ( kappaleKohdalla != null) hae(kappaleKohdalla.getID());
    }
    
    
    /**
     * Hakee versiot versio-listaan hakuehtojen mukaisesti. 
     */
    private void haeVersio()  {
        Dialogs.showMessageDialog("Ei osata vielä hakea!");
    }

    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
        
    }
    
    
    /**
     * Ohjelman lopettaminen 
     */
    private void lopeta()  {
        tallenna();
        Platform.exit();
    }
    
    
    /**
     * Alustaa kappalekirjaston lukemalla sen valitun nimisestä tiedostosta, huom. ei toimi vielä 
     * @param nimi tiedosto, jolta kirjaston tiedot luetaan
     * @return null, jos lukeminen ok. Jos ei ole ok, palautetaan virhe.  
     */
    protected String lueTiedosto(String nimi) {
        kirjastonnimi = nimi;
        setTitle("Kirjasto - " + kirjastonnimi);
        
        try {
            kappalekirjasto.lueTiedostot(nimi);
            hae(0);
            return null;
        } catch (SailoException ex) {
            hae(0);
            String virhe = ex.getMessage();
            if (virhe != null) Dialogs.showMessageDialog(virhe);
            return virhe;
        }       
    }
    
    
    /**
     * Kappaleen tietojen muokkaaminen 
     */
    private void muokkaaKappale() {    
        if (kappaleKohdalla == null ) return;
        try {
            Kappale kappale;
            kappale = KappaleDialogController.kysyKappale(null, kappaleKohdalla.clone());
            if ( kappale == null ) return;
            kappalekirjasto.korvaaTaiLisaa(kappale);
            hae(kappale.getID());
        } catch (CloneNotSupportedException ex) {
            // 
        }
    }
    
    
    /**
     * Olemassa olevan version tietojen muokkaaminen 
     */
    private void muokkaaVersio() {
        if (kappaleKohdalla == null) return;
        versioKohdalla = gridVersiot.getObject();
        
        if (versioKohdalla == null ) return;
        try {
            Versio versio;
            versio = VersioDialogController.kysyVersio(null, versioKohdalla, kappaleKohdalla.getNimi());
            if ( versio == null ) return;
            kappalekirjasto.korvaaTaiLisaa(versio);
            hae(kappaleKohdalla.getID()); 
        } catch (Exception ex) {
            // 
        }    
    }
    

    /**
     * Näyttää kappalleen tiedot käyttöliittymässä 
     */
    protected void naytaKappale() {
        kappaleKohdalla = chooserKappaleet.getSelectedObject();
        
        if (kappaleKohdalla == null) return;
        KappaleDialogController.naytaKappale(edits, editKommentit, kappaleKohdalla);
        naytaVersiot();
    }
    
    
    /**
     * Näyttää version tiedot StringGridissä
     */
    protected void naytaVersiot() {
        gridVersiot.clear();
        List<Versio> versiot = kappalekirjasto.annaVersiot(kappaleKohdalla); 
        String[] rivi = new String[2];
        
        for (Versio ver : versiot) {
            rivi[0] = ver.getSavellaji();
            rivi[1] = ver.getKommentti();
            gridVersiot.add(ver, rivi); 
        }
    }

        
    /**
     * Ohjelman tietojen näyttäminen
     */
    private void naytaTiedot()  {
        ModalController.showModal(KappalekirjastoGUIController.class.getResource("AboutView.fxml"), 
                "Kappalekirjasto", null, "");
    }
    
        
    /**
     * Kappaleen poistaminen 
     */
    private void poistaKappale()  {    
        List<Versio> poistettavat = kappalekirjasto.annaVersiot(kappaleKohdalla);
        
        for (Versio ver : poistettavat) {
            kappalekirjasto.poistaVersio(ver);
        }
        
        kappalekirjasto.poistaKappale(kappaleKohdalla.getID());
        hae(kappaleKohdalla.getID());
    }
    
    
    /**
     * Version poistaminen 
     */
    private void poistaVersio()  {
        versioKohdalla = gridVersiot.getObject();
        kappalekirjasto.poistaVersio(versioKohdalla);
        hae(kappaleKohdalla.getID());
    }
    
    
    /**
     * Asettaa käyttöliittymässä käytettävän Kappalekirjaston
     * @param kappalekirjasto käyttöliittymässä käytettävä kappalekirjasto
     */
    public void setKappalekirjasto(Kappalekirjasto kappalekirjasto) {
        this.kappalekirjasto = kappalekirjasto;
    }

        
    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }
    
    
    /**
     * Tietojen tallentaminen 
     * @return null, jos onnistuu, muuten virhe tekstinä 
     */
    private String tallenna()  {
        try {
            kappalekirjasto.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia: " + ex.getMessage());
            return ex.getMessage();
        }  
    }
    
    
    /**
     * Version tulostaminen 
     */
    private void tulosta() {
        ModalController.showModal(KappalekirjastoGUIController.class.getResource("TulostusView.fxml"), 
                "Tulosta versio", null, "");
    }

    
    @SuppressWarnings("unused")
    private void tulosta(PrintStream os, final Kappale kappale) {
        os.println("-----------------------------------------");
        kappale.tulosta(os);
        os.println("-----------------------------------------");
        List<Versio> versiot = kappalekirjasto.annaVersiot(kappale);
        for (Versio ver : versiot) {
            ver.tulosta(os);
            os.println();
        }
        
    }

    
    /**
     * Uuden kappaleen lisääminen 
     */
    protected void uusiKappale() {
        Kappale uusi = new Kappale(); 
        uusi = KappaleDialogController.kysyKappale(null, uusi);
        if (uusi == null) return;
        uusi.rekisteroi();
        kappalekirjasto.lisaa(uusi);
        hae(uusi.getID());            
    }
    
    
    /**
     * Hakee uuden kappaleen tiedot listaan
     * @param id uuden kappaleen id
     */
    protected void hae(int id) {
        chooserKappaleet.clear();
        String haettu = hakuehto.getText();
        int index = 0;        
        Collection<Kappale> kappaleet;
        
        try {
            
            kappaleet = kappalekirjasto.etsi(haettu, 1);  
            
            int i = 0;
            for (Kappale kappale : kappaleet) {
                if (kappale.getID() == id) index = i;
                chooserKappaleet.add(kappale.getNimi(), kappale);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Kappaleen hakemisessa ongelmia: " + ex.getMessage());
        }
        
        chooserKappaleet.setSelectedIndex(index);
    }


    /**
     * Uuden version lisääminen 
     */
    protected void uusiVersio() {        
        if ( kappaleKohdalla == null ) return;
        
        Versio uusi = new Versio();  
        uusi = VersioDialogController.kysyVersio(null, uusi, "Uusi Versio");
        if (uusi == null) return;
        uusi.rekisteroi();
        uusi.setKappaleTunnus(kappaleKohdalla.getID());
        kappalekirjasto.lisaa(uusi);
        
        hae(kappaleKohdalla.getID());
    }
    
    
    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }
}
