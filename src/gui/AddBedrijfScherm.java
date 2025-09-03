package gui;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import domein.BetalingsMethodesEnum;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddBedrijfScherm extends AnchorPane {

	private Label logoLabel;
	private Label nameLabel;
	private Label sectorLabel;
	private Label addressLabel;
	private Label btwLabel;
	private Label contactLabel;
	private Label streetLabel;
	private Label numberLabel;
	private Label postalCodeLabel;
	private Label citylLabel;
	private Label countryLabel;

	private Label paymentLabel;
	private TextField nameTextField;
	private TextField sectorTextField;
	private TextField streetTextField;
	private TextField numberTextField;
	private TextField postalCodeTextField;
	private TextField cityTextField;
	private TextField countryTextField;
	private TextField btwTextField;
	private TextField contactPerson;
	private TextField phoneTextField;
	private TextField emailTextField;

	private TextField logoTextField;
	private Button addButton;
	private Label errorLabel;
	private List<CheckBox> checkboxes;
	private GridPane gridPane;

	private CheckBox activateKlantCheckbox;
	private CheckBox deactivateKlantCheckbox;
	private CheckBox activateLeverancierCheckbox;
	private CheckBox deactivateLeverancierCheckbox;
	private Label titleLabel;
	private Button backButton;
	private boolean klantstatus;
	private boolean leverancierstatus;
	private ScrollPane sp;

	public AddBedrijfScherm() {
		this.gridPane = initializeGridPane();
		initializeComponents();

		sp = new ScrollPane();
		AnchorPane.setTopAnchor(sp, 20.0);
		AnchorPane.setLeftAnchor(sp, 20.0);
		AnchorPane.setRightAnchor(sp, 20.0);
		AnchorPane.setBottomAnchor(sp, 30.0);
		sp.setContent(gridPane);
		sp.setFitToWidth(true);
		sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		this.getChildren().add(sp);
		getStylesheets().add(getClass().getResource("/css/bedrijfScherm.css").toExternalForm());
		sp.setStyle("-fx-background-color: white; -fx-background-radius: 5px; -fx-border-radius: 5px;");
		sp.setEffect(new DropShadow(10, Color.GRAY));
	}

	private GridPane initializeGridPane() {
		gridPane = new GridPane();
		gridPane.setPadding(new Insets(20));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setStyle("-fx-background-color: white; -fx-background-radius: 5px; -fx-border-radius: 5px;");
		return gridPane;
	}

	private void addTitle() {
		titleLabel = new Label("Voeg Bedrijf Toe");
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		gridPane.add(titleLabel, 0, 0, 2, 1);
	}

	public void wijzigTitle(String wijzig) {
		titleLabel.setText(wijzig);

		backButton = new Button("Terug");

		HBox titleBox = new HBox(10);
		titleBox.getChildren().addAll(backButton, titleLabel);

		gridPane.add(titleBox, 0, 0, 2, 1);
	}

	private void initializeComponents() {
		addTitle();
		logoLabel = createLabel("Logo:");
		nameLabel = createLabel("Naam:");
		sectorLabel = createLabel("Sector:");
		addressLabel = createLabel("Adres:");
		btwLabel = createLabel("BTW nummer:");
		contactLabel = createLabel("Contactgegevens:");
		paymentLabel = createLabel("Betalingsmogelijkheden en -info:");
		streetLabel = createLabel("Straat:");
		numberLabel = createLabel("Huisnummer:");
		postalCodeLabel = createLabel("Postcode:");
		citylLabel = createLabel("Stad:");
		countryLabel = createLabel("Land:");

		logoTextField = createTextField();
		logoTextField.setPromptText("Logo als URL");
		nameTextField = createTextField();
		sectorTextField = createTextField();
		streetTextField = createTextField();
		numberTextField = createTextField();
		postalCodeTextField = createTextField();
		cityTextField = createTextField();
		countryTextField = createTextField();
		btwTextField = createTextField();
		phoneTextField = createTextField();
		phoneTextField.setPromptText("Telefoon nr.");

		emailTextField = createTextField();
		emailTextField.setPromptText("Email");
		contactPerson = createTextField();
		contactPerson.setPromptText("Contactpersoon");

		checkboxes = Arrays.stream(BetalingsMethodesEnum.values()).map(this::createCheckbox)
				.collect(Collectors.toList());
		addButtonToGrid(gridPane);

		addComponentsToGrid(gridPane, checkboxes);
	}

	private Label createLabel(String labelText) {
		Label label = new Label(labelText);
		return label;
	}

	private TextField createTextField() {
		TextField textField = new TextField();
		textField.setPrefWidth(400);
		textField.setPrefHeight(40);
		return textField;
	}

	private CheckBox createCheckbox(BetalingsMethodesEnum method) {
		CheckBox checkBox = new CheckBox(method.getStatus());
		return checkBox;
	}

	private void addButtonToGrid(GridPane gridPane) {
		addButton = new Button("Voeg toe");
		addButton.setPrefWidth(120);
		addButton.setPrefHeight(40);
		addButton.setEffect(new DropShadow(10, Color.GRAY));
		addButton.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
		gridPane.add(addButton, 2, checkboxes.size() + 17);
	}

	private void addComponentsToGrid(GridPane gridPane, List<CheckBox> checkboxes) {
		gridPane.add(logoLabel, 0, 1);
		gridPane.add(logoTextField, 1, 1);
		gridPane.add(nameLabel, 0, 2);
		gridPane.add(nameTextField, 1, 2);
		gridPane.add(sectorLabel, 0, 3);
		gridPane.add(sectorTextField, 1, 3);
		gridPane.add(addressLabel, 0, 4);
		gridPane.add(streetLabel, 0, 5);
		gridPane.add(numberLabel, 0, 6);
		gridPane.add(postalCodeLabel, 0, 7);
		gridPane.add(citylLabel, 0, 8);
		gridPane.add(countryLabel, 0, 9);
		gridPane.add(streetTextField, 1, 5);
		gridPane.add(numberTextField, 1, 6);
		gridPane.add(postalCodeTextField, 1, 7);
		gridPane.add(cityTextField, 1, 8);
		gridPane.add(countryTextField, 1, 9);
		gridPane.add(btwLabel, 0, 10);
		gridPane.add(btwTextField, 1, 10);
		gridPane.add(contactLabel, 0, 12);
		gridPane.add(contactPerson, 1, 12);
		gridPane.add(phoneTextField, 1, 13);
		gridPane.add(emailTextField, 1, 14);
		gridPane.add(paymentLabel, 0, 15);

		IntStream.range(0, checkboxes.size()).forEach(i -> gridPane.add(checkboxes.get(i), 1, 15 + i));

	}

	public void addListener(EventHandler<ActionEvent> listenForAdd) {
		addButton.setOnAction(listenForAdd);

	}

	public void backListener(EventHandler<ActionEvent> listenForBack) {
		this.backButton.setOnAction(listenForBack);

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

	public void clearFields() {
		logoTextField.setText("");
		nameTextField.setText("");
		sectorTextField.setText("");
		streetTextField.setText("");
		numberTextField.setText("");
		postalCodeTextField.setText("");
		cityTextField.setText("");
		countryTextField.setText("");
		btwTextField.setText("");
		contactPerson.setText("");
		phoneTextField.setText("");
		emailTextField.setText("");

		for (CheckBox checkbox : checkboxes) {
			checkbox.setSelected(false);
		}
	}

	public Map<BetalingsMethodesEnum, Boolean> getPaymentMethods() {
		return checkboxes.stream().collect(Collectors
				.toMap(checkbox -> BetalingsMethodesEnum.fromString(checkbox.getText()), CheckBox::isSelected));
	}

	public String getLogo() {
		return logoTextField.getText();
	}

	public String getName() {
		return nameTextField.getText();
	}

	public String getSector() {
		return sectorTextField.getText();
	}

	public String getAddress() {
		return String.format("%s %s, %s %s, %s", streetTextField.getText().strip(), numberTextField.getText().strip(),
				postalCodeTextField.getText().strip(), cityTextField.getText().strip(),
				countryTextField.getText().strip());
	}

	public String getBtwNumber() {
		return btwTextField.getText();
	}

	public String getContactPersoon() {
		return contactPerson.getText();
	}

	public String getPhoneNumber() {
		return phoneTextField.getText();
	}

	public String getEmail() {
		return emailTextField.getText();
	}

	public boolean getKlantstatus() {
		return klantstatus;
	}

	public boolean getLeverancierstatus() {
		return leverancierstatus;
	}

	public void setLogo(String logo) {
		logoTextField.setText(logo);
	}

	public void setName(String name) {
		nameTextField.setText(name);
		nameTextField.setDisable(true);
	}

	public void setSector(String sector) {
		sectorTextField.setText(sector);
	}

	public void setAddress(String address) {
		String[] delen = address.split(",");
		String deel1 = delen[0].strip();
		String deel2 = delen[1].strip();
		String deel3 = delen[2].strip();

		streetTextField.setText(deel1.substring(0, deel1.lastIndexOf(" ")));
		numberTextField.setText(deel1.substring(deel1.lastIndexOf(" ") + 1, deel1.length()));
		postalCodeTextField.setText(deel2.substring(0, deel2.indexOf(" ")));
		cityTextField.setText(deel2.substring(deel2.indexOf(" ") + 1, deel2.length()));
		countryTextField.setText(deel3);
	}

	public void setContactPersoon(String contactPersoon) {
		contactPerson.setText(contactPersoon);
	}

	public void setBtwNumber(String btwNumber) {
		btwTextField.setText(btwNumber);
		btwTextField.setDisable(true);

	}

	public void setPhoneNumber(String phoneNumber) {
		phoneTextField.setText(phoneNumber);
	}

	public void setEmail(String email) {
		emailTextField.setText(email);
	}

	public void setAddButton(String update) {
		addButton.setText(update);
	}

	public void setPaymentMethods(Map<BetalingsMethodesEnum, Boolean> paymentMethods) {
		paymentMethods.forEach((method, isSelected) -> {
			checkboxes.stream().filter(checkbox -> checkbox.getText().equals(method.getStatus())).findFirst()
					.ifPresent(checkbox -> checkbox.setSelected(isSelected));
		});
	}

	public void setActivateKlantOptions(boolean state) {
		activateKlantCheckbox = new CheckBox("Activate");
		deactivateKlantCheckbox = new CheckBox("Deactivate");

		klantstatus = state;

		activateKlantCheckbox.setSelected(klantstatus);
		deactivateKlantCheckbox.setSelected(!klantstatus);

		activateKlantCheckbox.setOnAction(event -> {
			klantstatus = true;
			if (activateKlantCheckbox.isSelected()) {
				deactivateKlantCheckbox.setSelected(false);
			} else {
				deactivateKlantCheckbox.setSelected(true);
			}
		});

		deactivateKlantCheckbox.setOnAction(event -> {
			klantstatus = false;
			if (deactivateKlantCheckbox.isSelected()) {
				activateKlantCheckbox.setSelected(false);
			} else {
				activateKlantCheckbox.setSelected(true);
			}
		});

		Label statusLabel = new Label("Klant status:");
		gridPane.add(statusLabel, 0, checkboxes.size() + 16);

		HBox checkboxesHBox = new HBox(10);
		checkboxesHBox.getChildren().addAll(activateKlantCheckbox, deactivateKlantCheckbox);

		gridPane.add(checkboxesHBox, 1, checkboxes.size() + 16);
	}

	public void setActivateLeverancierOptions(boolean state) {
		activateLeverancierCheckbox = new CheckBox("Activate");
		deactivateLeverancierCheckbox = new CheckBox("Deactivate");

		leverancierstatus = state;

		activateLeverancierCheckbox.setSelected(leverancierstatus);
		deactivateLeverancierCheckbox.setSelected(!leverancierstatus);

		activateLeverancierCheckbox.setOnAction(event -> {
			leverancierstatus = true;
			if (activateLeverancierCheckbox.isSelected()) {
				deactivateLeverancierCheckbox.setSelected(false);
			} else {
				deactivateLeverancierCheckbox.setSelected(true);
			}
		});

		deactivateLeverancierCheckbox.setOnAction(event -> {
			leverancierstatus = false;
			if (deactivateLeverancierCheckbox.isSelected()) {
				activateLeverancierCheckbox.setSelected(false);
			} else {
				activateLeverancierCheckbox.setSelected(true);
			}
		});

		Label statusLabel = new Label("Leverancier status:");
		gridPane.add(statusLabel, 0, checkboxes.size() + 18);

		HBox checkboxesHBox = new HBox(10);
		checkboxesHBox.getChildren().addAll(activateLeverancierCheckbox, deactivateLeverancierCheckbox);

		gridPane.add(checkboxesHBox, 1, checkboxes.size() + 18);
	}

	public void goBackToDetails(BedrijfSchermDetails vorigeScherm) {
		Stage stage = (Stage) (getScene().getWindow());
		stage.setScene(vorigeScherm.getScene());
	}

}