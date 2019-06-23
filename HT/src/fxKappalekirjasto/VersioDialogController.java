package fxKappalekirjasto;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kappalekirjasto.Versio;

/**
 * T‰m‰ ei ole viel‰ k‰ytˆss‰ 
 * @author sanna
 * @version 6.6.2018
 *
 */
public class VersioDialogController implements ModalControllerInterface<Versio>,Initializable {

    @FXML TextField editKappale;
    @FXML TextField editSavellaji;
    @FXML CheckBox  editAlkuperainen;
    @FXML CheckBox  editValmis;
    @FXML TextArea  editKommentit;
    @FXML TextArea  editSoinnut;
    
    @FXML Label labelVirhe; 
    
    
    @FXML void handleTallenna()  {
        tallenna();
    }
    
    @FXML void handlePeruuta()  {
        versioKohdalla = null;
        ModalController.closeStage(peruuta);
    }
    
    @FXML Button peruuta;
    
    // ===============================================================================================
    
    private Versio versioKohdalla;
    

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
        
    }

        
    /**
     * Asettaa kentt‰-attribuuteille tapahtuman-kuuntelijat
     */
    protected void alusta() {    
        editSavellaji.setOnKeyReleased(e -> kasitteleMuutosVersioon((TextField) e.getSource()));
        editAlkuperainen.setOnKeyReleased(e -> kasitteleMuutosVersioon(1, (CheckBox) e.getSource()));
        editValmis.setOnKeyReleased(e -> kasitteleMuutosVersioon(2, (CheckBox) e.getSource()));
        editKommentit.setOnKeyReleased(e -> kasitteleMuutosVersioon(1, (TextArea) e.getSource()));
        editSoinnut.setOnKeyReleased(e -> kasitteleMuutosVersioon(2, (TextArea) e.getSource()));
    }
    
    
    /**
     * K‰sittelee muutokseen TextArea-kentt‰‰n 
     * @param k tapauksen numero
     * @param edit k‰sitelt‰v‰ kentt‰ 
     */
    private void kasitteleMuutosVersioon(int k, TextArea edit) {
        if (versioKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        
        switch (k) {
            case 1 : virhe = versioKohdalla.setKommentti(s); break;
            case 2 : virhe = versioKohdalla.setSoinnut(s); break;
            default : 
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


    /**
     * K‰sittelee muutokseen CheckBox-kentt‰‰n 
     * @param k tapauksen numero
     * @param edit k‰sitelt‰v‰ kentt‰ 
     */
    private void kasitteleMuutosVersioon(int k, CheckBox edit) {
        if (versioKohdalla == null) return;
        // String s = edit.getText();
        String virhe = null;
        
        int asetettava = 0;
        if (edit.isSelected()) asetettava = 1;
                
        switch (k) {
            case 1 : virhe = versioKohdalla.setAlkuperainen(asetettava); break;
            case 2 : virhe = versioKohdalla.setValmis(asetettava); break;
            default : 
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

    
    /**
     * K‰sittelee muutokseen TextField-kentt‰‰n 
     * @param edit k‰sitelt‰v‰ kentt‰ 
     */
    private void kasitteleMuutosVersioon(TextField edit) {
        if (versioKohdalla == null) return;
        String s = edit.getText();
        String virhe = versioKohdalla.setSavellaji(s);
                
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
    

    
    /**
     * N‰ytt‰‰ virheen, mik‰li kentt‰‰n syˆtetty arvo ei mene oikeellisuustarkistuksesta l‰pi
     * @param virhe esitett‰v‰ virhe
     */
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
     * N‰ytt‰‰ version tiedot muokkausn‰kym‰ss‰ 
     * @param versio N‰ytett‰v‰ versio
     * @param savellaji S‰vellaji-kentt‰
     * @param valmis valmis-checkbox
     * @param alkuperainen alkuper‰inen-checkbox
     * @param kommentti kommentti-kentt‰ 
     * @param soinnut soinnut-kentt‰ 
     */
    public static void naytaVersio(Versio versio, TextField savellaji, CheckBox valmis, CheckBox alkuperainen, TextArea kommentti, TextArea soinnut) {
        if (versio == null ) return;
        savellaji.setText(versio.getSavellaji());
        if (versio.getValmis() == 1) valmis.setSelected(true);
        if (versio.getAlkuperainen() == 1) alkuperainen.setSelected(true);
        kommentti.setText(versio.getKommentti());
        soinnut.setText(versio.getSoinnut());
    }


    /**
     * Kysyy version tiedot
     * @param modalityStage stage, jolle ollaan modaalisia
     * @param oletus versio, jota k‰ytet‰‰n oletukena 
     * @param otsikko otsikkona k‰ytett‰v‰ teksti
     * @return version
     */
    public static Versio kysyVersio(Stage modalityStage, Versio oletus, String otsikko) { 
        return ModalController.<Versio, VersioDialogController>showModal (
                VersioDialogController.class.getResource("VersioDialogView.fxml"), otsikko,
                modalityStage, oletus, null);
    }
    
    
    /**
     * Avataan Version-muokkaus tai luonti-n‰kym‰
     * @param otsikko dialogin otsikko
     */
    public static void avaa(String otsikko)  {
        ModalController.showModal(KappalekirjastoGUIController.class.getResource("VersioDialogView.fxml"), otsikko, null, "");
    }
    
    
    private void tallenna() {
        ModalController.closeStage(peruuta);
    }
    
    
    @Override
    public void handleShown() {
        // TODO : t‰t‰ ei ole toistaiseksi tarvittu         
    }

    
    @Override
    public Versio getResult() {
        return versioKohdalla;
    }

    
    @Override
    public void setDefault(Versio oletus) {
        versioKohdalla = oletus;
        naytaVersio(versioKohdalla, editSavellaji, editAlkuperainen, editValmis, editKommentit, editSoinnut);    
    }    
}
