package gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import domein.AdminDomeinController;
import domein.IBedrijf;
import domein.IBedrijfVerandering;
import exception.InformationRequiredException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VergelijkGegevensBedrijfController extends VBox {

	@FXML
	private Button aanvaardenBtn;

	@FXML
	private Button afkeurenBtn;

	@FXML
	private HBox hBoxBeneden;

	@FXML
	private HBox hBoxBoven;

	@FXML
	private Button terug;

	private BedrijfSchermDetails bdOud;

	private VergelijkGegevensDetailsController bdNieuw;

	private AdminDomeinController adc;
	private IBedrijfVerandering verandering;
	private LijstVeranderingVerzoekenController vorigeScherm;

	private IBedrijf bedrijf;

	public VergelijkGegevensBedrijfController(AdminDomeinController adc, IBedrijfVerandering verandering,
			LijstVeranderingVerzoekenController vorigeScherm) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("VergelijkGegevensBedrijf.fxml"));
		loader.setRoot(this);

		loader.setController(this);

		try {

			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		this.vorigeScherm = vorigeScherm;
		bdOud = new BedrijfSchermDetails(adc, verandering.getBedrijf(), null);
		bdOud.verbertWijzigButton();
		bdNieuw = new VergelijkGegevensDetailsController(adc, verandering, vorigeScherm);

		this.adc = adc;
		this.verandering = verandering;
		this.bedrijf = verandering.getBedrijf();
		this.setSpacing(10);
		build();
	}

	private void build() {
		hBoxBoven.getChildren().add(bdOud);

		hBoxBeneden.getChildren().add(0, bdNieuw);
	}

	@FXML
	private void aanvaarden() {
		try {
			this.adc.checkWijzigingBedrijf(verandering.getBedrijf(), verandering.getLogo(), verandering.getSector(),
					verandering.getAdres(), verandering.getContactGegevens().get("contactpersoon"),
					verandering.getContactGegevens().get("telefoon"), verandering.getContactGegevens().get("email"),
					verandering.getBetalingsInfo());
			verandering.setGoedgekeurd(true);
			this.adc.updateBedrijf(bedrijf);
			showSuccessNotification("Bedrijf bijgewerkt", "Het bedrijf is succesvol bijgewerkt.");
			verandering.setIBedrijf(bedrijf);
			vorigeScherm.updateTabel(bedrijf, verandering);
			terug();
		} catch (InformationRequiredException e) {
			this.showErrors(e.getInformationRequired().values());
		} catch (RuntimeException e) {
			this.showErrors(Arrays.asList(e.getMessage()));
		}
	}

	@FXML
	private void afkeuren() {
		this.adc.afkeuren(this.verandering);
		this.adc.updateBedrijf(verandering.getBedrijf());
		showSuccessNotification("Afgekeurd", "U heeft de gevraagde wijzigingen niet toegestaan.");
		terug();
	}

	private void showSuccessNotification(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
		vorigeScherm.updateTabel(bedrijf, verandering);
	}

	public void showErrors(Collection<String> errors) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText(null);

		String errorMessage = errors.stream().collect(Collectors.joining("\n"));

		alert.setContentText(errorMessage);

		DialogPane dialogPane = alert.getDialogPane();

		dialogPane.setMinHeight(Region.USE_PREF_SIZE);

		alert.showAndWait();
	}

	@FXML
	private void terug() {
		Stage stage = (Stage) (getScene().getWindow());
		stage.setScene(vorigeScherm.getScene());
	}
}
