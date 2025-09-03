package domein;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import exception.InformationRequiredException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Bedrijf klasse
 */
@Entity
@Table(name = "bedrijf")
@NamedQueries({
		@NamedQuery(name = "Bedrijf.findBedrijf", query = "SELECT b FROM Bedrijf b WHERE b.btwNummer = :btwNummer"),
		@NamedQuery(name = "Bedrijf.findPendingVeranderingen", query = "SELECT b FROM Bedrijf b JOIN b.veranderingen v WHERE v.isGoedGekeurd = false and v.isAfgekeurd = false") }

)

public class Bedrijf implements IBedrijf, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToOne(mappedBy = "bedrijf", cascade = CascadeType.ALL)
	private Leverancier leverancier;
	@OneToOne(mappedBy = "bedrijf", cascade = CascadeType.ALL)
	private Klant klant;
	@ManyToOne
	private Admin admin;
	@Column(columnDefinition = "JSON")
	@Convert(converter = ListEnumToStringConverter.class)
	private List<BetalingsMethodesEnum> betalingsInfo;
	@Column(unique = true)
	private String naam;
	private String logoImage;
	private String sector;
	private String adres;
	@Column(unique = true)
	private String btwNummer;
	private LocalDate accountSinds;
	private boolean isActiefKlant;
	private boolean isActiefLeverancier;
	@Column(columnDefinition = "JSON")
	@Convert(converter = MapToStringConverter.class)
	private Map<String, String> contactGegevens;

	@OneToMany(mappedBy = "bedrijf", cascade = CascadeType.ALL)
	private List<BedrijfVerandering> veranderingen = new ArrayList<>();
	@Transient
	private Map<RequiredElementBedrijfEnum, String> requiredElements = new HashMap<>();

	/**
	 * Constructor voor bedrijf. Alle attributen worden via de parameters ingesteld
	 * 
	 * @param logoImage
	 * @param naam
	 * @param sector
	 * @param adres
	 * @param betalingsInfo
	 * @param contactGegevens
	 * @param btwNummer
	 * @param accountSinds
	 * @param isActiefKlant
	 * @param isActiefLeverancier
	 */
	public Bedrijf(String logoImage, String naam, String sector, String adres,
			List<BetalingsMethodesEnum> betalingsInfo, Map<String, String> contactGegevens, String btwNummer,
			LocalDate accountSinds, boolean isActiefKlant, boolean isActiefLeverancier) {
		setLogoUrl(logoImage);
		setNaam(naam);
		setSector(sector);
		setAdres(adres);
		setBetalingsInfo(betalingsInfo);
		this.contactGegevens = contactGegevens;
		setBTWNummer(btwNummer);
		setAccountSinds(accountSinds);
		this.isActiefKlant = isActiefKlant;
		this.isActiefLeverancier = isActiefLeverancier;

	}

	/**
	 * Protected constructor voor JPA
	 */
	protected Bedrijf() {
	}

	public int getId() {
		return this.id;
	}

	public List<BetalingsMethodesEnum> getBetalingsInfo() {
		return betalingsInfo;
	}

	public void setBetalingsInfo(List<BetalingsMethodesEnum> betalingsInfo) {
		this.betalingsInfo = betalingsInfo;
	}

	public void setAccountSinds(LocalDate accountSinds) {
		this.accountSinds = accountSinds;

	}

	public LocalDate getAccountSinds() {
		return this.accountSinds;

	}

	/**
	 * Controleert of het bedrijf als klant actief is
	 */
	public boolean isActiefKlant() {
		return isActiefKlant;
	}

	/**
	 * Activeert/Deactiveert het klantenaccount dat tot het bedrijf behoort
	 */
	public void setActiefKlant(boolean isActiefKlant) {
		this.isActiefKlant = isActiefKlant;
	}

	/**
	 * Controleert of het bedrijf als leverancier actief is
	 */
	public boolean isActiefLeverancier() {
		return isActiefLeverancier;
	}

	/**
	 * Haalt het leveranciersaccount dat bij dit bedrijf hoort op
	 */
	public ILeverancier getLeverancier() {
		return (ILeverancier) leverancier;
	}

	/**
	 * Stelt het leveranciersaccount in voor een bedrijf
	 */
	public void setLeverancier(Leverancier leverancier) {
		this.leverancier = leverancier;
	}

	/**
	 * Haalt het klanten account dat bij dit bedrijf hoort op
	 */
	public IKlant getKlant() {
		return (IKlant) klant;
	}

	/**
	 * Stelt het klantenaccount in voor een bedrijf
	 */
	public void setKlant(Klant klant) {
		this.klant = klant;
	}

	/**
	 * Activeert/Deactiveert het leveranciersaccount dat tot het bedrijf behoort
	 */
	public void setActiefLeverancier(boolean isActiefLeverancier) {
		this.isActiefLeverancier = isActiefLeverancier;
	}

	public String getNaam() {
		return naam;
	}

	/**
	 * Setter voor bedrijfsnaam. Deze moet minstens 2 tekens en tenminste 1 letter
	 * bevatten
	 */
	public void setNaam(String naam) {
		if (!(naam == null || naam.isBlank()) && naam.matches("^(?=.*[a-zA-Z])(?!\\d+$).{2,}$")) {
			this.naam = naam;
		}
	}

	public String getSector() {
		return sector;
	}

	public String getAdres() {
		return adres;
	}

	public Map<String, String> getContactGegevens() {
		return contactGegevens;
	}

	public String getLogoUrl() {
		return this.logoImage;
	}

	public void setLogoUrl(String image) {
		this.logoImage = image;
	}

	public String getBTWNummer() {
		return btwNummer;
	}

	/**
	 * Setter voor BTW nummer. Dit moet een geldig Europees BTW nummer zijn
	 * 
	 * @param btwNummer
	 */
	public void setBTWNummer(String btwNummer) {
		if (!(btwNummer == null || btwNummer.isBlank()) && btwNummer.matches(
				"^(ATU[0-9]{8}|BE[01][0-9]{9}|BG[0-9]{9,10}|HR[0-9]{11}|CY[A-Z0-9]{9}|CZ[0-9]{8,10}|DK[0-9]{8}|EE[0-9]{9}|FI[0-9]{8}|FR[0-9A-Z]{2}[0-9]{9}|DE[0-9]{9}|EL[0-9]{9}|HU[0-9]{8}|IE([0-9]{7}[A-Z]{1,2}|[0-9][A-Z][0-9]{5}[A-Z])|IT[0-9]{11}|LV[0-9]{11}|LT([0-9]{9}|[0-9]{12})|LU[0-9]{8}|MT[0-9]{8}|NL[0-9]{9}B[0-9]{2}|PL[0-9]{10}|PT[0-9]{9}|RO[0-9]{2,10}|SK[0-9]{10}|SI[0-9]{8}|ES[A-Z]([0-9]{8}|[0-9]{7}[A-Z])|SE[0-9]{12}|GB([0-9]{9}|[0-9]{12}|GD[0-4][0-9]{2}|HA[5-9][0-9]{2}))$")) {
			this.btwNummer = btwNummer;
		}

	}

	/**
	 * Controleer of de meegegeven sector geldig is. Deze moet minstens 2 tekens en
	 * tenminste 1 letter bevatten
	 * 
	 * @param sector
	 * @return Een boolean die weergeeft of de meegegeven sector geldig is
	 */
	private boolean isValidSector(String sector) {
		return !(sector == null || sector.isBlank()) && sector.matches("^(?=.*[a-zA-Z])(?!\\d+$).{2,}$");
	}

	/**
	 * Controleert of het meegegeven adres geldig is. Deze moet van de volgende vorm
	 * zijn: Straat Huisnummer, Postcode Stad, Land
	 * 
	 * @param address
	 * @return Een boolean die weergeeft of het meegegeven adres geldig is
	 */
	private boolean isValidAddress(String address) {
		return !(address == null || address.isBlank()) && address
				.matches("\\b[\\p{L} -ë]+? \\d+,(?:\\s*\\d{4} [\\p{L} -ë]+|\\d{4} [\\p{L} -ë]+),\\s*[\\p{L} -ë]+?");
	}

	/**
	 * Controleert of de meegegeven contactinformatie geldig is. Er moet een
	 * contactpersoon, een email een een telefoonnummer meegegeven zijn. De email
	 * moet van een geldig formaat zijn, en het telefoonnummer moet geldig zijn en
	 * met een "+" beginnen
	 * 
	 * @param contactPersoon
	 * @param email
	 * @param phoneNumber
	 * @return Een boolean die weergeeft of de meegegeven contactinformatie geldig
	 *         is
	 */
	private boolean isValidContactDetails(String contactPersoon, String email, String phoneNumber) {
		return !(contactPersoon == null || contactPersoon.isBlank())
				&& !(email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
				&& !(phoneNumber == null || !phoneNumber.matches("^\\+[1-9]{1}[0-9]{3,14}$"));
	}

	/**
	 * Setter voor de sector. Hierbij wordt de methode isValidSector opgeroepen voor
	 * de controle
	 */
	public void setSector(String sector) {
		if (isValidSector(sector)) {
			this.sector = sector;
		} else {
			requiredElements.put(RequiredElementBedrijfEnum.BedrijfSectorRequired, "Bedrijf sector is niet geldig");
		}
	}

	/**
	 * Setter voor het adres. Hierbij wordt de methode isValidAdress opgeroepen voor
	 * de controle
	 */
	public void setAdres(String adres) {
		if (isValidAddress(adres)) {
			this.adres = adres;
		} else {
			requiredElements.put(RequiredElementBedrijfEnum.BedrijfAdresRequired, "Bedrijf adres is niet geldig");
		}
	}

	/**
	 * Setter voor de contactinformatie. Hierbij wordt de methode
	 * isValidContactDetails opgeroepen voor de controle
	 */
	public void setContactGegevens(Map<String, String> contactGegevens) {
		if (contactGegevens != null && !contactGegevens.isEmpty()) {
			String contactPersoon = contactGegevens.get("contactpersoon");
			String email = contactGegevens.get("email");
			String telefoon = contactGegevens.get("telefoon");

			if (isValidContactDetails(contactPersoon, email, telefoon)) {
				this.contactGegevens = contactGegevens;
			} else {
				requiredElements.put(RequiredElementBedrijfEnum.ContactGegevensRequired,
						"Bedrijf contactgegevens zijn niet geldig");
			}
		}
	}

	public ObservableList<BetalingsMethodesEnum> getBetalingsOpties() {
		return FXCollections.observableArrayList(betalingsInfo); // CHECKEN
	}

	/**
	 * Controleert of een bedrijfswijziging, aangevraagd door een klant of
	 * leverancier, geldig is en voert de wijziging ook uit indien ze geldig is
	 * 
	 * @param logo
	 * @param sector
	 * @param address
	 * @param contactPersoon
	 * @param phoneNumber
	 * @param email
	 * @param betalingsMethodes
	 * @throws InformationRequiredException Exception die geworpen wordt als 1 of
	 *                                      meerdere attributen leeg of niet geldig
	 *                                      zijn
	 */
	public void checkWijzigingBedrijf(String logo, String sector, String address, String contactPersoon,
			String phoneNumber, String email, List<BetalingsMethodesEnum> betalingsMethodes)
			throws InformationRequiredException {
		requiredElements = new HashMap<>();

		if (!isValidSector(sector)) {
			requiredElements.put(RequiredElementBedrijfEnum.BedrijfSectorRequired, "Bedrijf sector is niet geldig");
		}

		if (!isValidAddress(address)) {
			requiredElements.put(RequiredElementBedrijfEnum.BedrijfAdresRequired, "Bedrijf adres is niet geldig");
		}

		if (!isValidContactDetails(contactPersoon, email, phoneNumber)) {
			requiredElements.put(RequiredElementBedrijfEnum.ContactGegevensRequired,
					"Bedrijf contactgegevens zijn niet geldig");
		}

		if (logo.length() > 255) {
			requiredElements.put(RequiredElementBedrijfEnum.BedrijfLogoRequired,
					"URL voor bedrijflogo mag niet langer zijn dan 255 karakters");
		}

		if (!requiredElements.isEmpty()) {
			throw new InformationRequiredException(requiredElements);
		}

		setLogoUrl(logo);
		setSector(sector);
		setAdres(address);
		setBetalingsInfo(betalingsMethodes);
		Map<String, String> cinfo = new HashMap<>();
		cinfo.put("contactpersoon", contactPersoon);
		cinfo.put("email", email);
		cinfo.put("telefoon", phoneNumber);
		setContactGegevens(cinfo);
	}

	public List<BedrijfVerandering> getVeranderingen() {
		return veranderingen;
	}

	/**
	 * Voegt een bedrijfswijzigingsaanvraag toe
	 * 
	 * @param verandering
	 */
	public void addChange(BedrijfVerandering verandering) {
		this.veranderingen.add(verandering);
		verandering.setBedrijf(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(naam);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bedrijf other = (Bedrijf) obj;
		return Objects.equals(naam, other.naam);
	}

}
