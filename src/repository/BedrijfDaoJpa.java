package repository;

import java.util.List;

import domein.Bedrijf;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

public class BedrijfDaoJpa extends GenericDaoJpa<Bedrijf> implements BedrijfDao {
	public BedrijfDaoJpa() {
		super(Bedrijf.class);
	}

	@Override
	public Bedrijf getBedrijf(String btwNummer) {
		try {
			return em.createNamedQuery("Bedrijf.findBedrijf", Bedrijf.class).setParameter("btwNummer", btwNummer)
					.getSingleResult();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException("geen bedrijf met dit btwnummer gevonden");
		}
	}

	EntityTransaction trans = null;

	@Override
	public void voegBedrijfToe(Bedrijf b) {
		try {

			trans = em.getTransaction();
			trans.begin();
			insert(b);
			trans.commit();
		} catch (Exception ex) {
			if (trans != null && trans.isActive())
				trans.rollback();

			String errorMessage = ex.getMessage();
			String errorType = "";
			if (errorMessage.contains("bedrijf.BTWNUMMER")) {
				errorType = "btwnummer";
			} else if (errorMessage.contains("bedrijf.NAAM")) {
				errorType = "naam";
			}

			if ("btwnummer".equals(errorType)) {
				throw new RuntimeException("Er bestaat al een bedrijf met dit BTW nummer", ex);
			} else {
				throw new RuntimeException("Er bestaat al een bedrijf met deze naam", ex);
			}
		}

	}

	EntityTransaction trans2 = null;

	@Override
	public void updateBedrijf(Bedrijf b) {
		try {
			trans2 = em.getTransaction();
			trans2.begin();
			update(b);
			trans2.commit();
		} catch (Exception e) {
			if (trans2 != null && trans2.isActive()) {
				trans2.rollback();
				throw new RuntimeException("fout bij udpaten bedrijf");
			}

		}
	}

	@Override
	public List<Bedrijf> getBedrijvenMetAanVragenVoorWijzingen() {
		try {
			return em.createNamedQuery("Bedrijf.findPendingVeranderingen", Bedrijf.class).getResultList();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException("geen bedrijven die verzoek voor veranderingen hebben gestuurd");

		}

	}

}