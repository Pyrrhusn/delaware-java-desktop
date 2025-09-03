package gui;

import domein.Admin;
import domein.AdminDomeinController;
import domein.DomeinController;
import domein.Klant;
import domein.Leverancier;
import domein.LoginController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class LoginSchermController {

	private LoginScherm loginScherm;
	private final LoginController loginController;
	private final DomeinController domeinController;

	public LoginSchermController(LoginScherm loginScherm, LoginController loginController,
			DomeinController domeinController) {
		if (loginController == null)
			this.loginController = new LoginController();
		else
			this.loginController = loginController;

		this.loginScherm = loginScherm;
		this.loginScherm.addLoginListener(this::handleLogin);

		this.domeinController = domeinController;
	}

	private void handleLogin(ActionEvent event) {
		String username = loginScherm.getUsername();
		String password = loginScherm.getPassword();
		boolean loggedin = loginController.login(username, password);
		if (loggedin) {
			domeinController.setUser();
			showNextWindow();
		} else {
			showAlert("Login Failed", "Invalid username or password.");
		}

	}

	private void showNextWindow() {
		if (domeinController.isUserInstanceOf(Admin.class)) {
			this.loginScherm.lauchAdminWindow(new AdminDomeinController(loginController));
		} else if (domeinController.isUserInstanceOf(Leverancier.class)) {
			this.loginScherm.lauchLeverancierWindow(domeinController);
		} else if (domeinController.isUserInstanceOf(Klant.class)) {
			this.loginScherm.lauchKlantWindow(domeinController);
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}