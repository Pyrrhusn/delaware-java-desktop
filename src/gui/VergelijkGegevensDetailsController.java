package gui;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import domein.AdminDomeinController;
import domein.BetalingsMethodesEnum;
import domein.IBedrijf;
import domein.IBedrijfVerandering;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class VergelijkGegevensDetailsController extends VBox {
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
	private IBedrijfVerandering bv;
	private LijstVeranderingVerzoekenController lvc;
	private String logo2;
	private String sector2;
	private String adres2;
	private List<BetalingsMethodesEnum> betalingsMethodes2;
	private String email2;
	private String telNr2;
	private String contactpersoon2;

	public VergelijkGegevensDetailsController(AdminDomeinController adc, IBedrijfVerandering bedrijfVerandering,
			LijstVeranderingVerzoekenController vorig) {
		this.adc = adc;
		this.bv = bedrijfVerandering;
		this.bedrijf = bv.getBedrijf();
		this.lvc = vorig;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("BedrijfSchermDetails.fxml"));

		loader.setRoot(this);

		loader.setController(this);

		try {

			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		if (bv.getLogo() == null || bv.getLogo().isEmpty()
				|| (bedrijf.getLogoUrl() != null && bedrijf.getLogoUrl().equals(bv.getLogo()))) {
			logoImageView.setImage(new Image(bedrijf.getLogoUrl()));
			this.logo2 = bedrijf.getLogoUrl();
		} else {
			logoImageView.setImage(new Image(bv.getLogo()));
			this.logo2 = bv.getLogo();
		}

		naamLbl.setText(bedrijf.getNaam());

		this.sector2 = setLabelText(sectorLbl, bv.getSector(), bedrijf.getSector());
		this.adres2 = setLabelText(adresLbl, bv.getAdres(), bedrijf.getAdres());

		if (bv.getBetalingsInfo() == null || bv.getBetalingsInfo().isEmpty() || (bedrijf.getBetalingsOpties() != null
				&& bedrijf.getBetalingsOpties().equals(bv.getBetalingsInfo()))) {
			betalingsmogelijkhedenLbl.setText(
					bedrijf.getBetalingsOpties().stream().map(Enum::toString).collect(Collectors.joining(", ")));
			this.betalingsMethodes2 = bedrijf.getBetalingsOpties();
		} else {
			betalingsmogelijkhedenLbl
					.setText(bv.getBetalingsInfo().stream().map(Enum::toString).collect(Collectors.joining(", ")));
			betalingsmogelijkhedenLbl.setTextFill(Color.RED);
			betalingsmogelijkhedenLbl.setStyle("-fx-font-weight: bold");
			this.betalingsMethodes2 = bv.getBetalingsInfo();
		}

		btwNrLbl.setText(bedrijf.getBTWNummer());

		if (bv.getContactGegevens() == null || bv.getContactGegevens().isEmpty()
				|| (bv.getContactGegevens() != null && bv.getContactGegevens().equals(bedrijf.getContactGegevens()))) {
			String contactGegevensString = bedrijf.getContactGegevens().entrySet().stream()
					.map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining(",  "));

			contactgegevensLbl.setText(contactGegevensString);
			this.telNr2 = bedrijf.getContactGegevens().get("telefoon");
			this.email2 = bedrijf.getContactGegevens().get("email");
			this.contactpersoon2 = bedrijf.getContactGegevens().get("contactpersoon");

		} else {
			String contactGegevensString = bv.getContactGegevens().entrySet().stream()
					.map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining(", "));

			contactgegevensLbl.setText(contactGegevensString);
			contactgegevensLbl.setTextFill(Color.RED);
			contactgegevensLbl.setStyle("-fx-font-weight: bold");
			this.telNr2 = bv.getContactGegevens().get("telefoon");
			this.email2 = bv.getContactGegevens().get("email");
			this.contactpersoon2 = bv.getContactGegevens().get("contactpersoon");
		}

		buildTabelKlantAccount(bedrijf);
		buildTabelLeverancierAccount(bedrijf);
		verbergButton();

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

	private String setLabelText(Label label, String verandering, String huidigeWaarde) {
		if (verandering == null || verandering.isBlank()
				|| (verandering != null && verandering.equals(huidigeWaarde))) {
			label.setText(huidigeWaarde);
			return huidigeWaarde;
		} else {
			label.setText(verandering);
			label.setTextFill(Color.RED);
			label.setStyle("-fx-font-weight: bold");
			return verandering;
		}
	}

	@FXML
	private void wijzigBedrijfGegevens(ActionEvent event) {
	}

	private void verbergButton() {
		wijzigBedrijfButton.setVisible(false);
	}

}
