package domein;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.BestellingDao;
import repository.BestellingDaoJpa;

/**
 * BestellingBeheerder klasse, werkt via BestellingDao met de databank
 */
public class BestellingBeheerder {
	private BestellingDao bestellingDao;
	private List<Bestelling> bestellingLijst;

	/**
	 * Constructor voor BestellingBeheerder. Hier wordt een BestellingDao ingesteld
	 * en worden alle bestellingen uit de databank opgehaald
	 */
	public BestellingBeheerder() {
		bestellingDao = new BestellingDaoJpa();
		bestellingLijst = bestellingDao.findAll();
	}

	/**
	 * Geeft een lijst van alle bestellingen terug
	 * 
	 * @return Een lijst van alle bestellingen die in de databank staan
	 */
	public List<Bestelling> getBestellingLijst() {
		return Collections.unmodifiableList(bestellingLijst);
	}

	/**
	 * Geeft een lijst van bestellingen van een bepaalde gebruiker
	 * 
	 * @param user De gebruiker wiens bestellingen opgehaald worden
	 * @return Een lijst van bestellingen van een bepaalde gebruiker
	 */
	public ObservableList<IBestelling> getUserBestellingen(User user) {
		ObservableList<IBestelling> bestellingenObservableList = FXCollections.observableArrayList();
		bestellingenObservableList.addAll(bestellingDao.getUserBestellingen(user));
		return bestellingenObservableList;
	}

	/**
	 * Haalt een bestelling met een bepaald order ID op
	 * 
	 * @param orderId Het order ID van de op te halen bestelling
	 * @return De bestelling met het meegegeven order ID
	 */
	public IBestelling getBestellingPerOrderId(String orderId) {
		return (IBestelling) bestellingDao.getUserBestellingPerOrderId(orderId);
	}

	/**
	 * Werkt de betalingsstatus van een bepaalde bestelling bij
	 * 
	 * @param orderID Het order ID van de bestelling die geupdate wordt
	 * @param enu     De nieuwe betalingsstatus die de bestelling zal krijgen
	 */
	public void updateBetalingsStatus(String orderID, BetalingsStatusEnum enu) {

		bestellingDao.werkBetalingsstatusBij(orderID, enu);
	}

	/**
	 * Werkt de levering status van een bepaalde bestelling bij
	 * 
	 * @param orderID Het order ID van de bestelling die geupdate wordt
	 * @param enu     De nieuwe levering status die de bestelling zal krijgen
	 */
	public void updateLeverStatus(String orderID, LeveringStatusEnum enu) {
		bestellingDao.werkOrderstatusBij(orderID, enu);
	}

	/**
	 * Geeft het aantal openstaande bestellingen van een bepaalde klant bij een
	 * bepaalde leverancier. Een bestelling wordt als openstaand beschouwt indien
	 * hij niet zowel betaald als geleverd is
	 * 
	 * @param leverancier De leverancier bij wie de klant bestellingen heeft
	 * @param klant       De klant wiens aantal openstaande bestellingen opgehaald
	 *                    wordt
	 * @return Het aantal openstaande bestellingen van de meegegeven klant bij de
	 *         meegegeven leverancier
	 */
	public long telOpenstaandeBestellingenKlant(Leverancier leverancier, Klant klant) {
		try {
			return bestellingDao.telBestelling(klant, leverancier);
		} catch (EntityNotFoundException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/**
	 * Haal alle bestellingen van een bepaalde klant bij een bepaalde leverancier
	 * 
	 * @param lever De leverancier bij wie de meegegeven klant bestellingen heeft
	 * @param De    klant wiens bestellingen bij de meegegeven leverancier opgehaald
	 *              worden
	 * @return Een lijst van bestellingen
	 */
	public ObservableList<IBestelling> getKlantBestellingenLeverancier(ILeverancier lever, IKlant kla) {
		ObservableList<IBestelling> bestellingenObservableList = FXCollections.observableArrayList();
		bestellingenObservableList
				.addAll(bestellingDao.getKlantBestellingenLeverancier((Leverancier) lever, (Klant) kla));
		return bestellingenObservableList;
	}

	/**
	 * Haalt alle openstaande bestellingen van een bepaalde klant bij een bepaalde
	 * leverancier
	 * 
	 * @param lever De leverancier bij wie de meegegeven klant openstaande
	 *              bestellingen heeft
	 * @param De    klant wiens openstaande bestellingen bij de meegegeven
	 *              leverancier opgehaald worden
	 * @return Een lijst van openstaande bestellingen
	 */
	public ObservableList<IBestelling> getOpenKlantBestellingenLeverancier(ILeverancier lever, IKlant kla) {
		ObservableList<IBestelling> bestellingenObservableList = FXCollections.observableArrayList();
		bestellingenObservableList.addAll(bestellingDao.haalOpenBestellingKlant((Leverancier) lever, (Klant) kla));
		return bestellingenObservableList;
	}

	/**
	 * Haalt de bestellingen op waar automatisch een betalingsherinnering voor moet
	 * worden gestuurd. Dit zijn bestellingen waarvoor de betaaldag binnen 3 dagen
	 * valt en waarvoor nog geen betalingsherinnering gestuurd is
	 * 
	 * @return Een lijst van bestellingen
	 */
	public ObservableList<IBestelling> haalBestellingenVoorBetalingsherinnering() {
		ObservableList<IBestelling> bestellingenObservableList = FXCollections.observableArrayList();
		bestellingenObservableList.addAll(bestellingDao.haalBestellingenVoorBetalingsherinnering());
		return bestellingenObservableList;
	}

	/**
	 * Wijzigt het attribuut "heeftBetalingsherinnering" bij een bestelling. Dit
	 * attribuut houdt bij of er al een herinnering is gestuurd naar de klant van
	 * een bepaalde bestelling
	 */
	public void werkHeeftBetalingsherinneringBij() {
		bestellingDao.werkHeeftBetalingsherinneringBij();
	}

}