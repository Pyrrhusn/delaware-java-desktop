package domein;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import exception.ProductInformationRequiredException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * DomeinController klasse
 */
public class DomeinController {

	private User user;
	private Product prod;
	private BestellingBeheerder bestellingbeheerder;
	private BedrijfBeheerder bedrijfbeheerder;
	private UserBeheerder userbeheerder;
	private ProductBeheerder productbeheerder;
	private final LoginController loginController;

	/**
	 * Constructor voor DomeinController. Alle attributen worden hier ingesteld
	 * 
	 * @param loginController Wordt gebruikt om de ingelogde gebruiker op te halen
	 */
	public DomeinController(LoginController loginController) {
		this.loginController = loginController;

		user = loginController.getUser();

		bedrijfbeheerder = new BedrijfBeheerder();
		bestellingbeheerder = new BestellingBeheerder();
		userbeheerder = new UserBeheerder();
		productbeheerder = new ProductBeheerder();
		prod = new Product();

	}

	public void setUser() {
		this.user = this.loginController.getUser();
	}

	public User getUser() {
		return this.user;
	}

	public boolean isLeverancier(User user) {
		return user.isLeverancier(user);
	}

	/**
	 * Controleert of de ingelogde user van een bepaald type is
	 * 
	 * @param subclass Een subklasse van de abstracte klasse User
	 * @return true indien de ingelogde user van het meegegeven type is, false zo
	 *         niet
	 */
	public boolean isUserInstanceOf(Class<? extends User> subclass) {
		return user.isUserIstanceOf(user, subclass);
	}

	/**
	 * Wijzigt de betalingsstatus van een bepaalde bestelling
	 * 
	 * @param OrderID         Het order ID van de bestelling die gewijzigd wordt
	 * @param betalingsstatus De nieuwe betalingsstatus die de bestelling zal
	 *                        krijgen
	 */
	public void wijzigBetalingStatus(String OrderID, BetalingsStatusEnum betalingsstatus) {
		bestellingbeheerder.updateBetalingsStatus(OrderID, betalingsstatus);
	}

	/**
	 * Wijzigt de levering status van een bepaalde bestelling
	 * 
	 * @param OrderID         Het order ID van de bestelling die gewijzigd wordt
	 * @param betalingsstatus De nieuwe levering status die de bestelling zal
	 *                        krijgen
	 */
	public void wijzigOrderStatus(String OrderID, LeveringStatusEnum leverstatus) {
		bestellingbeheerder.updateLeverStatus(OrderID, leverstatus);
	}

	/**
	 * Haal een bestelling met een bepaald ID op
	 * 
	 * @param order Het order ID van de gezochte bestelling
	 * @return De bestelling met het meegegeven order ID
	 */
	public IBestelling haalOrderPerId(String order) {
		return bestellingbeheerder.getBestellingPerOrderId(order);
	}

	/**
	 * Haalt de bestellingen van een bepaalde gebruiker op
	 * 
	 * @param filterValue Een filterwaarde die kan worden ingegeven om te filteren
	 *                    op een bepaalde naam of order ID
	 * @return Een lijst van bestellingen van een bepaalde gebruiker
	 */
	public ObservableList<IBestelling> getUserBestellingen(String filterValue) {
		ObservableList<IBestelling> bestellingen = bestellingbeheerder.getUserBestellingen(user);
		if (filterValue == null || filterValue.isBlank() || filterValue.isEmpty())
			return bestellingen;
		else
			return (ObservableList<IBestelling>) bestellingen
					.filtered(b -> b.getOrderID().toLowerCase().contains(filterValue)
							|| b.getKlant().getBedrijf().getNaam().toLowerCase().contains(filterValue)
							|| b.getProducten().stream().map(IBestellingProduct::getProductTotaal)
									.collect(Collectors.summingDouble(p -> p)).toString().contains(filterValue));
	}

	/**
	 * Haalt alle bedrijven op
	 * 
	 * @return Een lijst van alle bedrijven
	 */
	public ObservableList<IBedrijf> getBedrijven() {
		return bedrijfbeheerder.getBedrijvenLijst();
	}

	/**
	 * Haalt een lijst van klanten van de ingelogde leverancier op
	 * 
	 * @param filterValue Een filterwaarde die kan worden ingegeven om te filteren
	 *                    op een bepaalde naam
	 * @return Een lijst van klanten van de ingelogde leverancier
	 */
	public ObservableList<IKlant> getKlantenLeverancier(String filterValue) {
		ObservableList<IKlant> klanten = userbeheerder.geefKlantenPerLeverancier((Leverancier) this.user);
		if (filterValue == null || filterValue.isBlank() || filterValue.isEmpty())
			return klanten;
		else
			return (ObservableList<IKlant>) klanten.filtered(k -> k.getUsername().toLowerCase().contains(filterValue));
	}

	/**
	 * Haalt zowel klanten op, als per klant hun aantal openstaande bestellingen bij
	 * de ingelogde leverancier. Een bestelling wordt als openstaand beschouwt
	 * indien hij niet zowel betaald als geleverd is
	 * 
	 * @return Een lijst van een Maps, met als key een klant en als value het aantal
	 *         openstaande bestellingen van die klant
	 */
	public ObservableList<Map<IKlant, Long>> getKlantenMetHunOpenstaandeBestellingen() {

		ObservableList<Map<IKlant, Long>> data = FXCollections.observableArrayList();

		getKlantenLeverancier("").stream().forEach(klant -> {
			data.add(aantalBestellingenOpen(klant));

		});

		return data;
	}

	/**
	 * Haalt het aantal openstaande bestellingen op van een bepaalde klant bij de
	 * ingelogde leverancier. Een bestelling wordt als openstaand beschouwt indien
	 * hij niet zowel betaald als geleverd is
	 * 
	 * @param kl De klant waarvoor het aantal openstaande bestellingen opgehaald
	 *           wordt
	 * @return Een Map, met als key de meegegeven klant en als value het aantal
	 *         openstaande bestellingen van die klant
	 */
	public Map<IKlant, Long> aantalBestellingenOpen(IKlant kl) {
		Klant k = (Klant) kl;
		Map<IKlant, Long> result = new HashMap<>();
		result.put(k, bestellingbeheerder.telOpenstaandeBestellingenKlant((Leverancier) user, k));
		return result;
	}

	/**
	 * Haal alle bestellingen van een bepaalde klant bij de ingelogde leverancier op
	 * 
	 * @param kla De klant waarvan bestellingen opgehaald worden
	 * @return Een lijst van bestellingen
	 */
	public ObservableList<IBestelling> getKlantBestellingenLeverancier(IKlant kla) {
		return bestellingbeheerder.getKlantBestellingenLeverancier((Leverancier) user, kla);
	}

	/**
	 * Haalt de openstaande bestellingen van een bepaalde klant bij de ingelogde
	 * leverancier
	 * 
	 * @param kla         De klant wiens openstaande bestellingen opgehaald worden
	 * @param filterValue Een filterwaarde die kan worden ingegeven om te filteren
	 *                    op een bepaalde status of order ID
	 * @return Een lijst van openstaande bestellingen van de meegegeven klant bij de
	 *         ingelogde leverancier
	 */
	public ObservableList<IBestelling> getOpenKlantBestellingenLeverancier(IKlant kla, String filterValue) {
		ObservableList<IBestelling> klantBestellingen = bestellingbeheerder
				.getOpenKlantBestellingenLeverancier((Leverancier) user, kla);
		if (filterValue == null || filterValue.isBlank() || filterValue.isEmpty())
			return klantBestellingen;
		else
			return (ObservableList<IBestelling>) klantBestellingen
					.filtered(b -> b.getOrderID().toLowerCase().contains(filterValue)
							|| b.getOrderstatus().toString().toLowerCase().contains(filterValue)
							|| b.isBetalingsstatus().toString().toLowerCase().contains(filterValue));
	}

	/**
	 * Haal alle producten op die de ingelogde leverancier aanbiedt
	 * 
	 * @return Een lijst van aangeboden producten
	 */
	public ObservableList<IProduct> getProducten() {
		Leverancier u = (Leverancier) user;
		return productbeheerder.getProductenLeverancier(u);
	}

	/**
	 * Logt de gebruiker uit
	 * 
	 * @return
	 */
	public boolean logout() {
		return loginController.logout();
	}

	/**
	 * Voegt een nieuw product toe aan de catalogus van de ingelogde leverancier
	 * 
	 * @param aantalInStock
	 * @param naam
	 * @param eenheidsprijs
	 * @param url
	 * @param user2
	 * @return Het toegevoegde product
	 * @throws ProductInformationRequiredException Exception die geworpen wordt als
	 *                                             attributen leeg of ongeldig zijn
	 */
	public IProduct voegProductToe(int aantalInStock, String naam, double eenheidsprijs, String url, User user2)
			throws ProductInformationRequiredException {
		return prod.voegProductToe(aantalInStock, naam, eenheidsprijs, url, user2, 0);
	}

	public void productToevoegen(IProduct p) {
		productbeheerder.voegProductToe(p);
	}

	/**
	 * Werkt het aantal in stock bij van een bepaald product
	 * 
	 * @param prod2     Het product waarvoor het aantal in stock gewijzigd wordt
	 * @param wijziging De wijziging die uitgevoerd wordt
	 * @return
	 * @throws ProductInformationRequiredException Exception die geworpen wordt als
	 *                                             attributen leeg of ongeldig zijn
	 */
	public IProduct werkStockBij(IProduct prod2, int wijziging) throws ProductInformationRequiredException {
		Product p = (Product) prod2;
		p.werkStockBij(wijziging);
		return p;
	}

	/**
	 * Wijzigt een bepaald product
	 * 
	 * @param p Het product dat gewijzigd wordt
	 */
	public void bijwerkenProduct(IProduct p) {
		productbeheerder.updateProduct(p);
	}
}