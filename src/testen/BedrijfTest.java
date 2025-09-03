package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import domein.Bedrijf;
import domein.BedrijfVerandering;
import domein.BetalingsMethodesEnum;
import exception.InformationRequiredException;

public class BedrijfTest {

	private Bedrijf bedrijf;

	@BeforeEach
	public void setUp() {
		List<BetalingsMethodesEnum> betalingsInfo = new ArrayList<>();
		betalingsInfo.add(BetalingsMethodesEnum.PAYPAL);

		Map<String, String> contactGegevens = new HashMap<>();
		contactGegevens.put("contactpersoon", "John Doe");
		contactGegevens.put("email", "john@example.com");
		contactGegevens.put("telefoon", "+1234567890");

		bedrijf = new Bedrijf("logo.png", "Test Company", "Technology", "Test Avenue 32, 1000 Brussel, België",
				betalingsInfo, contactGegevens, "123456789", LocalDate.now(), true, true);
	}

	@ParameterizedTest
	@ValueSource(strings = { "IT", "Finance", "Healthcare" })
	public void testSetSectorValid(String sector) {
		bedrijf.setSector(sector);
		assertEquals(sector, bedrijf.getSector());
	}

	private static Stream<String> InvalidSectorValues() {
		return Stream.of("12", "A", "B", "a", "5", "100", "-10");
	}

	@ParameterizedTest
	@MethodSource("InvalidSectorValues")
	public void testSetSectorInvalid(String invalidSector) {
		String initialSector = bedrijf.getSector();
		bedrijf.setSector(invalidSector);
		assertEquals(initialSector, bedrijf.getSector());
	}

	@ParameterizedTest
	@ValueSource(strings = { "Oak St 32, 1000 Stad, Land", "Elm St 45, 2000 Dorp, Land",
			"Maple St 78, 3000 Gemeente, LandLand" })
	public void testSetAdresValid(String adres) {
		bedrijf.setAdres(adres);
		assertEquals(adres, bedrijf.getAdres());
	}

	@ParameterizedTest
	@ValueSource(strings = { "Invalid Address", "No Street Number, City, Country", "1234 Invalid St, Town, Country",
			"" })
	public void testSetAdresInvalid(String invalidAdres) {
		String initialAdres = bedrijf.getAdres();
		bedrijf.setAdres(invalidAdres);
		assertEquals(initialAdres, bedrijf.getAdres());
	}

	private static Stream<Map<String, String>> validContactGegevens() {
		return Stream.of(Map.of("contactpersoon", "Jane Doe", "email", "jane@example.com", "telefoon", "+1987654321"),
				Map.of("contactpersoon", "Bob Smith", "email", "bob@example.be", "telefoon", "+3244444444"),
				Map.of("contactpersoon", "A", "email", "test@gmail.net", "telefoon", "+3144444"));
	}

	@ParameterizedTest
	@MethodSource("validContactGegevens")
	public void testSetContactGegevensValid(Map<String, String> contactGegevens) {
		bedrijf.setContactGegevens(contactGegevens);
		assertEquals(contactGegevens, bedrijf.getContactGegevens());
	}

	@ParameterizedTest
	@MethodSource("invalidContactGegevens")
	public void testSetContactGegevensInvalid(Map<String, String> invalidContactGegevens) {
		Map<String, String> initialContactGegevens = bedrijf.getContactGegevens();
		bedrijf.setContactGegevens(invalidContactGegevens);
		assertEquals(initialContactGegevens, bedrijf.getContactGegevens());
	}

	private static Stream<Map<String, String>> invalidContactGegevens() {
		return Stream.of(Map.of("contactpersoon", "John Doe", "email", "invalid_email", "telefoon", "+32444444444"),
				Map.of("contactpersoon", "Jane Smith", "email", "invalid_email@domain", "telefoon", "+31888888"),
				Map.of("contactpersoon", "Alice", "email", "test@", "telefoon", "+1122334455"),
				Map.of("contactpersoon", "Alice", "email", "test.com", "telefoon", "+1122334455"),
				Map.of("contactpersoon", "Alice", "email", "test@gmail.com", "telefoon", "1122334455"), // Zonder +
				Map.of("contactpersoon", "Alice", "email", "test@gmail.com", "telefoon", "+3244444444444444"), // Net te
																												// lang
																												// tel
																												// nr
				Map.of("contactpersoon", "Alice", "email", "test@gmail.com", "telefoon", "+3244444444444444444444"),
				Map.of("contactpersoon", "Alice", "email", "test@gmail.com", "telefoon", "+324") // Te kort tel nr met +
		);
	}

	@Test
	public void testVoegBedrijfswijzigingsAanvraagToe() {
		BedrijfVerandering mockVerandering = mock(BedrijfVerandering.class);

		when(mockVerandering.getBedrijf()).thenReturn(bedrijf);

		bedrijf.addChange(mockVerandering);

		assertTrue(bedrijf.getVeranderingen().contains(mockVerandering));

		verify(mockVerandering).setBedrijf(bedrijf);
	}

	@Test
	public void testCheckWijzigingBedrijfWithInvalidData() {
		assertThrows(InformationRequiredException.class, () -> {
			bedrijf.checkWijzigingBedrijf("new_logo.png", "Invalid Sector", "Invalid Address", "Invalid Contact Person",
					"invalid_email", "1234567890", new ArrayList<>());
		});
	}

	@Test
	public void testCheckWijzigingBedrijfWithValidData() throws InformationRequiredException {
		bedrijf.checkWijzigingBedrijf("new_logo.png", "New Sector", "Test Street 1, 9000 Gent, België", "Jane Doe",
				"+1987654321", "jane@example.com", new ArrayList<>());
		assertEquals("new_logo.png", bedrijf.getLogoUrl());
		assertEquals("New Sector", bedrijf.getSector());
		assertEquals("Test Street 1, 9000 Gent, België", bedrijf.getAdres());
		assertEquals("Jane Doe", bedrijf.getContactGegevens().get("contactpersoon"));
		assertEquals("jane@example.com", bedrijf.getContactGegevens().get("email"));
		assertEquals("+1987654321", bedrijf.getContactGegevens().get("telefoon"));
	}
}
