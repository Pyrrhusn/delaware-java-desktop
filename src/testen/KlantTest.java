package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import domein.Bestelling;
import domein.IBestelling;
import domein.Klant;

public class KlantTest {

	private Klant klant;

	@Mock
	private List<Bestelling> mockBestellingen;

	@BeforeEach
	public void setUp() {
		mockBestellingen = Arrays.asList(mock(Bestelling.class), mock(Bestelling.class), mock(Bestelling.class));

		klant = new Klant("testUser", "password");
		klant.setBestellingen(mockBestellingen);
	}

	@Test
	public void testGetBestellingen() {
		List<IBestelling> bestellingen = klant.getBestellingen();

		assertEquals(3, bestellingen.size());

		for (int i = 0; i < 3; i++) {
			assertEquals(mockBestellingen.get(i), bestellingen.get(i));
		}
	}

}
