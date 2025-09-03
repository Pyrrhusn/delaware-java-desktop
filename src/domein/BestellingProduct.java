package domein;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * BestellingProduct klasse, werkt als een tussentabel met extra informatie
 * (prijs en aantal) voor bestelling en product
 */
@Entity
@Table(name = "bestelling_product")
public class BestellingProduct implements IBestellingProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn()
	private Bestelling bestelling;

	@ManyToOne
	@JoinColumn()
	private Product product;

	@Column(name = "price")
	private double price;

	private int aantal;

	/**
	 * Protected constructor voor JPA
	 */
	protected BestellingProduct() {
	}

	/**
	 * Constructor voor BestellingProduct. Alle attributen worden hier ingesteld
	 * 
	 * @param bestelling
	 * @param product
	 * @param price
	 * @param aantal
	 */
	public BestellingProduct(Bestelling bestelling, Product product, double price, int aantal) {
		this.bestelling = bestelling;
		this.product = product;
		this.price = price;
		this.aantal = aantal;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Bestelling getBestelling() {
		return bestelling;
	}

	public void setBestelling(Bestelling bestelling) {
		this.bestelling = bestelling;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAantal() {
		return aantal;
	}

	public void setAantal(int aantal) {
		this.aantal = aantal;
	}

	/**
	 * Haalt de totale prijs van een bepaald product op. De totale prijs van een
	 * bestelling is de som van de totale prijs van alle producten die tot de
	 * bestelling horen
	 */
	public double getProductTotaal() {
		return price * aantal;
	}

}
