package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.IKlant;
import domein.Klant;
import domein.Leverancier;
import domein.User;
import domein.UserBeheerder;
import javafx.collections.ObservableList;
import repository.UserDao;

@ExtendWith(MockitoExtension.class)
public class UserBeheerderTest {

	@Mock
	private UserDao userDao;

	@InjectMocks
	private UserBeheerder userBeheerder;

	@Test
	public void testGeefKlantenPerLeverancier() {
		Leverancier leverancier = mock(Leverancier.class);
		List<Klant> klanten = Arrays.asList(mock(Klant.class), mock(Klant.class));
		when(userDao.getKlanten(leverancier)).thenReturn(klanten);
		ObservableList<IKlant> result = userBeheerder.geefKlantenPerLeverancier(leverancier);
		assertEquals(klanten, result);
	}

	@Test
	public void testGetUser() {
		String username = "testUser";
		User user = mock(User.class);
		when(userDao.getUser(username)).thenReturn(user);
		User result = userBeheerder.getUser(username);
		assertEquals(user, result);
	}

	@Test
	public void testGetUserHashWW() {
		String username = "usernameHier";
		String hash = "passHier";

		when(userDao.getUserHashWW(username)).thenReturn(hash);
		String result = userBeheerder.getUserHashWW(username);
		assertEquals(hash, result);
	}
}
