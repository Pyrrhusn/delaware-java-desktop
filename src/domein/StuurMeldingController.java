package domein;

import java.time.LocalDate;

import javafx.collections.ObservableList;

/**
 * StuurMeldingController klasse, is verantwoordelijk voor de logica rond
 * betalingsherinneringen
 */
public class StuurMeldingController {

	private BestellingBeheerder bestellingbeheerder;

	/**
	 * Constructor voor StuurMeldingController
	 */
	public StuurMeldingController() {
		this.bestellingbeheerder = new BestellingBeheerder();
	}

	/**
	 * Haal alle bestellingen op waarvoor er automatisch een betalingsherinnering
	 * moet worden gestuurd. Dit zijn bestellingen waarvoor de betaaldag binnen 3
	 * dagen valt en waarvoor er nog geen betalingsherinnering gestuurd is
	 * 
	 * @return Een lijst van alle bestellingen waarvoor een automatische
	 *         betalingsherinnering gestuurd moet worden
	 */
	public ObservableList<IBestelling> haalBestellingenVoorBetalingsherinnering() {
		return bestellingbeheerder.haalBestellingenVoorBetalingsherinnering();
	}

	/**
	 * Wijzigt het attribuut "heeftBetalingsHerinnering" van bestelling, dit houdt
	 * bij of er voor deze bestelling al een herinnering gestuurd is
	 */
	public void werkHeeftBetalingsherinneringBij() {
		bestellingbeheerder.werkHeeftBetalingsherinneringBij();
	}

	/**
	 * Wijzigt de datum van de laatste betalingsherinnering voor een bepaalde
	 * bestelling
	 * 
	 * @param bestelling De bestelling waarvan de datum van de laatste
	 *                   betalingsherinnering gestuurd wordt
	 * @param datum      De nieuwe laatste betalingsherinnering datum
	 */
	public void werkDatumBetalingsHerinneringbij(IBestelling bestelling, LocalDate datum) {
		Bestelling bestelling2 = (Bestelling) bestelling;
		bestelling2.setDatumLaatsteBetalingsherinnering(datum);
	}

}
