package gui;

import java.io.IOException;

import domein.AdminDomeinController;
import domein.IBedrijf;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

public class BedrijfScherm extends VBox {

	@FXML
	private TableColumn<IBedrijf, Integer> aantalKlantenBedrijf;

	@FXML
	private TableColumn<IBedrijf, String> bedrijfAdres;

	@FXML
	private TableColumn<IBedrijf, String> bedrijfnaam;

	@FXML
	private TableColumn<IBedrijf, String> bedrijfsector;

	@FXML
	private TableView<IBedrijf> tableViewBedrijf;

	private AdminDomeinController adc;

	public BedrijfScherm(AdminDomeinController adc) {
		this.adc = adc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BedrijfScherm.fxml"));

		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		setSpacing(10);
		setPadding(new Insets(10));

		buildTable();
		behandelKlik();
	}

	private void behandelKlik() {
		this.tableViewBedrijf.setRowFactory((TableView<IBedrijf> tv) -> {
			TableRow<IBedrijf> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					IBedrijf rowData = row.getItem();
					showBedrijfDetails(rowData);
				}
			});
			return row;
		});

	}

	private BedrijfSchermDetails findExistingDetailsScherm() {
		for (Node node : this.getChildren()) {
			if (node instanceof BedrijfSchermDetails) {
				return (BedrijfSchermDetails) node;
			}
		}
		return null;
	}

	private void showBedrijfDetails(IBedrijf bedrijf) {

		BedrijfSchermDetails existingDetailsScherm = findExistingDetailsScherm();
		if (existingDetailsScherm != null) {
			this.getChildren().remove(existingDetailsScherm);
		}

		BedrijfSchermDetails newDetailsScherm = new BedrijfSchermDetails(adc, bedrijf, this);
		newDetailsScherm.setPrefWidth(tableViewBedrijf.getWidth());
		this.getChildren().add(newDetailsScherm);
	}

	private void buildTable() {
		bedrijfAdres.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdres()));
		bedrijfnaam.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNaam()));
		bedrijfsector.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSector()));
		aantalKlantenBedrijf.setCellValueFactory(
				data -> new SimpleIntegerProperty(data.getValue().getLeverancier().getAantalKlanten()).asObject());
		setItems();
	}

	private void setItems() {
		this.tableViewBedrijf.setItems(adc.getBedrijven());

	}

	public void updateDataTable() {
		tableViewBedrijf.refresh();
	}

	public void updateTabellen(IBedrijf bedrijf2) {
		tableViewBedrijf.refresh();
		tableViewBedrijf.getSelectionModel().select(bedrijf2);
	}

}