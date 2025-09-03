package main;

import java.time.LocalDate;

import domein.DomeinController;
import domein.EmailSender;
import domein.IBestelling;
import domein.LoginController;
import domein.StuurMeldingController;
import gui.LoginScherm;
import gui.LoginSchermController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startup extends Application {

	@Override
	public void start(Stage primaryStage) {
//		new BevolkenDB().run();
		StuurMeldingController sm = new StuurMeldingController();
		EmailSender es = new EmailSender();

		for (IBestelling b : sm.haalBestellingenVoorBetalingsherinnering()) {
			sm.werkDatumBetalingsHerinneringbij(b, LocalDate.now());
			// es.stuurHerinnering(b);
		}

		sm.werkHeeftBetalingsherinneringBij();

		LoginScherm loginScherm = new LoginScherm();
		LoginController lc = new LoginController();

		LoginSchermController lsc = new LoginSchermController(loginScherm, lc, new DomeinController(lc));
		Scene scene = new Scene(loginScherm, 1250, 800);

		scene.getStylesheets().add(getClass().getResource("/css/bedrijfScherm.css").toExternalForm());

		primaryStage.setScene(scene);

		primaryStage.setTitle("Delaware b2b");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}