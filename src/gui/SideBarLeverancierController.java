package gui;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domein.DomeinController;
import domein.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SideBarLeverancierController extends HBox {
	@FXML
	private HBox hBoxSidebarLeverancier;

	@FXML
	private Button toonBestellingenBtn;

	@FXML
	private Button toonKlantenBtn;

	@FXML
	private Button voorraadbeherenBtn;

	@FXML
	private Button voorraadtoevoegenBtn;

	private Set<Button> sideButtons;

	private DomeinController domeincontroller;
	private BestellingScherm bestellingScherm = new BestellingScherm();
	private BestellingSchermController bestellingSchermController;

	private KlantenSchermController klantenScherm;

	private LijstProductenLeverancierController lijstProductenScherm;

	private AddProductSchermController addProductScherm;

	public SideBarLeverancierController(DomeinController domeincontroller) {
		this.domeincontroller = domeincontroller;

		bestellingSchermController = new BestellingSchermController(bestellingScherm, domeincontroller);
		klantenScherm = new KlantenSchermController(domeincontroller);
		lijstProductenScherm = new LijstProductenLeverancierController(domeincontroller);
		addProductScherm = new AddProductSchermController(domeincontroller, lijstProductenScherm);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("SideBarLeverancier.fxml"));
		loader.setRoot(this);

		loader.setController(this);

		try {

			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		sideButtons = new HashSet<>(
				List.of(toonBestellingenBtn, toonKlantenBtn, voorraadtoevoegenBtn, voorraadbeherenBtn));

		toonNieuweSchermNaastSidebar(bestellingScherm);
		setActiveBtn(toonBestellingenBtn);
		bestellingScherm.setPrefSize(1000, 1000);
	}

	@FXML
	private void toonBestellingen() {
		verwijderHuidigeSchermNaastSidebar();
		toonNieuweSchermNaastSidebar(bestellingScherm);
		setActiveBtn(toonBestellingenBtn);
	}

	@FXML
	private void toonKlanten() {
		verwijderHuidigeSchermNaastSidebar();
		toonNieuweSchermNaastSidebar(klantenScherm);

		setActiveBtn(toonKlantenBtn);
	}

	@FXML
	private void voorraadtoevoegen() {
		verwijderHuidigeSchermNaastSidebar();
		toonNieuweSchermNaastSidebar(addProductScherm);
		setActiveBtn(voorraadtoevoegenBtn);
	}

	@FXML
	private void voorraadbeheren() {
		verwijderHuidigeSchermNaastSidebar();
		toonNieuweSchermNaastSidebar(lijstProductenScherm);
		setActiveBtn(voorraadbeherenBtn);
	}

	@FXML
	private void logoutLeverancier() {
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

	private void verwijderHuidigeSchermNaastSidebar() {
		hBoxSidebarLeverancier.getChildren().remove(1);

	}

	private void toonNieuweSchermNaastSidebar(Pane scherm) {
		hBoxSidebarLeverancier.getChildren().add(scherm);
	}

	private void setActiveBtn(Button btn) {
		btn.setStyle("-fx-background-color: #941914;");
		sideButtons.stream().filter(button -> !button.equals(btn))
				.forEach(otherBtn -> otherBtn.setStyle("-fx-background-color: C42728;"));
	}

}
