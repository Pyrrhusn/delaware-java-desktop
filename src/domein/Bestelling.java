
package domein;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Bestelling klasse
 */
@Entity
@Table(name="bestelling")
@NamedQueries({
		@NamedQuery(name = "Bestelling.findLeverancier", query = "SELECT b FROM Bestelling b WHERE b.leverancier.username = :leverancier"),
		@NamedQuery(name = "Bestelling.findKlant", query = "SELECT b FROM Bestelling b WHERE b.klant.username = :klant"),
		@NamedQuery(name = "Bestelling.findByOrderId", query = "SELECT b FROM Bestelling b WHERE b.orderID = :orderID"),
		@NamedQuery(name = "Bestelling.wijzigOrderStatus", query = " UPDATE Bestelling b SET b.orderstatus = :orderstatus WHERE b.orderID = :orderID "),
		@NamedQuery(name = "Bestelling.wijzigBetalingsstatus", query = "UPDATE Bestelling b SET b.betalingsstatus = :betalingsstatus WHERE b.orderID = :orderID"),
		@NamedQuery(name = "Bestelling.haalKlantBestellingLeverancier", query = "SELECT b FROM Bestelling b WHERE b.leverancier.username = :lusername AND b.klant.username = :kusername"),
		@NamedQuery(name = "Bestelling.haalOpenKlantBestellingLeverancier", query = "SELECT b FROM Bestelling b WHERE b.klant.username = :gebruikersnaamKlant AND b.leverancier.username = :gebruikersnaamLeverancier AND (b.orderstatus <> :Geleverd OR b.betalingsstatus <> :Betaald)"),
		@NamedQuery(name = "Bestelling.telOpenBestellingen", query = "SELECT COUNT(b) FROM Bestelling b WHERE b.klant.username = :gebruikersnaamKlant AND b.leverancier.username = :gebruikersnaamLeverancier AND (b.orderstatus <> :Geleverd OR b.betalingsstatus <> :Betaald)"),
		@NamedQuery(name = "Bestelling.haalBestellingenVoorBetalingsherinnering", query = "SELECT b FROM Bestelling b WHERE b.heeftBetalingsherinnering = false AND b.betalingsstatus = :betalingStatus AND b.betaaldag BETWEEN :startDate AND :endDate"),
		@NamedQuery(name = "Bestelling.wijzigHeeftBetalingsherinnering", query = "UPDATE Bestelling b SET b.heeftBetalingsherinnering = true,  b.datumLaatsteBetalingsherinnering = CURRENT_DATE WHERE b.heeftBetalingsherinnering = false AND b.betalingsstatus = :betalingStatus AND b.betaaldag BETWEEN :startDate AND :endDate") })

public class Bestelling implements IBestelling, Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Enumerated(EnumType.STRING)
	private LeveringStatusEnum orderstatus;
	@Enumerated(EnumType.STRING)
	private BetalingsStatusEnum betalingsstatus;

	@OneToMany(mappedBy = "bestelling", cascade = CascadeType.ALL)
	private List<BestellingProduct> bestellingProducts = new ArrayList<>();

	@ManyToOne
	private User user;

	private String orderID;
	private LocalDate datumGeplaatst;
	private String leveradres;
	private Klant klant;
	private Leverancier leverancier;
	private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private double bedrag;
	private LocalDate betaaldag;
	private boolean heeftBetalingsherinnering;
	private LocalDate datumLaatsteBetalingsherinnering;

	/**
	 * Constructor voor Bestelling. Alle attributen behalve
	 * heeftBetalingsherinnering en datumLaatsteBetalingsHerinnering worden door de
	 * parameters ingesteld. heeftBetalingsherinnering wordt bij aanmaak of false
	 * gezet, en datumLaatsteBetalingsherinnering op null
	 * 
	 * @param orderID
	 * @param datumGeplaatst
	 * @param leveradres
	 * @param orderstatus
	 * @param betalingsstatus
	 * @param klant
	 * @param leverancier
	 * @param betaaldag
	 */
	public Bestelling(String orderID, LocalDate datumGeplaatst, String leveradres, LeveringStatusEnum orderstatus,
			BetalingsStatusEnum betalingsstatus, Klant klant, Leverancier leverancier, LocalDate betaaldag, double bedrag) {

		this.orderID = orderID;
		this.datumGeplaatst = datumGeplaatst;
		this.leveradres = leveradres;
		setLeveringsStatus(orderstatus);
		setBetalingsStatus(betalingsstatus);
		this.klant = klant;
		this.leverancier = leverancier;
		this.betaaldag = betaaldag;
		this.heeftBetalingsherinnering = false;
		this.datumLaatsteBetalingsherinnering = null;
		this.bedrag = bedrag;
		propertyChangeSupport = new PropertyChangeSupport(this);
	}

	// Lege constructor voor JPA
	protected Bestelling() {
	}

	public Klant getKlant() {
		return klant;
	}

	public Leverancier getLeverancier() {
		return leverancier;
	}

	public String getOrderID() {
		return orderID;
	}

	public LocalDate getDatumGeplaatst() {
		return datumGeplaatst;
	}

	public String getLeveradres() {
		return leveradres;
	}

	public LeveringStatusEnum getOrderstatus() {
		return orderstatus;
	}

	public BetalingsStatusEnum isBetalingsstatus() {
		return betalingsstatus;
	}

	public void setLeveringsStatus(LeveringStatusEnum ls) {
		this.orderstatus = ls;
	}

	public void setBetalingsStatus(BetalingsStatusEnum bs) {
		this.betalingsstatus = bs;
	}

	public List<IBestellingProduct> getProducten() {
		List<IBestellingProduct> l = new ArrayList<>(bestellingProducts);
		return l;
	}

	/**
	 * Berekent de totale prijs van een bestelling
	 */
	public double berekenBedrag() {
		this.bedrag = bestellingProducts.stream()
				.mapToDouble(bestellingProduct -> bestellingProduct.getPrice() * bestellingProduct.getAantal()).sum();
		return bestellingProducts.stream()
				.mapToDouble(bestellingProduct -> bestellingProduct.getPrice() * bestellingProduct.getAantal()).sum();
	}

	public LocalDate getDatumLaatsteBetalingsherinnering() {
		return this.datumLaatsteBetalingsherinnering;
	}

	/**
	 * Geeft alle producten, en per product het aantal van dat specifieke product,
	 * van een bestelling
	 * 
	 * @return
	 */
	public List<IBestellingProduct> getBestellingProducts() {
		List<IBestellingProduct> l = new ArrayList<>(bestellingProducts);
		return l;
	}

	public void setBestellingProducts(List<BestellingProduct> bestellingProducts) {
		this.bestellingProducts = bestellingProducts;
	}

	/**
	 * Voegt een product, en een bepaald aantal van dat specifieke product, toe aan
	 * een bestelling
	 * 
	 * @param bestellingProduct Klasse die een referentie naar bestelling en product
	 *                          heeft
	 */
	public void addBestellingProduct(BestellingProduct bestellingProduct) {
		this.bestellingProducts.add(bestellingProduct);
		bestellingProduct.setBestelling(this);
	}

	/**
	 * Verwijdert een product van een bestelling
	 * 
	 * @param bestellingProduct Klasse die een referentie naar bestelling en product
	 *                          heeft
	 */
	public void removeBestellingProduct(BestellingProduct bestellingProduct) {
		this.bestellingProducts.remove(bestellingProduct);
		bestellingProduct.setBestelling(null);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * Wijzigt de levering status van een bestelling. Dit gaat enkel in 1 volgorde.
	 * Een bestelling kan van de status "in behandeling" naar de status "verzonden"
	 * gaan. En van de status "verzonden" naar de status "geleverd", maar het is
	 * 
	 * @param volgende De nieuwe levering status die de bestelling zal krijgen na
	 *                 uitvoeren van deze methode
	 * @param huidige  De huidige levering status van de bestelling
	 * @throws IllegalArgumentException
	 */
	@Override
	public void wijzigLeveringStatus(String volgende, LeveringStatusEnum huidige) {
		if (huidige.getStatus().equals("Geleverd") && !volgende.equals("Geleverd")) {
			throw new IllegalArgumentException(
					"Het is niet mogelijk om van de status \"Geleverd\" terug naar een andere status te gaan.");
		} else if (huidige.getStatus().equals("Verzonden") && volgende.equals("In behandeling")) {
			throw new IllegalArgumentException(
					"Het is niet mogelijk om van de status \"Verzonden\" terug naar de status \"In behandeling\" te gaan.");
		} else if (huidige.getStatus().equals("In behandeling") && volgende.equals("Geleverd")) {
			throw new IllegalArgumentException(
					"Het is niet mogelijk om meteen van de status \"In behandeling\" naar de status \"Geleverd\" te gaan. Een bestelling moet eerst verzonden worden.");
		}
		setLeveringsStatus(LeveringStatusEnum.fromString(volgende));

		propertyChangeSupport.firePropertyChange("orderstatus", huidige, orderstatus);
	}

	/**
	 * Wijzigt de betalingsstatus van een bestelling. Dit gaat enkel in 1 volgorde.
	 * Het is enkel mogelijk om van de status "niet betaald" naar de status
	 * "betaald" te gaan
	 * 
	 * @param volgende De nieuwe betalingsstatus die de bestelling zal krijgen na
	 *                 uitvoeren van deze methode
	 * @param huidige  De huidige betalingsstatus van de bestelling
	 * @throws IllegalArgumentException
	 */
	@Override
	public void wijzigBetalingsstatus(String volgende, BetalingsStatusEnum huidige) {
		if (huidige.getStatus().equals("Betaald") && volgende.equals("Niet Betaald")) {
			throw new IllegalArgumentException(
					"Het is niet mogelijk om van de status \"Betaald\" terug naar de status \"Niet betaald\" te gaan.");
		}
		setBetalingsStatus(BetalingsStatusEnum.fromString(volgende));
		propertyChangeSupport.firePropertyChange("betalingsstatus", huidige, betalingsstatus);
	}

	@Override
	public void setDatumLaatsteBetalingsherinnering(LocalDate datum) {
		this.datumLaatsteBetalingsherinnering = datum;
	};

	@Override
	public int hashCode() {
		return Objects.hash(orderID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bestelling other = (Bestelling) obj;
		return Objects.equals(orderID, other.orderID);
	}

}