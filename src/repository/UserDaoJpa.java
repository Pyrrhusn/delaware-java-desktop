package repository;

import java.util.List;

import domein.Klant;
import domein.Leverancier;
import domein.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

public class UserDaoJpa extends GenericDaoJpa<User> implements UserDao {
	public UserDaoJpa() {
		super(User.class);
	}

	@Override
	public User getUser(String gebruikersnaam) throws EntityNotFoundException {
		try {
			return em.createNamedQuery("User.haalUserOp", User.class).setParameter("gebruikersnaam", gebruikersnaam)
					.getSingleResult();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public List<Klant> getKlanten(Leverancier leverancier) throws EntityNotFoundException {
		try {
			String gebruikersnaam = leverancier.getUsername();
			String pw = leverancier.getPassword();
			return em.createNamedQuery("Leverancier.haalKlanten", Klant.class)
					.setParameter("gebruikersnaam", gebruikersnaam).setParameter("wachtwoord", pw).getResultList();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
	
	@Override
	public String getUserHashWW(String gebruikersnaam) throws EntityNotFoundException {
		try {
			return em.createNamedQuery("User.haalUserHashWWOp", String.class)
					.setParameter("gebruikersnaam", gebruikersnaam).getSingleResult();
		} catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}

}