package domein;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Klasse voor aangevraagde bedrijfswijzigingen
 */
@Entity
@Table(name = "bedrijfverandering")
public class BedrijfVerandering implements IBedrijfVerandering {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	private Bedrijf bedrijf;

	private String logoImage;
	private String sector;
	private String adres;
	private LocalDate accountSinds;
	private boolean isActiefKlant;
	private boolean isActiefLeverancier;
	@Column(columnDefinition = "JSON")
	@Convert(converter = MapToStringConverter.class)
	private Map<String, String> contactGegevens;
	@Column(columnDefinition = "JSON")
	@Convert(converter = ListEnumToStringConverter.class)
	private List<BetalingsMethodesEnum> betalingsInfo;
	private boolean isGoedGekeurd;
	private boolean isAfgekeurd;

	@OneToOne
	private User aangevraagdDoor;

	public IUser getAangevraagdDoor() {
		return aangevraagdDoor;
	}

	public void setAangevraagdDoor(IUser aangevraagdDoor) {
		this.aangevraagdDoor = (User) aangevraagdDoor;
	}

	private LocalDateTime aangevraagdOp;

	public void setBedrijf(Bedrijf bedrijf) {
		this.bedrijf = bedrijf;
	}

	public String getLogo() {
		return logoImage;
	}

	public void setLogo(String logoImage) {
		this.logoImage = logoImage;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getAdres() {
		return adres;
	}

	public void setAdres(String adres) {
		this.adres = adres;
	}

	public LocalDate getAccountSinds() {
		return accountSinds;
	}

	public void setAccountSinds(LocalDate accountSinds) {
		this.accountSinds = accountSinds;
	}

	public boolean isActiefKlant() {
		return isActiefKlant;
	}

	public void setActiefKlant(boolean isActiefKlant) {
		this.isActiefKlant = isActiefKlant;
	}

	public boolean isActiefLeverancier() {
		return isActiefLeverancier;
	}

	public void setActiefLeverancier(boolean isActiefLeverancier) {
		this.isActiefLeverancier = isActiefLeverancier;
	}

	public LocalDateTime getAangevraagdOp() {
		return aangevraagdOp;
	}

	public void setAangevraagdOp(LocalDateTime aangevraagdOp) {
		this.aangevraagdOp = aangevraagdOp;
	}

	public Map<String, String> getContactGegevens() {
		return contactGegevens;
	}

	public void setContactGegevens(Map<String, String> contactGegevens) {
		this.contactGegevens = contactGegevens;
	}

	public Bedrijf getBedrijf() {
		return bedrijf;
	}

	public List<BetalingsMethodesEnum> getBetalingsInfo() {
		return betalingsInfo;
	}

	public void setBetalingsInfo(List<BetalingsMethodesEnum> betalingsInfo) {
		this.betalingsInfo = betalingsInfo;
	}

	public void setGoedgekeurd(boolean status) {
		this.isGoedGekeurd = status;
	}

	public void setAfgekeurd(boolean isAfgekeurd) {
		this.isAfgekeurd = isAfgekeurd;
	}

	@Override
	public void setIBedrijf(IBedrijf bedrijf) {
		this.bedrijf = (Bedrijf) bedrijf;

	}

}
