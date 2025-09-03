package domein;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import domein.singleton.UserSingleton;
import jakarta.persistence.EntityNotFoundException;

/**
 * LoginController klasse, is verantwoordelijk voor de login logica
 */
public class LoginController {
	private User user;
	private static final int ARGON2_ITERATIONS = 10;
	private static final int ARGON2_HASHLENGTH = 32;
	private static final int ARGON2_PARALLELISM = 1;
	private static final int ARGON2_MEMORY = 40960;
	private static final int ARGON2_SALTLENGTH = 32;
	private UserBeheerder userbeheerder;

	/**
	 * Constructor voor LoginController
	 */
	public LoginController() {
		this.userbeheerder = new UserBeheerder();
	}

	/**
	 * Logt de gebruiker in door middel van een ingegeven gebruikersnaam en
	 * wachtwoord
	 * 
	 * @param gebruikersnaam De gebruikersnaam die de gebruiker ingeeft om in te
	 *                       loggen
	 * @param wachtwoord     Het wachtwoord die de gebruiker ingeeft om in te loggen
	 * @return
	 */
	public final boolean login(String gebruikersnaam, String wachtwoord) {
		try {
			String hashDB = userbeheerder.getUserHashWW(gebruikersnaam);
			Argon2PasswordEncoder argon2 = new Argon2PasswordEncoder(ARGON2_SALTLENGTH, ARGON2_HASHLENGTH,
					ARGON2_PARALLELISM, ARGON2_MEMORY, ARGON2_ITERATIONS);
			if (argon2.matches(wachtwoord, hashDB))
				user = UserSingleton.getInstance(gebruikersnaam, userbeheerder);
		} catch (EntityNotFoundException e) {
			System.out.print(e.getMessage());
		}

		if (user != null)
			return true;

		return false;
	}

	/**
	 * Haalt de ingelogde gebruiker op
	 * 
	 * @return De ingelogde gebruiker
	 */
	public final User getUser() {
		return user;
	}

	/**
	 * Hasht het meegegeven wachtwoord
	 * 
	 * @param password Het wachtwoord dat gehasht moet worden
	 * @return Het gehashte wachtwoord
	 */
	public static String hashPassword(String password) {
		Argon2PasswordEncoder argon2 = new Argon2PasswordEncoder(ARGON2_SALTLENGTH, ARGON2_HASHLENGTH,
				ARGON2_PARALLELISM, ARGON2_MEMORY, ARGON2_ITERATIONS);
		return argon2.encode(password);
	}

	/**
	 * Logt de gebruiker uit
	 * 
	 * @return
	 */
	public boolean logout() {
		this.user = null;
		UserSingleton.deleteInstance();
		return true;
	}
}