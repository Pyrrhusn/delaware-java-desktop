package gui;

import domein.DomeinController;
import domein.IBestelling;
import domein.Klant;
import domein.Leverancier;
import domein.User;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseButton;

public class BestellingSchermController {

	private BestellingScherm bestellingScherm;
	private DomeinController domeinController;
	private ObservableList<IBestelling> bestellingen;

	public BestellingSchermController(BestellingScherm bestellingScherm, DomeinController domeinController) {
		this.bestellingScherm = bestellingScherm;
		this.domeinController = domeinController;
		bestellingen = domeinController.getUserBestellingen("");
		build(bestellingen);
		this.bestellingScherm.filterField.setOnKeyReleased(e -> {
			setUserBestellingenFilterPredicate(this.bestellingScherm.filterField.getText());
		});
	}

	private void behandelKlik() {
		this.bestellingScherm.getBestellingenTabel().setRowFactory(tv -> {
			TableRow<IBestelling> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					IBestelling rowData = row.getItem();
					this.bestellingScherm.showOrderDetails(rowData, domeinController);
				}
			});
			return row;
		});
	}

	private void build(ObservableList<IBestelling> bs) {

		User user = domeinController.getUser();
		if (user instanceof Leverancier) {
			bestellingScherm.buildLeverancierTabelview();
		}

		if (user instanceof Klant) {
			bestellingScherm.buildKlantTabelview();
		}

		bestellingScherm.voegData(bs);
		behandelKlik();

	}

	public void setUserBestellingenFilterPredicate(String filterValue) {
		filterValue = filterValue.replace(" ", "").toLowerCase();
		bestellingen = domeinController.getUserBestellingen(filterValue);
		bestellingScherm.updateData(bestellingen);
	}

}