package domein;

import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.UserDao;
import repository.UserDaoJpa;

/**
 * UserBeheerder klasse bevat een UserDao om met de databank te werken
 */
public class UserBeheerder {
	private UserDao userdao;
	private List<User> gebruikers;

	/**
	 * Constructor voor UserBeheerder. Hier wordt een UserDao ingesteld en worden
	 * alle gebruikers opgehaald
	 */
	public UserBeheerder() {
		userdao = new UserDaoJpa();
		gebruikers = userdao.findAll();
	}

	public List<User> getUsers() {
		return Collections.unmodifiableList(gebruikers);
	}

	/**
	 * Haalt alle klanten die bestellingen hebben bij een bepaalde leverancier op
	 * 
	 * @param l De leverancier wiens klanten worden opgehaald
	 * @return Een lijst van klanten die bestellingen hebben bij de meegegeven
	 *         leverancier
	 */
	public ObservableList<IKlant> geefKlantenPerLeverancier(Leverancier l) {
		ObservableList<IKlant> bestellingenObservableList = FXCollections.observableArrayList();
		bestellingenObservableList.addAll(userdao.getKlanten(l));
		return bestellingenObservableList;
	}

	public User getUser(String gebruikersnaam) {
		return userdao.getUser(gebruikersnaam);
	}

	public String getUserHashWW(String gebruikersnaam) {

		return userdao.getUserHashWW(gebruikersnaam);
	}
}