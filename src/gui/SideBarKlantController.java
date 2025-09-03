package gui;

import java.io.IOException;

import domein.DomeinController;
import domein.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SideBarKlantController extends HBox {

	@FXML
	private HBox hBoxSidebarKlant;

	@FXML
	private Button toonBestellingenBtn;

	private DomeinController domeincontroller;

	private BestellingScherm bestellingScherm = new BestellingScherm();
	private BestellingSchermController bestellingSchermController;

	public SideBarKlantController(DomeinController domeincontroller) {
		this.domeincontroller = domeincontroller;
		this.bestellingSchermController = new BestellingSchermController(bestellingScherm, domeincontroller);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("SideBarKlant.fxml"));
		loader.setRoot(this);

		loader.setController(this);

		try {

			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		hBoxSidebarKlant.getChildren().add(bestellingScherm);
		bestellingScherm.setPrefSize(1000, 1000);

		toonBestellingenBtn.setStyle("-fx-background-color: #941914;");
	}

	@FXML
	void logoutKlant() {
		if (domeincontroller.logout()) {
			LoginScherm loginScherm = new LoginScherm();

			LoginController lc = new LoginController();

			LoginSchermController lsc = new LoginSchermController(loginScherm, lc, new DomeinController(lc));
			Scene scene = new Scene(loginScherm, 1250, 800);

			Stage stage = (Stage) this.getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}
	}

	@FXML
	void toonBestellingen() {

	}
}
