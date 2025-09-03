package repository;

import java.time.LocalDate;
import java.util.List;

import domein.Bestelling;
import domein.BetalingsStatusEnum;
import domein.Klant;
import domein.Leverancier;
import domein.LeveringStatusEnum;
import domein.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

public class BestellingDaoJpa extends GenericDaoJpa<Bestelling> implements BestellingDao {
	public BestellingDaoJpa() {
		super(Bestelling.class);
	}

	@Override
	public List<Bestelling> getUserBestellingen(User user) throws EntityNotFoundException {
		try {
			if (user instanceof Klant) {
				return em.createNamedQuery("Bestelling.findKlant", Bestelling.class)
						.setParameter("klant", user.getUsername()).getResultList();
			} else if (user instanceof Leverancier) {
				return em.createNamedQuery("Bestelling.findLeverancier", Bestelling.class)
						.setParameter("leverancier", user.getUsername()).getResultList();
			} else {
				throw new NoResultException();
			}
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
	
	@Override
	public List<Bestelling> getKlantBestellingenLeverancier(Leverancier l, Klant k) throws EntityNotFoundException {
		try {
				return em.createNamedQuery("Bestelling.haalKlantBestellingLeverancier", Bestelling.class).setParameter("lusername", l.getUsername()).setParameter("kusername", k.getUsername()).getResultList();
			
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public Bestelling getUserBestellingPerOrderId(String orderId) throws EntityNotFoundException {
		try {
			
				return em.createNamedQuery("Bestelling.findByOrderId", Bestelling.class).setParameter("orderID", orderId).getSingleResult();
			
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public void werkBetalingsstatusBij(String orderId, BetalingsStatusEnum betalingsstatus) {
		try {
			em.getTransaction().begin();
			em.createNamedQuery("Bestelling.wijzigBetalingsstatus", Bestelling.class).setParameter("orderID", orderId)
					.setParameter("betalingsstatus", betalingsstatus).executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}

	@Override
	public void werkOrderstatusBij(String orderId, LeveringStatusEnum orderstatus) {
		try {
			em.getTransaction().begin();
			em.createNamedQuery("Bestelling.wijzigOrderStatus", Bestelling.class).setParameter("orderID", orderId)
					.setParameter("orderstatus", orderstatus).executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}
	
	@Override
	public List<Bestelling> haalOpenBestellingKlant(Leverancier leverancier, Klant klant) throws EntityNotFoundException {

		try {
			return em.createNamedQuery("Bestelling.haalOpenKlantBestellingLeverancier", Bestelling.class).setParameter("gebruikersnaamKlant", klant.getUsername()).setParameter("gebruikersnaamLeverancier", leverancier.getUsername()).setParameter("Betaald", BetalingsStatusEnum.BETAALD).setParameter("Geleverd", LeveringStatusEnum.GELEVERD).getResultList();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
	

	@Override
	public long telBestelling(Klant klant, Leverancier leverancier) throws EntityNotFoundException {

		try {
			return em.createNamedQuery("Bestelling.telOpenBestellingen", Long.class).setParameter("gebruikersnaamKlant", klant.getUsername()).setParameter("gebruikersnaamLeverancier", leverancier.getUsername()).setParameter("Betaald", BetalingsStatusEnum.BETAALD).setParameter("Geleverd", LeveringStatusEnum.GELEVERD).getSingleResult();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
	
	@Override
	public List<Bestelling> haalBestellingenVoorBetalingsherinnering() throws EntityNotFoundException {
		try {
			BetalingsStatusEnum nietBetaaldStatus = BetalingsStatusEnum.fromString("Niet Betaald");

	        LocalDate currentDate = LocalDate.now();
	        LocalDate endDate = currentDate.plusDays(3);
	        
			return em.createNamedQuery("Bestelling.haalBestellingenVoorBetalingsherinnering", Bestelling.class).setParameter("betalingStatus", nietBetaaldStatus).setParameter("startDate", currentDate).setParameter("endDate", endDate).getResultList();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
	
	@Override
	public void werkHeeftBetalingsherinneringBij() {
		try {
			em.getTransaction().begin();
			BetalingsStatusEnum nietBetaaldStatus = BetalingsStatusEnum.fromString("Niet Betaald");

	        LocalDate currentDate = LocalDate.now();
	        LocalDate endDate = currentDate.plusDays(3);
			em.createNamedQuery("Bestelling.wijzigHeeftBetalingsherinnering", Bestelling.class).setParameter("betalingStatus", nietBetaaldStatus).setParameter("startDate", currentDate).setParameter("endDate", endDate).executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}

}