package gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import domein.DomeinController;
import domein.IProduct;
import exception.ProductInformationRequiredException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AddProductSchermController extends VBox{
    @FXML
    private TextField aantalFld;

    @FXML
    private TextField naamFld;

    @FXML
    private TextField prijsFld;

    @FXML
    private TextField urlFld;

    @FXML
    private AnchorPane voegProductAnchor;

    @FXML
    private Button voegToeBtn;
    
    private DomeinController dc;
    private LijstProductenLeverancierController lplc;

    public AddProductSchermController(DomeinController dc, LijstProductenLeverancierController lc) {
    	
    	this.dc = dc;
    	this.lplc = lc;
    	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProductScherm.fxml"));

		loader.setRoot(this);

		loader.setController(this);

		try {

			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
    }
    
    
    @FXML
    void voegToe(ActionEvent event) {
    	try {
    		IProduct prod = dc.voegProductToe(this.getAantalInStock(),this.getNaam(),this.getEenheidsPrijs(),this.getUrl(),dc.getUser());
    		dc.productToevoegen(prod);
    		showSuccessNotification("Product toegevoegd", "Het product is succesvol toegevoegd.");
    		lplc.updateTabel();
    		aantalFld.clear();
    		naamFld.clear();
    		prijsFld.clear();
    		urlFld.clear();
    	}catch(ProductInformationRequiredException e) {
    		this.showErrors(e.getInformationRequired().values());
    	}catch (RuntimeException e) {
			this.showErrors(Arrays.asList(e.getMessage()));
		}
    	
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
    
    public int getAantalInStock() {
        try {
            return Integer.parseInt(aantalFld.getText());
        } catch (NumberFormatException e) {
            return -1; 
        }
    }

    public double getEenheidsPrijs() {
        try {
            return Double.parseDouble(prijsFld.getText());
        } catch (NumberFormatException e) {
            return -1.1; 
        }
    }

    
    public String getNaam() {
    	return naamFld.getText();
    }
    
    public String getUrl() {
    	return urlFld.getText();
    }

}
