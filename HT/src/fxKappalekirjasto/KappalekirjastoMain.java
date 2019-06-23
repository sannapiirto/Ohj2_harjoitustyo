package fxKappalekirjasto;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import kappalekirjasto.Kappalekirjasto;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author sanna
 * @version 6.6.2018
 * Pääohjelma Kappalekirjasto-ohjelman käynnistämiseksi
 */
public class KappalekirjastoMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    
		    final FXMLLoader ldr = new FXMLLoader(getClass().getResource("KappalekirjastoGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
		    // final BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("KappalekirjastonNimiView.fxml"));
		    final KappalekirjastoGUIController appCtrl = (KappalekirjastoGUIController)ldr.getController(); 
				    
			final Scene scene = new Scene(root);
			
			scene.getStylesheets().add(getClass().getResource("kappalekirjasto.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Kappalekirjasto");
			// primaryStage.show();
        
			primaryStage.setOnCloseRequest((event) -> {
                if ( !appCtrl.voikoSulkea() ) event.consume();
            });
			
			Kappalekirjasto kappalekirjasto = new Kappalekirjasto();
            appCtrl.setKappalekirjasto(kappalekirjasto);
			
            primaryStage.show();
            if ( !appCtrl.avaa() ) Platform.exit();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Käynnistetään käyttöliittymä
	 * @param args komentorivin parametrit 
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
