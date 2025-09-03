package gui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import domein.AdminDomeinController;
import domein.BetalingsMethodesEnum;
import dto.BedrijfDTO;
import exception.InformationRequiredException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AddBedrijfSchermController {
	private AddBedrijfScherm addBedrijfScherm;
	private AdminDomeinController adc;

	public AddBedrijfSchermController(AddBedrijfScherm addBedrijfScherm, AdminDomeinController adc) {
		this.addBedrijfScherm = addBedrijfScherm;
		this.adc = adc;

		addBedrijfScherm.addListener(this::handleAdd);
	}

	private void handleAdd(ActionEvent event) {
		String logo = addBedrijfScherm.getLogo();
		String name = addBedrijfScherm.getName();
		String sector = addBedrijfScherm.getSector();
		String address = addBedrijfScherm.getAddress();
		String btwNumber = addBedrijfScherm.getBtwNumber();
		String contactPersoon = addBedrijfScherm.getContactPersoon();
		String phoneNumber = addBedrijfScherm.getPhoneNumber();
		String email = addBedrijfScherm.getEmail();

		List<BetalingsMethodesEnum> betalingsMethodes = geefLijstBetalingsmethodes(
				addBedrijfScherm.getPaymentMethods());

		BedrijfDTO bedrijfDTO = new BedrijfDTO(logo, name, sector, address, btwNumber, contactPersoon, phoneNumber,
				email, betalingsMethodes);

		try {
			this.adc.createBedrijf(bedrijfDTO);
			this.adc.getBedrijf();
			this.adc.voegBedrijfToe();
			addBedrijfScherm.clearFields();
			showSuccessNotification("Bedrijf toegevoegd", "Het bedrijf is succesvol toegevoegd.");
		} catch (InformationRequiredException e) {
			addBedrijfScherm.showErrors(e.getInformationRequired().values());
			addBedrijfScherm.clearFields();
		} catch (RuntimeException e) {
			addBedrijfScherm.showErrors(Arrays.asList(e.getMessage()));
			addBedrijfScherm.clearFields();
		}
	}

	private List<BetalingsMethodesEnum> geefLijstBetalingsmethodes(
			Map<BetalingsMethodesEnum, Boolean> betalingsMethodes) {
		return betalingsMethodes.entrySet().stream().filter(entry -> entry.getValue()).map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}

	private void showSuccessNotification(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}