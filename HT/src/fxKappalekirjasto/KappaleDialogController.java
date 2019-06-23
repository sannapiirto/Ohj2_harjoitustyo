package fxKappalekirjasto;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kappalekirjasto.Kappale;


/**
 * T‰m‰ ei ole viel‰ k‰ytˆss‰ 
 * @author sanna
 * @version 6.6.2018
 *
 */
public class KappaleDialogController implements ModalControllerInterface<Kappale>,Initializable { 
	
    @FXML private TextField editNimi;
    @FXML private TextField editEsittaja;
    @FXML private TextField editJulkaisuVuosi;
    @FXML private TextField editGenre;
    @FXML private TextArea editKommentit;
    @FXML private Label labelVirhe;

    @FXML private void handleOK() {
        if (kappaleKohdalla != null && kappaleKohdalla.getNimi().trim().equals("") ) {
            naytaVirhe("Nimi ei saa olla tyhj‰!");
            return;
        }

        ModalController.closeStage(labelVirhe);
    }
    
    @FXML private void handleCancel() {
        kappaleKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }
    
    // ========================================================================================

    private Kappale kappaleKohdalla; 
    private TextField[] edits;

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }
    
    
    /**
     * ALustaa edits-taulukoiden arvot, sek‰ textArea-tyyppisen kommentti-kent‰n arvon 
     */
    protected void alusta() {
        edits = new TextField[] {editNimi, editEsittaja, editJulkaisuVuosi, editGenre};
        int i = 0;
        
        for (TextField edit : edits) {
            final int k = ++i;
            edit.setOnKeyReleased(e -> kasitteleMuutosKappaleeseen(k, (TextField) e.getSource()));
        }
        
        editKommentit.setOnKeyReleased(e -> kasitteleMuutosKappaleeseen((TextArea) e.getSource()));
    }
    

    /**
     * Avataan muokkausn‰kym‰
     * @param otsikko teksti joka n‰ytet‰‰n dialogin otsikkona 
     */
    public static void avaa(String otsikko)  {
        ModalController.showModal(KappalekirjastoGUIController.class.getResource("KappaleDialogView.fxml"), 
                otsikko, null, "");
    }
        
    
    /**
     * Tyhjent‰‰ edits-taulukon kenttien arvot
     * @param edits tyhjennett‰v‰ edits-taulukko
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit : edits) {
            edit.setText("");
        }
    }
    
        
    private void kasitteleMuutosKappaleeseen(TextArea edit) {
        if (kappaleKohdalla == null) return;
        String s = edit.getText();
        String virhe = kappaleKohdalla.setKommentti(s);
        
        if (virhe == null ) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");  // TODO kent‰n taustav‰ri punaiseksi ?? 
            naytaVirhe(virhe);
        } 
        
        else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }    
    }

    
    private void kasitteleMuutosKappaleeseen(int k, TextField edit) {
        if (kappaleKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
            case 1 : virhe = kappaleKohdalla.setKappaleenNimi(s); break;
            case 2 : virhe = kappaleKohdalla.setEsittajanNimi(s); break;
            case 3 : virhe = kappaleKohdalla.setJulkaisuVuosi(s); break;
            case 4 : virhe = kappaleKohdalla.setGenre(s); break;
            default:
            break;
        }
        
        if (virhe == null ) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } 
        
        else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }


    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    
    /**
     * N‰ytt‰‰ kappaleen tiedot
     * @param edits TextField- tyyppiset kent‰t taulukossa
     * @param kommentit TextArea-tyyppinen kommentti-kentt‰
     * @param kappale kappale, jonka tiedot n‰ytet‰‰n 
     */
    public static void naytaKappale(TextField[] edits, TextArea kommentit, Kappale kappale) {
        if (kappale == null) return;
        edits[0].setText(kappale.getNimi());
        edits[1].setText(kappale.getEsittajanNimi());
        edits[2].setText("" + kappale.getJulkaisuVuosi());
        edits[3].setText(kappale.getGenre());
        kommentit.setText(kappale.getKommentti());
    }
   

    /**
     * Kysyy kappaleen tiedot
     * @param modalityStage stage, jolle ollaan modaalisia 
     * @param oletus oletuksena k‰ytett‰v‰ kappale
     * @return kappaleen tiedot
     */
    public static Kappale kysyKappale(Stage modalityStage, Kappale oletus) {
        return ModalController.<Kappale, KappaleDialogController>showModal (
                KappaleDialogController.class.getResource("KappaleDialogView.fxml"), "Kappalekirjasto",
                modalityStage, oletus, null);
        
    }


    @Override
    public void handleShown() {
        // TODO Auto-generated method stub        
    }

    
    @Override
    public Kappale getResult() {
        return kappaleKohdalla;
    }
    
    
    @Override
    public void setDefault(Kappale oletus) {
        kappaleKohdalla = oletus;
        naytaKappale(edits, editKommentit, kappaleKohdalla);
    }
}
