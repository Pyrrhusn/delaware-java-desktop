package domein;

import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.ProductDao;
import repository.ProductDaoJpa;

/**
 * ProductBeheerder klasse, bevat een ProductDao om met de databank te werken
 */
public class ProductBeheerder {
	private ProductDao productdao;
	private ObservableList<IProduct> producten;

	/**
	 * Constructor voor ProductBeheerder. Hier wordt een ProductDao ingesteld en
	 * worden alle producten opgehaald
	 */
	public ProductBeheerder() {
		productdao = new ProductDaoJpa();
		producten = FXCollections.observableArrayList(productdao.findAll());
	}

	/**
	 * Wijzigt een bepaald product
	 * 
	 * @param p Het product dat gewijzigd wordt
	 */
	public void updateProduct(IProduct p) {
		productdao.updateProduct((Product) p);
	}

	/**
	 * Haalt alle producten op die een bepaalde leverancier aanbiedt
	 * 
	 * @param l De leverancier wiens producten opgehaald worden
	 * @return Een lijst van alle producten die de meegegeven leverancier aanbiedt
	 */
	public ObservableList<IProduct> getProductenLeverancier(ILeverancier l) {
		ObservableList<IProduct> productenObservableList = FXCollections.observableArrayList();
		productenObservableList.addAll(Collections.unmodifiableList(productdao.getProductenLeverancier(l)));
		return productenObservableList;
	}

	/**
	 * Voegt een nieuw product toe aan de catalogus van een leverancier
	 * 
	 * @param p Het product dat toegevoegd wordt
	 */
	public void voegProductToe(IProduct p) {
		this.productdao.voegProductToe((Product) p);
		this.producten.add(p);
	}

}
