package domein.singleton;

import domein.User;
import domein.UserBeheerder;

public class UserSingleton {
	private static User instance;

	private UserSingleton() {
	}

	public static User getInstance(String username, UserBeheerder userbeheerder) {
		if (instance == null) {
			instance = userbeheerder.getUser(username);
		}
		return instance;
	}

	public static void deleteInstance() {
		instance = null;
	}
}
