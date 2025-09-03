package domein;

/**
 * Interface voor BestellingProduct
 */
public interface IBestellingProduct {

	Integer getId();

	Bestelling getBestelling();

	IProduct getProduct();

	double getPrice();

	int getAantal();

	double getProductTotaal();

}
