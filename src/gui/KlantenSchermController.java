package gui;

import java.io.IOException;
import java.util.Map;

import domein.DomeinController;
import domein.IKlant;
import domein.ILeverancier;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

public class KlantenSchermController extends VBox {

	@FXML
	private TableColumn<Map<IKlant, Long>, Long> aantalBestellingenCol;

	@FXML
	private TableColumn<Map<IKlant, String>, String> klantBedrijfNaamCol;

	@FXML
	private TableView<Map<IKlant, Long>> klantenTableView;

	@FXML
	private TextField txtKlantFilter;

	private DomeinController domeincontroller;
	private ILeverancier leverancier;

	public KlantenSchermController(DomeinController dc) {
		this.domeincontroller = dc;
		this.leverancier = leverancier;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("KlantenScherm.fxml"));

		loader.setRoot(this);

		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		buildTable(this.domeincontroller.getKlantenLeverancier(""));
		behandelKlik();
	}

	private void behandelKlik() {
		this.klantenTableView.setRowFactory(tv -> {
			TableRow<Map<IKlant, Long>> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					Map<IKlant, Long> rowData = row.getItem();
					IKlant klant = rowData.keySet().iterator().next();
					showBedrijfDetails(klant);
				}
			});
			return row;
		});
	}

	private KlantDetailScherm findExistingDetailsScherm() {
		for (Node node : this.getChildren()) {
			if (node instanceof KlantDetailScherm) {
				return (KlantDetailScherm) node;
			}
		}
		return null;
	}

	private void showBedrijfDetails(IKlant klant) {
		KlantDetailScherm existingDetailsScherm = findExistingDetailsScherm();
		if (existingDetailsScherm != null) {
			this.getChildren().remove(existingDetailsScherm);
		}

		KlantDetailScherm newDetailsScherm = new KlantDetailScherm(domeincontroller, klant, this);
		this.getChildren().add(newDetailsScherm);

	}

	private void buildTable(ObservableList<IKlant> klanten) {
		klantBedrijfNaamCol.setCellValueFactory(data -> {
			IKlant klant = data.getValue().keySet().iterator().next();
			return new SimpleStringProperty(klant.getUsername());
		});

		aantalBestellingenCol.setCellValueFactory(data -> {
			Long aantalBestellingen = data.getValue().values().iterator().next();
			return new SimpleObjectProperty<>(aantalBestellingen);
		});

		ObservableList<Map<IKlant, Long>> data = FXCollections.observableArrayList();

		klanten.stream().forEach(klant -> {
			data.add(this.domeincontroller.aantalBestellingenOpen(klant));

		});

		this.klantenTableView.setItems(data);

	}

	@FXML
	private void filter(KeyEvent event) {
		String filterValue = txtKlantFilter.getText().replace(" ", "").toLowerCase();
		buildTable(this.domeincontroller.getKlantenLeverancier(filterValue));
	}

	public void updateTable() {
		buildTable(this.domeincontroller.getKlantenLeverancier(""));
	}

}