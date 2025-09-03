package domein;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import exception.ProductInformationRequiredException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Product klasse, bevat ProductBuilder als innerklasse
 */
@Entity
@Table(name = "product")
@NamedQueries({
		@NamedQuery(name = "Product.getPerLeverancier", query = "SELECT p FROM Product p WHERE p.leverancier.username = :gebruikernaam") })
public class Product implements IProduct, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@ManyToOne
	private Leverancier leverancier;
	private String naam;
	private Integer inStock;
	private Double eenheidsprijs;
	private String fotoUrl;
	@Transient
	private Map<RequiredElementProductEnum, String> requiredElements = new HashMap<>();

	/**
	 * ProductBuilder: innerklasse van Product
	 */
	public static class ProductBuilder {
		private Leverancier leverancier;
		private String naam;
		private Integer inStock;
		private Double eenheidsprijs;
		private String fotoUrl;
		
		
		private Map<RequiredElementProductEnum, String> requiredElements = new HashMap<>();

		public ProductBuilder leverancier(Leverancier leverancier) {
			this.leverancier = leverancier;
			return this;
		}

		/**
		 * Stelt de naam van een product in. Deze mag niet leeg zijn
		 * 
		 * @param naam De naam waarmee het product ingesteld wordt
		 * @return
		 */
		public ProductBuilder naam(String naam) {
			if (naam == null || naam.isBlank()) {
				requiredElements.put(RequiredElementProductEnum.ProductNaamRequired, "Productnaam is vereist");
			} else {
				this.naam = naam;
			}
			return this;
		}

		/**
		 * Stelt het aantal in stock van een product in. Deze mag niet leeg zijn en moet
		 * tenminste 0 zijn
		 * 
		 * @param inStock Het aantal in stock waarmee het product ingesteld wordt
		 * @return
		 */
		public ProductBuilder inStock(Integer inStock) {
			if (inStock == null) {
				requiredElements.put(RequiredElementProductEnum.ProductInStockRequired, "Aantal in stock is vereist");
			} else if (inStock < 0) {
				requiredElements.put(RequiredElementProductEnum.ProductInStockRequired,
						"Het aantal in stock kan niet kleiner zijn dan 0 en is verplicht op te geven!");
			} else {
				this.inStock = inStock;
			}
			return this;
		}

		/**
		 * Stel de eenheidsprijs van een product in. Deze mag niet leeg zijn en moet
		 * tenminste 0 zijn
		 * 
		 * @param eenheidsprijs De eenheidsprijs waarmee het product ingesteld wordt
		 * @return
		 */
		public ProductBuilder eenheidsprijs(Double eenheidsprijs) {
			if (eenheidsprijs == null || eenheidsprijs < 0) {
				requiredElements.put(RequiredElementProductEnum.ProductEenheidsprijsRequired,
						"Positieve eenheidsprijs is vereist");
			} else {
				this.eenheidsprijs = eenheidsprijs;
			}
			return this;
		}

		/**
		 * Stel de afbeelding van een product in. Deze mag niet leeg zijn
		 * 
		 * @param url De url van de afbeelding waarmee het product ingesteld wordt
		 * @return
		 */
		public ProductBuilder fotoUrl(String url) {
			if (url == null || url.isBlank()) {
				requiredElements.put(RequiredElementProductEnum.ProductFotoUrlRequired,
						"Url voor productfoto is vereist");
			} else if (url.length() > 255) {
		        requiredElements.put(RequiredElementProductEnum.ProductFotoUrlRequired, "URL voor productfoto mag niet langer zijn dan 255 karakters");
		    }else {
				this.fotoUrl = url;
			}
			return this;
		}

		/**
		 * Maakt een nieuw product aan indien alle attributen correct ingesteld zijn
		 * 
		 * @return Het nieuwe product
		 * @throws ProductInformationRequiredException Exception die geworpen wordt als
		 *                                             attributen leeg of ongeldig zijn
		 */
		public Product build() throws ProductInformationRequiredException {
			if (!requiredElements.isEmpty()) {
				throw new ProductInformationRequiredException(requiredElements);
			} else {
				return new Product(this);
			}

		}
	}

	/**
	 * Constructor voor product. Alle attributen worden hier ingesteld
	 * 
	 * @param builder
	 */
	public Product(ProductBuilder builder) {
		this.naam = builder.naam;
		this.inStock = builder.inStock;
		this.eenheidsprijs = builder.eenheidsprijs;
		this.fotoUrl = builder.fotoUrl;
		this.leverancier = builder.leverancier;
	}

	/**
	 * Protected constructor voor JPA
	 */
	protected Product() {

	}

	public Product(Leverancier leverancier) {
	}

	public String getNaam() {
		return naam;
	}

	public int getInStock() {
		return inStock;
	}

	public double getEenheidsprijs() {
		return eenheidsprijs;
	}

	public Leverancier getLeverancier() {
		return this.leverancier;
	}

	public String getFotoUrl() {
		return fotoUrl;
	}

	public ProductBuilder updateFrom(Product p) {
		return new ProductBuilder().leverancier(p.leverancier).naam(p.naam).inStock(p.inStock)
				.eenheidsprijs(p.eenheidsprijs).fotoUrl(p.fotoUrl);
	}

	/**
	 * Voegt een nieuw product toe aan de catalogus van een leverancier
	 * 
	 * @param aantalInStock
	 * @param naam2
	 * @param eenheidsprijs2
	 * @param url
	 * @param user2
	 * @param aantal
	 * @return Het nieuwe product
	 * @throws ProductInformationRequiredException Exception die geworpen wordt als
	 *                                             attributen leeg of ongeldig zijn
	 */
	public IProduct voegProductToe(int aantalInStock, String naam2, double eenheidsprijs2, String url, User user2,
			int aantal) throws ProductInformationRequiredException {
		Leverancier l = (Leverancier) user2;
		return (IProduct) new Product.ProductBuilder().naam(naam2).inStock(aantalInStock).eenheidsprijs(eenheidsprijs2)
				.fotoUrl(url).leverancier(l).build();

	}

	/**
	 * Voegt een bepaald aantal toe aan de bestaande stock
	 * 
	 * @param wijziging Het aantal dat toegevoegd wordt aan de bestaande stock
	 * @throws ProductInformationRequiredException Exception die geworpen wordt als
	 *                                             attributen leeg of ongeldig zijn
	 */
	public void werkStockBij(int wijziging) throws ProductInformationRequiredException {

		int nieuweStock = getInStock() + wijziging;
		wijzigInstock(nieuweStock);
		if (!requiredElements.isEmpty()) {
			throw new ProductInformationRequiredException(requiredElements);
		}

	}

	/**
	 * Wijzigt de stock van een product
	 * 
	 * @param nieuweStock
	 */
	private void wijzigInstock(int nieuweStock) {
		if (nieuweStock < 0) {
			requiredElements.put(RequiredElementProductEnum.ProductInStockRequired,
					"Het aantal in stock kan niet kleiner zijn dan 0 !");
		} else {
			this.inStock = nieuweStock;
		}
	}

}