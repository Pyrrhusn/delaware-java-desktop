package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

import domein.BetalingsStatusEnum;
import domein.DomeinController;
import domein.IBestelling;
import domein.IBestellingProduct;
import domein.Leverancier;
import domein.LeveringStatusEnum;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

public class BestellingSchermDetails extends VBox implements PropertyChangeListener {

	@FXML
	private VBox rootVBox;

	@FXML
	private GridPane gridPane;

	@FXML
	private Label orderOverviewLabel;

	@FXML
	private HBox backButtonBox;

	@FXML
	private Button TerugNaarLijstButton;

	@FXML
	private Label orderIdLabel;

	@FXML
	private HBox orderDateBox;

	@FXML
	private Label orderDateLabel;

	@FXML
	private Label datumGeplaatstLabel;

	@FXML
	private HBox statusLeveringBox;

	@FXML
	private Label statusLeveringLabel;

	@FXML
	private Label orderStatusLabel;

	@FXML
	private Label statusLeveringValueLabel;

	@FXML
	private Label leveradresLabel;

	@FXML
	private HBox statusBetalingBox;

	@FXML
	private Label statusBetalingLabel;

	@FXML
	private ChoiceBox<String> betalingsStatus;

	@FXML
	private ChoiceBox<String> leveringsStatus;

	@FXML
	private Label betalingStatusLabel;

	@FXML
	private TableView<IBestellingProduct> productTableView;

	@FXML
	private TableColumn<IBestellingProduct, String> naamCol;

	@FXML
	private TableColumn<IBestellingProduct, Integer> aantalCol;

	@FXML
	private TableColumn<IBestellingProduct, Integer> inStockCol;

	@FXML
	private TableColumn<IBestellingProduct, Double> eenheidsprijsCol;

	@FXML
	private TableColumn<IBestellingProduct, Double> totaalProductCol;

	@FXML
	private GridPane totalGridPane;

	@FXML
	private HBox totalBox;

	@FXML
	private Label totalTextLabel;

	@FXML
	private Label totaalBestellingBedragLabel;

	@FXML
	private Button wijzigKnop;

	@FXML
	private Label datumLaatsteBetalingsherinneringLabel;

	private DomeinController dc;
	private IBestelling bestelling;
	private String leverstatus;
	private String betalingsstatus;
	private BestellingenScherm bs;

	public BestellingSchermDetails(DomeinController dc, IBestelling bestelling, BestellingenScherm bs) {
		this.dc = dc;
		this.bestelling = bestelling;
		this.bs = bs;
		bestelling.addPropertyChangeListener(this);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("BestellingSchermDetails.fxml"));
		loader.setRoot(this);

		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		vulBestellingDetailMetData(bestelling);

	}

	private void vulBestellingDetailMetData(IBestelling bestelling) {
		orderIdLabel.setText(bestelling.getOrderID());
		orderIdLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		datumGeplaatstLabel.setText(bestelling.getDatumGeplaatst().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		leveradresLabel.setText(bestelling.getLeveradres());

		if (dc.isUserInstanceOf(Leverancier.class)) {
			betalingsStatus.getItems().addAll(Arrays.stream(BetalingsStatusEnum.values())
					.map(BetalingsStatusEnum::getStatus).collect(Collectors.toList()));

			leveringsStatus.getItems().addAll(Arrays.stream(LeveringStatusEnum.values())
					.map(LeveringStatusEnum::getStatus).collect(Collectors.toList()));

		} else {
			verbergButton();
		}

		betalingsStatus.setValue(bestelling.isBetalingsstatus().getStatus());
		leveringsStatus.setValue(bestelling.getOrderstatus().getStatus());

		totaalBestellingBedragLabel.setText(String.valueOf(bestelling.berekenBedrag()) + " €");

		productTableView.setItems(FXCollections.observableArrayList(bestelling.getProducten()));

		naamCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getNaam()));
		aantalCol
				.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAantal()).asObject());
		inStockCol.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getProduct().getInStock()).asObject());
		eenheidsprijsCol
				.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

		totaalProductCol.setCellValueFactory(
				cellData -> new SimpleDoubleProperty(cellData.getValue().getProductTotaal()).asObject());

		LocalDate date = bestelling.getDatumLaatsteBetalingsherinnering();
		String formattedDate = date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";
		datumLaatsteBetalingsherinneringLabel.setText(formattedDate);

	}

	public void showInScene(Window ownerWindow) {
		Scene scene = new Scene(this, 1280, 800);
		Stage stage = new Stage();
		stage.initOwner(ownerWindow);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void setLeveringsStatus(ActionEvent event) {
		leverstatus = leveringsStatus.getValue();
	}

	@FXML
	private void setBetalingsStatus(ActionEvent event) {
		betalingsstatus = betalingsStatus.getValue();
	}

	public void verbergButton() {
		wijzigKnop.setVisible(false);
	}

	@FXML
	private void wijzigBestelling(ActionEvent e) {
		try {

			bestelling.wijzigBetalingsstatus(betalingsstatus, bestelling.isBetalingsstatus());
			bestelling.wijzigLeveringStatus(leverstatus, bestelling.getOrderstatus());
			dc.wijzigBetalingStatus(bestelling.getOrderID(), BetalingsStatusEnum.fromString(betalingsstatus));
			dc.wijzigOrderStatus(bestelling.getOrderID(), LeveringStatusEnum.fromString(leverstatus));

		} catch (IllegalArgumentException exc) {
			showAlert("Fout bij het wijzigen van bestelling", exc.getMessage(), AlertType.ERROR);
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("orderstatus".equals(evt.getPropertyName()) || "betalingsstatus".equals(evt.getPropertyName())) {
			bs.updateTableColumns(bestelling);
		}

	}

	public void close() {
		bestelling.removePropertyChangeListener(this);
	}

	private void showAlert(String title, String content, AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);

		double width = content.length() * 7;
		alert.getDialogPane().setPrefWidth(width);

		alert.showAndWait();
	}
}