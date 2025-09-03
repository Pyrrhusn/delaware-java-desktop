package repository;

import java.util.List;

import domein.Klant;
import domein.Leverancier;
import domein.User;
import jakarta.persistence.EntityNotFoundException;

public interface UserDao extends GenericDao<User> {

	User getUser(String gebruikersnaam) throws EntityNotFoundException;

	List<Klant> getKlanten(Leverancier leverancier) throws EntityNotFoundException;
	
	public String getUserHashWW(String gebruikersnaam) throws EntityNotFoundException;
}