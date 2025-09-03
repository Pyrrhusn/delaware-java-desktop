package repository;

import java.util.List;

import domein.Bestelling;
import domein.BetalingsStatusEnum;
import domein.Klant;
import domein.Leverancier;
import domein.LeveringStatusEnum;
import domein.User;
import jakarta.persistence.EntityNotFoundException;

public interface BestellingDao extends GenericDao<Bestelling> {
	List<Bestelling> getUserBestellingen(User user) throws EntityNotFoundException;

	void werkOrderstatusBij(String orderId, LeveringStatusEnum orderstatus);

	void werkBetalingsstatusBij(String orderId, BetalingsStatusEnum betalingsstatus);

	Bestelling getUserBestellingPerOrderId(String orderId) throws EntityNotFoundException;

	long telBestelling(Klant k, Leverancier l) throws EntityNotFoundException;

	List<Bestelling> getKlantBestellingenLeverancier(Leverancier l, Klant k) throws EntityNotFoundException;

	List<Bestelling> haalOpenBestellingKlant(Leverancier leverancier, Klant klant) throws EntityNotFoundException;
	
	List<Bestelling> haalBestellingenVoorBetalingsherinnering() throws EntityNotFoundException;
	
	void werkHeeftBetalingsherinneringBij();
}