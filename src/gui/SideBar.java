package gui;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domein.AdminDomeinController;
import domein.DomeinController;
import domein.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SideBar extends HBox {

	@FXML
	private HBox hBoxchild;

	@FXML
	private VBox vBox;

	@FXML
	private Button toonBedrijvenBtn;

	@FXML
	private Button toonVoegBedrijfBtn;

	@FXML
	private Button beheerProfielenBtn;

	AdminDomeinController adminDomeinController;

	private BedrijfScherm bedrijfScherm;
	private AddBedrijfScherm abs;

	private AddBedrijfSchermController absc;
	private LijstVeranderingVerzoekenController verzoekenScherm;

	private Set<Button> sideButtons;

	public SideBar(AdminDomeinController adminDomeinController) {

		this.adminDomeinController = adminDomeinController;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SideBar.fxml"));
		loader.setRoot(this);

		loader.setController(this);

		try {

			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		sideButtons = new HashSet<>(List.of(toonBedrijvenBtn, toonVoegBedrijfBtn, beheerProfielenBtn));

		maakSchermenKlaar();
		toonNieuweSchermNaastSidebar(bedrijfScherm);
		setActiveBtn(toonBedrijvenBtn);

	}

	private void maakSchermenKlaar() {
		bedrijfScherm = new BedrijfScherm(adminDomeinController);
		abs = new AddBedrijfScherm();
		absc = new AddBedrijfSchermController(abs, adminDomeinController);
		verzoekenScherm = new LijstVeranderingVerzoekenController(adminDomeinController);
	}

	private void setActiveBtn(Button btn) {
		btn.setStyle("-fx-background-color: #941914;");
		sideButtons.stream().filter(button -> !button.equals(btn))
				.forEach(otherBtn -> otherBtn.setStyle("-fx-background-color: C42728;"));
	}

	@FXML
	private void logout() {
		if (this.adminDomeinController.logout()) {
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
	private void toonVoegBedrijf() {
		verwijderHuidigeSchermNaastSidebar();
		toonNieuweSchermNaastSidebar(abs);
		setActiveBtn(toonVoegBedrijfBtn);
	}

	@FXML
	private void toonBedrijven() {
		verwijderHuidigeSchermNaastSidebar();
		bedrijfScherm.updateDataTable();
		toonNieuweSchermNaastSidebar(bedrijfScherm);
		setActiveBtn(toonBedrijvenBtn);
	}

	@FXML
	private void beheerProfielen() {
		verwijderHuidigeSchermNaastSidebar();
		toonNieuweSchermNaastSidebar(verzoekenScherm);
		setActiveBtn(beheerProfielenBtn);
	}

	private void verwijderHuidigeSchermNaastSidebar() {
		hBoxchild.getChildren().remove(1);

	}

	private void toonNieuweSchermNaastSidebar(Pane scherm) {
		hBoxchild.getChildren().add(scherm);
	}

}