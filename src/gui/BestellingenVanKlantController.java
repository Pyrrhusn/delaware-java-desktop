package gui;

import java.io.IOException;

import domein.DomeinController;
import domein.IBestelling;
import domein.IKlant;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BestellingenVanKlantController extends VBox implements BestellingenScherm {

	@FXML
	private TableView<IBestelling> bestellingenKlantTabel;

	@FXML
	private TableColumn<IBestelling, String> betalingsStatusCol;

	@FXML
	private TableColumn<IBestelling, String> datumGeplaatsCol;

	@FXML
	private TableColumn<IBestelling, String> klantNaamCol;

	@FXML
	private TableColumn<IBestelling, String> orderIDcol;

	@FXML
	private TableColumn<IBestelling, String> orderStatusCol;

	@FXML
	private Button keerTerugNaarKlantBtn;

	@FXML
	private TextField filterBestellingenKlant;

	private DomeinController domeincontroller;
	private IKlant gekozenKlant;
	private KlantenSchermController kds;

	public BestellingenVanKlantController(DomeinController domeincontroller, IKlant gekozenKlant,
			KlantenSchermController kds) {
		this.domeincontroller = domeincontroller;
		this.gekozenKlant = gekozenKlant;
		this.kds = kds;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BestellingenVanKlant.fxml"));

		loader.setRoot(this);

		loader.setController(this);

		try {

			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		build();
		behandelKlik();
	}

	private void build() {

		orderIDcol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderID()));
		klantNaamCol.setCellValueFactory(
				data -> new SimpleStringProperty(data.getValue().getKlant().getBedrijf().getNaam()));
		datumGeplaatsCol
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDatumGeplaatst().toString()));
		orderStatusCol
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderstatus().getStatus()));
		betalingsStatusCol
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isBetalingsstatus().getStatus()));

		setTableItems(domeincontroller.getOpenKlantBestellingenLeverancier(gekozenKlant, ""));
	}

	private void setTableItems(ObservableList<IBestelling> bestellingen) {
		bestellingenKlantTabel.setItems(bestellingen);
	}

	private BestellingSchermDetails findExistingDetailsScherm() {
		for (Node node : this.getChildren()) {
			if (node instanceof BestellingSchermDetails) {
				return (BestellingSchermDetails) node;
			}
		}
		return null;
	}

	private void showOrderDetails(IBestelling rowData, DomeinController dc) {
		BestellingSchermDetails existingDetailsScherm = findExistingDetailsScherm();

		if (existingDetailsScherm != null) {
			this.getChildren().remove(existingDetailsScherm);
		}

		BestellingSchermDetails newDetailsScherm = new BestellingSchermDetails(dc, rowData, this);
		this.getChildren().add(newDetailsScherm);

	}

	private void behandelKlik() {
		bestellingenKlantTabel.setRowFactory(tv -> {
			TableRow<IBestelling> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					IBestelling rowData = row.getItem();
					showOrderDetails(rowData, domeincontroller);
				}
			});
			return row;
		});
	}

	@FXML
	void keerTerugNaarKlant() {
		Stage stage = (Stage) (getScene().getWindow());
		stage.setScene(kds.getScene());
		kds.updateTable();
	}

	@Override
	public void updateTableColumns(IBestelling bestelling) {
		bestellingenKlantTabel.getItems().clear();
		bestellingenKlantTabel.getItems()
				.addAll(domeincontroller.getOpenKlantBestellingenLeverancier(gekozenKlant, ""));
		build();
		bestellingenKlantTabel.getSelectionModel().select(bestelling);
	}

	@FXML
	private void filter(KeyEvent event) {
		String filterValue = filterBestellingenKlant.getText().replace(" ", "").toLowerCase();
		setTableItems(domeincontroller.getOpenKlantBestellingenLeverancier(gekozenKlant, filterValue));
	}
}
