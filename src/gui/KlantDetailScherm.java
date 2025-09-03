package gui;

import java.io.IOException;
import java.util.stream.Collectors;

import domein.DomeinController;
import domein.IKlant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class KlantDetailScherm extends HBox {

	@FXML
	private Label klantBedrijfAdresLabel;

	@FXML
	private Label klantBedrijfNaamLabel;

	@FXML
	private ImageView klantLogoImageView;

	@FXML
	private Label klantBedrijfContactgegevensLabel;

	@FXML
	private Button toonBestellingenKlantBtn;

	private DomeinController domeincontroller;
	private IKlant klant;
	private KlantenSchermController ks;

	public KlantDetailScherm(DomeinController domeincontroller, IKlant klant, KlantenSchermController ks) {
		this.domeincontroller = domeincontroller;
		this.klant = klant;
		this.ks = ks;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("KlantDetailScherm.fxml"));

		loader.setRoot(this);

		loader.setController(this);

		try {

			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		build();
	}

	private void build() {
		klantLogoImageView.setImage(new Image(klant.getBedrijf().getLogoUrl()));
		klantBedrijfNaamLabel.setText(this.klant.getUsername());

		klantBedrijfAdresLabel.setText(this.klant.getBedrijf().getAdres());

		String contactGegevensString = klant.getBedrijf().getContactGegevens().entrySet().stream()
				.map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n"));

		klantBedrijfContactgegevensLabel.setText(contactGegevensString);

	}

	@FXML
	private void toonBestellingenKlant() {
		BestellingenVanKlantController bestellingenVanKlant = new BestellingenVanKlantController(domeincontroller,
				klant, ks);

		Scene scene = new Scene(bestellingenVanKlant, 1280, 800);
		Stage stage = (Stage) this.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

}