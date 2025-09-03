package domein;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Abstracte User klasse: heeft Admin, Leverancier en Klant als subklasses
 */
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
		@NamedQuery(name = "User.haalUserOp", query = "SELECT u FROM User u WHERE :gebruikersnaam = u.username"),
		@NamedQuery(name = "User.haalUserHashWWOp", query = "SELECT u.password FROM User u WHERE :gebruikersnaam = u.username") })
public abstract class User implements Serializable, IUser {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Bestelling> bestellingen;
	@ManyToOne
	private Bedrijf bedrijf;

	private String username;
	private String password;

	/**
	 * Constructor voor User
	 * 
	 * @param username
	 * @param password
	 */
	public User(String username, String password) {
		this.username = username;
		setPassword(password);
	}

	protected User() {

	}

	public int getId() {
		return this.id;
	}

	public void setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
	}

	public void setBestellingen(List<Bestelling> bestellingen) {
		this.bestellingen = bestellingen;
	}

	public IBedrijf getBedrijf() {
		return this.bedrijf;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = LoginController.hashPassword(password);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * Haalt alle bestellingen die horen tot een bepaalde user op
	 */
	public ObservableList<IBestelling> getBestellingen() {
		ObservableList<IBestelling> bestellingenObservableList = FXCollections.observableArrayList();
		bestellingenObservableList.addAll(bestellingen);
		return bestellingenObservableList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(username, other.username);
	}

	public boolean isLeverancier(User user) {
		return user instanceof Leverancier;
	}

	public boolean isUserIstanceOf(User user, Class<? extends User> subclass) {
		return subclass.isInstance(user);
	}

}