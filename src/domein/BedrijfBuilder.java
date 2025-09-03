package domein;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import dto.BedrijfDTO;
import exception.InformationRequiredException;

/**
 * BedrijfBuilder klasse
 */
public class BedrijfBuilder {
	/**
	 * Gebruikt om een wachtwoord te genereren
	 */
	private static final Random random = new Random();
	/**
	 * Alle mogelijke tekens die in het wachtwoord kunnen voorkomen
	 */
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	/**
	 * Lengte van het wachtwoord
	 */
	private static final int LENGTH = 9;

	private Bedrijf bedrijf;
	private Map<RequiredElementBedrijfEnum, String> requiredElements = new HashMap<>();

	private String wwLeverancierText = "";
	private String wwKlantText = "";

	/**
	 * Lege public builder constructor
	 */
	public BedrijfBuilder() {

	}

	public List<BetalingsMethodesEnum> geefBetalingsMethodes() {
		return null;
	}

	/**
	 * Maakt een nieuw bedrijf aan
	 * 
	 * @param bedrijfInfo Een DTO die alle nodige informatie bevat om een nieuw
	 *                    bedrijf aan te maken
	 */
	public void createBedrijf(BedrijfDTO bedrijfInfo) {
		bedrijf = new Bedrijf();
		requiredElements = new HashMap<>();
		buildLogo(bedrijfInfo.logo());
		buildNaam(bedrijfInfo.name());
		buildSector(bedrijfInfo.sector());
		buildAdres(bedrijfInfo.address());

		buildBetalingsInfo(bedrijfInfo.betalingsmethodes());
		Map<String, String> cinfo = new HashMap<>();
		cinfo.put("contactpersoon", bedrijfInfo.contactPersoon());
		cinfo.put("email", bedrijfInfo.email());
		cinfo.put("telefoon", bedrijfInfo.phoneNumber());
		buildContactGegevens(cinfo);
		buildBtwNummer(bedrijfInfo.btwNumber());
		buildAccountSinds(LocalDate.now());
		buildIsActiefKlant(true);
		buildIsActiefLeverancier(true);
		buildLeverancier(bedrijfInfo.name());
		buildKlant(bedrijfInfo.name());
	}

	public void buildLogo(String image) {
		this.bedrijf.setLogoUrl(image);
	}

	public void buildNaam(String naam) {
		this.bedrijf.setNaam(naam);
	}

	public void buildSector(String sector) {
		this.bedrijf.setSector(sector);
	}

	public void buildAdres(String adres) {
		this.bedrijf.setAdres(adres);
	}

	public void buildBetalingsInfo(List<BetalingsMethodesEnum> betalingsInfo) {
		this.bedrijf.setBetalingsInfo(betalingsInfo);
	}

	public void buildContactGegevens(Map<String, String> contactGegevens) {
		this.bedrijf.setContactGegevens(contactGegevens);
	}

	public void buildBtwNummer(String btwNummer) {
		this.bedrijf.setBTWNummer(btwNummer);
	}

	public void buildAccountSinds(LocalDate accountSinds) {
		this.bedrijf.setAccountSinds(accountSinds);
	}

	public void buildIsActiefKlant(boolean isActiefKlant) {
		this.bedrijf.setActiefKlant(isActiefKlant);
	}

	public void buildIsActiefLeverancier(boolean isActiefLeverancier) {
		this.bedrijf.setActiefLeverancier(isActiefLeverancier);
	}

	/**
	 * Maakt een leveranciersaccount voor een nieuw bedrijf. Een random wachtwoord
	 * wordt gegenereerd en de username wordt ingesteld als
	 * "bedrijfsnaam_leverancier"
	 * 
	 * @param naam Naam van het bedrijf
	 */
	public void buildLeverancier(String naam) {
		wwLeverancierText = randomWw();
		Leverancier leverancier = new Leverancier(String.format("%s%sleverancier", naam, "_"), wwLeverancierText);
		this.bedrijf.setLeverancier(leverancier);
	}

	/**
	 * Maakt een klantenaccount voor een nieuw bedrijf. Een random wachtwoord wordt
	 * gegenereerd en de username wordt ingesteld als "bedrijfsnaam_klant"
	 * 
	 * @param naam Naam van het bedrijf
	 */
	public void buildKlant(String naam) {
		wwKlantText = randomWw();
		Klant klant = new Klant(String.format("%s%sklant", naam, '_'), wwKlantText);
		this.bedrijf.setKlant(klant);
	}

	/**
	 * Geeft het nieuwe bedrijf terug als alle gegevens geldig zijn en stuurt een
	 * email naar het net nieuwe bedrijf met login gegevens
	 * 
	 * @return Het nieuwe bedrijf
	 * @throws InformationRequiredException Exception die geworpen wordt als 1 of
	 *                                      meerdere attributen leeg of niet geldig
	 *                                      zijn
	 */
	public Bedrijf getBedrijf() throws InformationRequiredException {
		if (bedrijf.getLogoUrl() == null)
			requiredElements.put(RequiredElementBedrijfEnum.BedrijfLogoRequired, "-Bedrijfslogo is vereist.");
		if (bedrijf.getNaam() == null)

			requiredElements.put(RequiredElementBedrijfEnum.BedrijfNaamRequired,
					"-Bedrijfsnaam is vereist en moet minstens 2 tekens bevatten.");

		if (bedrijf.getSector() == null)
			requiredElements.put(RequiredElementBedrijfEnum.BedrijfSectorRequired, "-Bedrijfssector is vereist.");

		if (bedrijf.getAdres() == null)
			requiredElements.put(RequiredElementBedrijfEnum.BedrijfAdresRequired,
					"-Bedrijfsadres is vereist en moet van de volgende vorm zijn: Straat + nummer, Postcode + Stad, Land");

		if (bedrijf.getBetalingsInfo() == null || bedrijf.getBetalingsInfo().isEmpty())
			requiredElements.put(RequiredElementBedrijfEnum.BetalingsInfoRequired, "-Betalingsinformatie is vereist.");

		if (bedrijf.getContactGegevens() == null || bedrijf.getContactGegevens().isEmpty())
			requiredElements.put(RequiredElementBedrijfEnum.ContactGegevensRequired, "-Contactgegevens zijn vereist.");

		if (bedrijf.getBTWNummer() == null)
			requiredElements.put(RequiredElementBedrijfEnum.BtwNummerRequired, "-Een geldig BTW-nummer is vereist.");

		if (bedrijf.getAccountSinds() == null)
			requiredElements.put(RequiredElementBedrijfEnum.AccountSindsRequired,
					"-Datum van het openen van de rekening is vereist.");

		if (bedrijf.getLeverancier() == null)
			requiredElements.put(RequiredElementBedrijfEnum.LeverancierRequired, "-Leverancier is vereist.");

		if (bedrijf.getKlant() == null)
			requiredElements.put(RequiredElementBedrijfEnum.KlantRequired, "-Klant is vereist.");
		
		if (bedrijf.getLogoUrl().length() > 255) {
	        requiredElements.put(RequiredElementBedrijfEnum.BedrijfLogoRequired, "URL voor bedrijflogo mag niet langer zijn dan 255 karakters");
	    }

		if (!requiredElements.isEmpty())
			throw new InformationRequiredException(requiredElements);

		this.bedrijf.getKlant().setBedrijf(bedrijf);
		this.bedrijf.getLeverancier().setBedrijf(bedrijf);

		stuurGegevens();
		return this.bedrijf;
	}

	/**
	 * Stuurt een email naar een net toegevoegd bedrijf met hun login informatie
	 */
	private void stuurGegevens() {
		String inhoud = String.format(
				"Beste %n Hier zijn de gegevens van uw account voor Delaware b2p portaal.%n Leverancier gegevens: %n - username: %s %n - wachtwoord: %s %n%n Klant gegevens: %n - username: %s %n - wachtwoord: %s %n%n Wij raden aan om de wachtwoorden te veranderen via Delaware b2b web portaal. %n%n Met Vriendelijke Groeten%n%nDelaware b2b team",
				bedrijf.getLeverancier().getUsername(), wwLeverancierText, bedrijf.getKlant().getUsername(),
				wwKlantText);
		EmailSender.sendEmail(bedrijf.getContactGegevens().get("email"),
				"Gegevens van uw account voor Delaware b2b portaal", inhoud);

	}

	/**
	 * Genereert een random wachtwoord voor leverancier -en klantenaccounts
	 */
	private String randomWw() {
		return random.ints(0, CHARACTERS.length()).limit(LENGTH).mapToObj(CHARACTERS::charAt).map(Object::toString)
				.collect(Collectors.joining());
	}

}