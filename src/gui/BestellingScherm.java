
package gui;

import domein.DomeinController;
import domein.IBestelling;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class BestellingScherm extends VBox implements BestellingenScherm {

	private TableView<IBestelling> bestellingenTabel;
	private TableColumn<IBestelling, String> orderIdColumn;
	private TableColumn<IBestelling, String> klantNaamColumn;
	private TableColumn<IBestelling, String> datumGeplaatstColumn;
	private TableColumn<IBestelling, Double> bedragColumn;
	private TableColumn<IBestelling, String> orderStatusCol;
	private TableColumn<IBestelling, String> paymentStatusCol;

	protected TextField filterField;

	public BestellingScherm() {
		this.bestellingenTabel = new TableView<>();
		this.getStylesheets().add(getClass().getResource("/css/bedrijfScherm.css").toExternalForm());
		setSpacing(10);
		setPadding(new Insets(10));

	}

	private void buildTabelGezamenlijkeDeel() {
		if (filterField == null) {
			filterField = new TextField();
			filterField.setId("filterBestellingen");
			filterField.promptTextProperty().set("🔎 Order ID of ");
			this.getChildren().add(filterField);
		}

		orderIdColumn = new TableColumn<>("Order ID");
		datumGeplaatstColumn = new TableColumn<>("Datum Geplaatst");
		orderStatusCol = new TableColumn<>("Orderstatus");
		paymentStatusCol = new TableColumn<>("Betalingsstatus");

		orderIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderID()));
		datumGeplaatstColumn
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDatumGeplaatst().toString()));
		orderStatusCol
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderstatus().getStatus()));
		paymentStatusCol
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isBetalingsstatus().getStatus()));

	}

	public void buildKlantTabelview() {
		bedragColumn = new TableColumn<>("bedrag (€)");

		bedragColumn.setCellValueFactory(data -> new ReadOnlyDoubleWrapper(data.getValue().berekenBedrag()).asObject());

		buildTabelGezamenlijkeDeel();

		filterField.setPromptText(filterField.getPromptText() + "Bedrag");
		bestellingenTabel.getColumns().addAll(orderIdColumn, datumGeplaatstColumn, bedragColumn, orderStatusCol,
				paymentStatusCol);

	}

	public void buildLeverancierTabelview() {

		klantNaamColumn = new TableColumn<>("Klant naam");
		klantNaamColumn.setCellValueFactory(
				data -> new SimpleStringProperty(data.getValue().getKlant().getBedrijf().getNaam()));

		buildTabelGezamenlijkeDeel();

		if (!filterField.getPromptText().contains("Klantnaam")) {
			filterField.setPromptText(filterField.getPromptText() + "Klantnaam");
		}

		bestellingenTabel.getColumns().addAll(orderIdColumn, klantNaamColumn, datumGeplaatstColumn, orderStatusCol,
				paymentStatusCol);

	}

	public TableView<IBestelling> getBestellingenTabel() {
		return this.bestellingenTabel;
	}

	private BestellingSchermDetails findExistingDetailsScherm() {
		for (Node node : this.getChildren()) {
			if (node instanceof BestellingSchermDetails) {
				return (BestellingSchermDetails) node;
			}
		}
		return null;
	}

	public void showOrderDetails(IBestelling rowData, DomeinController dc) {
		BestellingSchermDetails existingDetailsScherm = findExistingDetailsScherm();

		if (existingDetailsScherm != null) {
			this.getChildren().remove(existingDetailsScherm);
		}

		BestellingSchermDetails newDetailsScherm = new BestellingSchermDetails(dc, rowData, this);
		this.getChildren().add(newDetailsScherm);

	}

	public void voegData(ObservableList<IBestelling> bestellingen) {
		bestellingenTabel.setItems(bestellingen);
		bestellingenTabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.getChildren().add(bestellingenTabel);
	}

	public void updateTableColumns(IBestelling bestelling) {
		bestellingenTabel.getColumns().clear();
		buildTabelGezamenlijkeDeel();
		buildLeverancierTabelview();
		bestellingenTabel.getSelectionModel().select(bestelling);
	}

	public void updateData(ObservableList<IBestelling> bestellingen) {
		bestellingenTabel.setItems(bestellingen);
		bestellingenTabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

}
