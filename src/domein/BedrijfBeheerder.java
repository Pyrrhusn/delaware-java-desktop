package domein;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.BedrijfDao;
import repository.BedrijfDaoJpa;

/**
 * BedrijfBeheerder klasse
 */
public class BedrijfBeheerder {
	private BedrijfDao bedrijfDao;
	private ObservableList<IBedrijf> bedrijven;

	/**
	 * Constructor voor BedrijfBeheerder. Hier wordt een BedrijfDao ingesteld en
	 * worden alle bedrijven opgehaald
	 */
	public BedrijfBeheerder() {
		bedrijfDao = new BedrijfDaoJpa();
		bedrijven = FXCollections.observableArrayList(bedrijfDao.findAll());
	}

	/**
	 * Haalt het aantal klanten van een bepaald bedrijf op
	 * 
	 * @param naam De naam van het bedrijf waarvoor het aantal klanten wordt
	 *             gevonden
	 * @return Het aantal klanten van het bedrijf
	 */
	public int getAantalKlanten(String naam) {
		IBedrijf b = bedrijven.stream().filter(bedrijf -> bedrijf.getNaam().equals(naam)).findFirst().get();
		return b.getLeverancier().getAantalKlanten();
	}

	/**
	 * Haalt alle bedrijven op
	 * 
	 * @return Alle bedrijven uit de databank
	 */
	public ObservableList<IBedrijf> getBedrijvenLijst() {
		return bedrijven;
	}

	/**
	 * Haalt alle bedrijven op waarvoor er wijzigingsaanvragen zijn die nog niet
	 * goedgekeurd of afgekeurd zijn
	 * 
	 * @return Alle bedrijven waarvoor er wijzigingsaanvragen zijn die nog niet
	 *         goedgekeurd of afgekeurd zijn
	 */
	public ObservableList<IBedrijfVerandering> getBedrijvenMetAanVragenVoorWijzingen() {
		List<Bedrijf> bedrijven = bedrijfDao.getBedrijvenMetAanVragenVoorWijzingen();

		ObservableList<IBedrijfVerandering> bedrijvenVoorReview = bedrijven.stream()
				.flatMap(bedrijf -> bedrijf.getVeranderingen().stream())
				.collect(Collectors.toCollection(FXCollections::observableArrayList));

		return bedrijvenVoorReview;
	}

	public Bedrijf getSpecifiekBedrijf(Bedrijf b) {
		return bedrijfDao.get(b.getId());
	}

	/**
	 * Voegt een nieuw bedrijf toe
	 * 
	 * @param b Het toe te voegen bedrijf
	 */
	public void voegBedrijfToe(Bedrijf b) {
		this.bedrijfDao.voegBedrijfToe(b);
		this.bedrijven.add(b);
	}

	/**
	 * Wijzigt een bestaand bedrijf
	 * 
	 * @param b Het bedrijf dat gewijzigd wordt
	 */
	public void updateBedrijf(Bedrijf b) {
		bedrijfDao.updateBedrijf(b);
	}
}