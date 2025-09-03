package gui;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import domein.DomeinController;
import domein.IProduct;
import exception.ProductInformationRequiredException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LijstProductenLeverancierController extends VBox {

	@FXML
	private TableColumn<IProduct, Integer> aantalInStockCol;

	@FXML
	private TextField aantalInStockFld;

	@FXML
	private AnchorPane detailsAnchorPane;

	@FXML
	private ImageView fotoProduct;

	@FXML
	private Label naamProductLbl;

	@FXML
	private TableColumn<IProduct, String> productCol;

	@FXML
	private TableView<IProduct> productenTabel;

	@FXML
	private Label stockLabel;

	@FXML
	private Button updateAantalBtn;

	private DomeinController dc;
	private IProduct prod;

	public LijstProductenLeverancierController(DomeinController dc) {
		this.dc = dc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LijstProductenLeverancier.fxml"));
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

		productCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNaam()));
		aantalInStockCol.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getInStock()));

		productenTabel.setItems(dc.getProducten());
		verbergDetails();

	}

	private void verbergDetails() {
		detailsAnchorPane.setVisible(false);

	}

	private void toonDetails(IProduct product) {
		detailsAnchorPane.setVisible(true);
		vulDataInDetails(product);
	}

	private void vulDataInDetails(IProduct product) {
		fotoProduct.setImage(new Image(product.getFotoUrl()));
		naamProductLbl.setText(product.getNaam());

	}

	private void behandelKlik() {
		productenTabel.setRowFactory(tv -> {
			TableRow<IProduct> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					IProduct rowData = row.getItem();
					this.prod = rowData;
					toonDetails(rowData);
				}
			});
			return row;
		});
	}

	@FXML
	private void updateAantal(ActionEvent event) {
		int wijziging;
		try {
			wijziging = Integer.parseInt(aantalInStockFld.getText());
		} catch (NumberFormatException e) {
			wijziging = 0;
		}

		if (wijziging != 0) {
			try {
				IProduct bijgewerkt = dc.werkStockBij(prod, wijziging);
				dc.bijwerkenProduct(bijgewerkt);
				showSuccessNotification("Stock bijgewerkt", "De stock werd succesvol bijgewerkt");
				verbergDetails();
				updateTabel();
			} catch (ProductInformationRequiredException e) {
				this.showErrors(e.getInformationRequired().values());
			}
		}
		aantalInStockFld.clear();

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

	private void showSuccessNotification(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public void updateTabel() {
		productenTabel.getItems().clear();
		productenTabel.getItems().addAll(dc.getProducten());
		productenTabel.refresh();
	}

}
