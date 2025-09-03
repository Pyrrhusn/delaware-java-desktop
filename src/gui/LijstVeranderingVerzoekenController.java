package gui;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import domein.AdminDomeinController;
import domein.IBedrijf;
import domein.IBedrijfVerandering;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LijstVeranderingVerzoekenController extends VBox {

	@FXML
	private TableView<IBedrijfVerandering> tabelVerzoeken;

	@FXML
	private TableColumn<IBedrijfVerandering, String> aangevraagOpCol;

	@FXML
	private TableColumn<IBedrijfVerandering, String> gebruikerCol;

	@FXML
	private HBox hBoxDetails;

	private AdminDomeinController adc;

	public LijstVeranderingVerzoekenController(AdminDomeinController adc) {
		this.adc = adc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LijstVeranderingVerzoeken.fxml"));
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
		gebruikerCol.setCellValueFactory(
				data -> new SimpleStringProperty(data.getValue().getAangevraagdDoor().getUsername()));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		aangevraagOpCol.setCellValueFactory(
				data -> new SimpleStringProperty(data.getValue().getAangevraagdOp().format(formatter)));

		tabelVerzoeken.setItems(adc.getBedrijvenMetAanVragenVoorWijzingen());
	}

	private void behandelKlik() {
		tabelVerzoeken.setRowFactory(tv -> {
			TableRow<IBedrijfVerandering> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					IBedrijfVerandering rowData = row.getItem();
					showDetails(rowData);
				}
			});
			return row;
		});
	}

	private void showDetails(IBedrijfVerandering verandering) {
		VergelijkGegevensBedrijfController tweePanelsMetDetailsScherm = new VergelijkGegevensBedrijfController(adc,
				verandering, this);

		Scene scene = new Scene(tweePanelsMetDetailsScherm, 1280, 800);
		Stage stage = (Stage) this.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	public void updateTabel(IBedrijf bedrijf, IBedrijfVerandering verandering) {
		tabelVerzoeken.setItems(adc.getBedrijvenMetAanVragenVoorWijzingen());
	}

}
