package domein;

import javafx.collections.ObservableList;

/**
 * Interface voor Leverancier
 */
public interface ILeverancier {

	public int getAantalKlanten();

	public void setBedrijf(Bedrijf bedrijf);

	public String getUsername();

	public String getPassword();

	public ObservableList<IProduct> getProducten();

}