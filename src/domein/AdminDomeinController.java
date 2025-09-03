package domein;

import java.util.List;
import java.util.stream.Collectors;

import dto.BedrijfDTO;
import exception.InformationRequiredException;
import javafx.collections.ObservableList;

/**
 * Domeincontroller voor admin
 */
public class AdminDomeinController {

	private BedrijfBuilder bedrijfBuilder;

	private BedrijfBeheerder bedrijfBeheerder;

	private Bedrijf bedrijf;

	private final LoginController loginController;

	public AdminDomeinController(LoginController loginController) {
		this(loginController, new BedrijfBuilder(), new BedrijfBeheerder());
	}

	/**
	 * Constructor voor AdminDomeinController
	 * 
	 * @param loginController
	 * @param bedrijfBuilder
	 * @param bedrijfBeheerder
	 */
	public AdminDomeinController(LoginController loginController, BedrijfBuilder bedrijfBuilder,
			BedrijfBeheerder bedrijfBeheerder) {
		this.loginController = loginController;
		this.bedrijfBuilder = bedrijfBuilder;
		this.bedrijfBeheerder = bedrijfBeheerder;
	}

	/**
	 * Roept createBedrijf uit BedrijfBuilder op om nieuwe bedrijven (en leverancier
	 * en klantenaccounts) aan te maken
	 * 
	 * @param bedrijfInfo Een DTO die over alle nodige informatie beschikt om een
	 *                    nieuw bedrijf aan te maken
	 */
	public void createBedrijf(BedrijfDTO bedrijfInfo) {
		this.bedrijfBuilder.createBedrijf(bedrijfInfo);

	}

	/**
	 * Haal het net aangemaakte bedrijf op
	 * 
	 * @return Een net aangemaakt bedrijf
	 * @throws InformationRequiredException Exception die geworpen wordt als 1 of
	 *                                      meerdere attributen leeg of niet geldig
	 *                                      zijn
	 */
	public Bedrijf getBedrijf() throws InformationRequiredException {
		this.bedrijf = this.bedrijfBuilder.getBedrijf();

		return bedrijf;

	}

	/**
	 * Voeg bedrijf toe aan databank
	 */
	public void voegBedrijfToe() {
		this.bedrijfBeheerder.voegBedrijfToe(this.bedrijf);
	}

	/**
	 * Geef alle mogelijke betalingsmethoden
	 * 
	 * @return Lijst van alle mogelijke betalingsmethoden
	 */
	public List<String> geefAlleBetalingsMethoden() {
		return List.of(BetalingsMethodesEnum.values()).stream().map(BetalingsMethodesEnum::getStatus)
				.collect(Collectors.toList());
	}

	/**
	 * Haal alle bedrijven op
	 * 
	 * @return Een lijst van alle bedrijven
	 */
	public ObservableList<IBedrijf> getBedrijven() {
		return bedrijfBeheerder.getBedrijvenLijst();
	}

	/**
	 * Logout voor admin
	 * 
	 * @return
	 */
	public boolean logout() {
		return loginController.logout();
	}

	/**
	 * Wijzig een bedrijf
	 * 
	 * @param het bedrijf dat gewijzigd moet worden
	 */
	public void updateBedrijf(IBedrijf bedrijf) {
		bedrijfBeheerder.updateBedrijf((Bedrijf) bedrijf);
	}

	/**
	 * Controleert of een bedrijfswijziging, aangevraagd door een klant of
	 * leverancier, geldig is en voert de wijziging ook uit indien ze geldig is
	 * 
	 * @param bedrijf
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
	public void checkWijzigingBedrijf(IBedrijf bedrijf, String logo, String sector, String address,
			String contactPersoon, String phoneNumber, String email, List<BetalingsMethodesEnum> betalingsMethodes)
			throws InformationRequiredException {
		Bedrijf bedrijf2 = (Bedrijf) bedrijf;
		bedrijf2.checkWijzigingBedrijf(logo, sector, address, contactPersoon, phoneNumber, email, betalingsMethodes);
	}

	/**
	 * Geef alle bedrijven waarvoor wijzigingen aangevraagd zijn
	 * 
	 * @return Een lijst van bedrijven waarvoor wijzigingen aangevraagd zijn
	 */
	public ObservableList<IBedrijfVerandering> getBedrijvenMetAanVragenVoorWijzingen() {
		return this.bedrijfBeheerder.getBedrijvenMetAanVragenVoorWijzingen();
	}

	/**
	 * Activeer/Deactiveer een leveranciersaccount
	 * 
	 * @param boolean
	 * @param bedrijf: bedrijf waarbij de leverancier hoort
	 */
	public void setActiefLeverancier(boolean b, IBedrijf bedrijf2) {
		bedrijf2.setActiefLeverancier(b);
	}

	/**
	 * Activeer/Deactiveer een klantenaccount
	 * 
	 * @param boolean
	 * @param bedrijf: bedrijf waarbij de klant hoort
	 */
	public void setActiefKlant(boolean b, IBedrijf bedrijf2) {
		bedrijf2.setActiefKlant(b);
	}

	/**
	 * Keur een aangevraagde bedrijfswijziging af
	 * 
	 * @param verandering een aangevraagde bedrijfswijziging
	 */
	public void afkeuren(IBedrijfVerandering verandering) {
		verandering.setAfgekeurd(true);
	}

}