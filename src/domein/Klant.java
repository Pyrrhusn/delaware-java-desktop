package domein;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Klant klasse, subklasse van User
 */
@Entity
@Table(name = "klant")
public class Klant extends User implements Serializable, IKlant {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor voor klant, alle attributen worden hier ingesteld
	 * 
	 * @param gebruikersnaam
	 * @param wachtwoord
	 */
	public Klant(String gebruikersnaam, String wachtwoord) {
		super(gebruikersnaam, wachtwoord);
	}

	/**
	 * Protected constructor voor JPA
	 */
	protected Klant() {
	}

}