package gui;

import java.io.IOException;
import java.util.stream.Collectors;

import domein.AdminDomeinController;
import domein.IBedrijf;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BedrijfSchermDetails extends VBox {

	@FXML
	private Label adresLbl;

	@FXML
	private Label betalingsmogelijkhedenLbl;

	@FXML
	private Label btwNrLbl;

	@FXML
	private Label contactgegevensLbl;

	@FXML
	private TableView<IBedrijf> klantAccountTableView;

	@FXML
	private TableColumn<IBedrijf, String> klantGebruikersnaamCol;

	@FXML
	private TableColumn<IBedrijf, String> klantIsActiefCol;

	@FXML
	private TableView<IBedrijf> leverancierAccountTableView;

	@FXML
	private TableColumn<IBedrijf, String> leverancierGebruikersnaamCol;

	@FXML
	private TableColumn<IBedrijf, String> leverancierIsActiefCol;

	@FXML
	private ImageView logoImageView;

	@FXML
	private Label naamLbl;

	@FXML
	private Label sectorLbl;

	@FXML
	private Button wijzigBedrijfButton;

	private AdminDomeinController adc;
	private IBedrijf bedrijf;
	private BedrijfScherm bs;

	public BedrijfSchermDetails(AdminDomeinController adc, IBedrijf bedrijf, BedrijfScherm bedrijfScherm) {
		this.adc = adc;
		this.bedrijf = bedrijf;
		this.bs = bedrijfScherm;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("BedrijfSchermDetails.fxml"));

		loader.setRoot(this);

		loader.setController(this);

		try {

			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		logoImageView.setImage(new Image(bedrijf.getLogoUrl()));
		naamLbl.setText(bedrijf.getNaam());
		sectorLbl.setText(bedrijf.getSector());
		adresLbl.setText(bedrijf.getAdres());
		betalingsmogelijkhedenLbl
				.setText(bedrijf.getBetalingsOpties().stream().map(Enum::toString).collect(Collectors.joining(", ")));
		btwNrLbl.setText(bedrijf.getBTWNummer());

		String contactGegevensString = bedrijf.getContactGegevens().entrySet().stream()
				.map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining(", "));

		contactgegevensLbl.setText(contactGegevensString);

		buildTabelKlantAccount(bedrijf);
		buildTabelLeverancierAccount(bedrijf);
	}

	private void buildTabelLeverancierAccount(IBedrijf bedrijf) {
		leverancierGebruikersnaamCol
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLeverancier().getUsername()));
		leverancierIsActiefCol.setCellValueFactory(
				data -> new SimpleStringProperty(data.getValue().isActiefLeverancier() ? "Ja" : "Nee"));

		leverancierAccountTableView.setItems(FXCollections.observableArrayList(bedrijf));
	}

	private void buildTabelKlantAccount(IBedrijf bedrijf) {
		klantGebruikersnaamCol
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKlant().getUsername()));
		klantIsActiefCol
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActiefKlant() ? "Ja" : "Nee"));

		klantAccountTableView.setItems(FXCollections.observableArrayList(bedrijf));
	}

	@FXML
	private void wijzigBedrijfGegevens(ActionEvent event) {
		AddBedrijfScherm abs = new AddBedrijfScherm();
		BeheerBedrijfSchermController bestellingenVanKlant = new BeheerBedrijfSchermController(abs, adc, bedrijf, this);

		Scene scene = new Scene(abs, 1280, 800);
		Stage stage = (Stage) this.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	public void updateTabel(IBedrijf bedrijf2) {
		bs.updateTabellen(bedrijf2);
		logoImageView.setImage(new Image(bedrijf.getLogoUrl()));
		naamLbl.setText(bedrijf.getNaam());
		sectorLbl.setText(bedrijf.getSector());
		adresLbl.setText(bedrijf.getAdres());
		betalingsmogelijkhedenLbl
				.setText(bedrijf.getBetalingsOpties().stream().map(Enum::toString).collect(Collectors.joining(", ")));
		btwNrLbl.setText(bedrijf.getBTWNummer());

		String contactGegevensString = bedrijf.getContactGegevens().entrySet().stream()
				.map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining(", "));

		contactgegevensLbl.setText(contactGegevensString);

		buildTabelKlantAccount(bedrijf);
		buildTabelLeverancierAccount(bedrijf);

	}

	public void verbertWijzigButton() {
		wijzigBedrijfButton.setVisible(false);
	}

}