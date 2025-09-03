package gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import domein.AdminDomeinController;
import domein.BetalingsMethodesEnum;
import domein.IBedrijf;
import exception.InformationRequiredException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BeheerBedrijfSchermController {

	private AddBedrijfScherm addBedrijfScherm;
	private AdminDomeinController adc;
	private IBedrijf bedrijf;
	private BedrijfSchermDetails vorigeScherm;

	public BeheerBedrijfSchermController(AddBedrijfScherm addBedrijfScherm, AdminDomeinController adc, IBedrijf bedrijf,
			BedrijfSchermDetails vorigeScherm) {
		this.addBedrijfScherm = addBedrijfScherm;
		this.adc = adc;
		this.bedrijf = bedrijf;
		this.vorigeScherm = vorigeScherm;

		build();
		this.addBedrijfScherm.addListener(this::handleUpdate);
		this.addBedrijfScherm.backListener(this::handleBack);
	}

	private void build() {
		addBedrijfScherm.setLogo(bedrijf.getLogoUrl());
		addBedrijfScherm.setName(bedrijf.getNaam());
		addBedrijfScherm.setSector(bedrijf.getSector());
		addBedrijfScherm.setAddress(bedrijf.getAdres());
		addBedrijfScherm.setAddress(bedrijf.getAdres());
		addBedrijfScherm.setBtwNumber(bedrijf.getBTWNummer());
		addBedrijfScherm.setBtwNumber(bedrijf.getBTWNummer());
		addBedrijfScherm.setContactPersoon(bedrijf.getContactGegevens().get("contactpersoon"));
		addBedrijfScherm.setPhoneNumber(bedrijf.getContactGegevens().get("telefoon"));
		addBedrijfScherm.setEmail(bedrijf.getContactGegevens().get("email"));
		addBedrijfScherm.setPaymentMethods(createPaymentMethodsMap(bedrijf.getBetalingsOpties()));
		addBedrijfScherm.setAddButton("Wijzig");
		addBedrijfScherm.setActivateKlantOptions(bedrijf.isActiefKlant());
		addBedrijfScherm.setActivateLeverancierOptions(bedrijf.isActiefLeverancier());
		addBedrijfScherm.wijzigTitle("Wijzig gegevens");
	}

	private Map<BetalingsMethodesEnum, Boolean> createPaymentMethodsMap(List<BetalingsMethodesEnum> betalingsInfo) {
		return Arrays.stream(BetalingsMethodesEnum.values()).collect(Collectors.toMap(method -> method,
				method -> betalingsInfo.contains(method), (existing, replacement) -> existing, HashMap::new));
	}

	private void handleUpdate(ActionEvent event) {
		String name = addBedrijfScherm.getName();
		String logo = addBedrijfScherm.getLogo();
		String sector = addBedrijfScherm.getSector();
		String address = addBedrijfScherm.getAddress();
		String btwNumber = addBedrijfScherm.getBtwNumber();
		String contactPersoon = addBedrijfScherm.getContactPersoon();
		String phoneNumber = addBedrijfScherm.getPhoneNumber();
		String email = addBedrijfScherm.getEmail();
		boolean klantstatus = addBedrijfScherm.getKlantstatus();
        boolean leverancierstatus = addBedrijfScherm.getLeverancierstatus();

		List<BetalingsMethodesEnum> betalingsMethodes = geefLijstBetalingsmethodes(
				addBedrijfScherm.getPaymentMethods());

		try {
			this.adc.checkWijzigingBedrijf(bedrijf, logo,sector, address, contactPersoon, phoneNumber, email, betalingsMethodes);
			this.adc.setActiefLeverancier(leverancierstatus, bedrijf);
        	this.adc.setActiefKlant(klantstatus, bedrijf);
			this.adc.updateBedrijf(bedrijf);
			showSuccessNotification("Bedrijf bijgewerkt", "Het bedrijf is succesvol bijgewerkt.");
			vorigeScherm.updateTabel(bedrijf);
		} catch (InformationRequiredException e) {
			addBedrijfScherm.showErrors(e.getInformationRequired().values());
		} catch (RuntimeException e) {
			addBedrijfScherm.showErrors(Arrays.asList(e.getMessage()));
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

	private void handleBack(ActionEvent event) {
		addBedrijfScherm.goBackToDetails(vorigeScherm);
	}
}
