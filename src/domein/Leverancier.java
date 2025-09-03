package domein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Leverancier klasse, subklasse van User
 */
@Entity
@Table(name = "leverancier")
@NamedQueries({
		@NamedQuery(name = "Leverancier.haalKlanten", query = "SELECT l.klanten FROM Leverancier l WHERE l.username = :gebruikersnaam AND l.password = :wachtwoord") })
public class Leverancier extends User implements ILeverancier, Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToMany
	private List<Klant> klanten;
	@OneToMany(mappedBy = "leverancier", cascade = CascadeType.ALL)
	private List<Product> producten;

	/**
	 * Constructor voor Leverancier, alle attributen worden hier ingesteld
	 * 
	 * @param gebruikersnaam
	 * @param wachtwoord
	 */
	public Leverancier(String gebruikersnaam, String wachtwoord) {
		super(gebruikersnaam, wachtwoord);
		this.klanten = new ArrayList<>();
		this.producten = new ArrayList<>();

	}

	/**
	 * Protected constructor voor JPA
	 */
	protected Leverancier() {

	}

	public void setKlanten(List<Klant> klanten) {
		this.klanten = klanten;
	}

	public List<Klant> getKlanten() {
		return Collections.unmodifiableList(klanten);
	}

	public int getAantalKlanten() {
		return this.klanten.size();
	}

	@Override
	public ObservableList<IProduct> getProducten() {
		return FXCollections.observableArrayList(producten);
	}

	public void setProducten(List<Product> producten) {
		this.producten = producten;
	}

}