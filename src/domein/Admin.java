package domein;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Admin klasse, is een subklasse van User
 */
@Entity
@Table(name="admin")
public class Admin extends User implements Serializable {
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
	private List<Bedrijf> bedrijven;

	/**
	 * Admin constructor
	 * 
	 * @param username
	 * @param password
	 */
	public Admin(String username, String password) {
		super(username, password);
	}

	/**
	 * Protected constructor voor JPA
	 */
	protected Admin() {
	}
}
