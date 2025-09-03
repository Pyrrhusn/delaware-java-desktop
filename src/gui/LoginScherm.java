package gui;

import domein.AdminDomeinController;
import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginScherm extends GridPane {

	private TextField usernameField;
	private PasswordField passwordField;
	private Button loginButton;

	public LoginScherm() {
		buildGui();
		getStylesheets().add(getClass().getResource("/css/bedrijfScherm.css").toExternalForm());

	}

	private void buildGui() {
		setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(25, 25, 25, 25));

		Image logo = new Image(
				"https://i0.wp.com/jobfairvub.be/wp-content/uploads/2017/02/delaware.png?fit=850%2C850&ssl=1");

		ImageView imageView = new ImageView(logo);
		imageView.setFitHeight(500);
		imageView.setFitWidth(500);
		imageView.setPreserveRatio(true);
		add(imageView, 0, 0, 2, 1);

		Label usernameLabel = new Label("Gebruikersnaam:");
		add(usernameLabel, 0, 1);

		usernameField = new TextField();
		usernameField.setPrefHeight(40);

		add(usernameField, 1, 1);

		Label passwordLabel = new Label("Wachtwoord:");
		add(passwordLabel, 0, 2);

		passwordField = new PasswordField();
		passwordField.setPrefHeight(40);
		add(passwordField, 1, 2);

		loginButton = new Button("Login");

		HBox hbLoginButton = new HBox(10);
		hbLoginButton.setAlignment(Pos.BOTTOM_RIGHT);
		hbLoginButton.getChildren().add(loginButton);
		add(hbLoginButton, 1, 3);

	}

	public String getUsername() {
		return usernameField.getText();
	}

	public String getPassword() {
		return passwordField.getText();
	}

	public void addLoginListener(EventHandler<ActionEvent> listenForCalcButton) {
		loginButton.setOnAction(listenForCalcButton);

	}

	public void lauchLeverancierWindow(DomeinController domeincontroller) {

		SideBarLeverancierController sideBarLeverancier = new SideBarLeverancierController(domeincontroller);

		Scene scene = new Scene(sideBarLeverancier, 1280, 800);
		Stage stage = (Stage) this.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	public void lauchKlantWindow(DomeinController domeincontroller) {

		SideBarKlantController sideBarKlant = new SideBarKlantController(domeincontroller);

		Scene scene = new Scene(sideBarKlant, 1280, 800);
		Stage stage = (Stage) this.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	public void lauchAdminWindow(AdminDomeinController adc) {

		SideBar sb = new SideBar(adc);
		Scene scene = new Scene(sb, 1250, 1000);

		Stage stage = (Stage) this.getScene().getWindow();
		stage.setTitle("Address Book");
		stage.setScene(scene);

		stage.show();
	}
}
