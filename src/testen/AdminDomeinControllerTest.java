package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import domein.AdminDomeinController;
import domein.Bedrijf;
import domein.BedrijfBeheerder;
import domein.BedrijfBuilder;
import domein.BetalingsMethodesEnum;
import domein.IBedrijf;
import domein.IBedrijfVerandering;
import domein.LoginController;
import dto.BedrijfDTO;
import exception.InformationRequiredException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminDomeinControllerTest {
	private AdminDomeinController adc;
	@Mock
	private BedrijfBuilder bedrijfBuilderMock;
	@Mock
	private BedrijfBeheerder bedrijfBeheerderMock;
	@Mock
	private Bedrijf bedrijfMock;
	@Mock
	private LoginController loginControllerMock;

	@BeforeEach
	public void setup() {
		bedrijfBeheerderMock = mock(BedrijfBeheerder.class);
		bedrijfBuilderMock = mock(BedrijfBuilder.class);
		bedrijfMock = mock(Bedrijf.class);
		loginControllerMock = mock(LoginController.class);
		adc = new AdminDomeinController(loginControllerMock, bedrijfBuilderMock, bedrijfBeheerderMock);
	}

	@Test
	public void createBedrijfTest() {
		BedrijfDTO bedrijfDTO = new BedrijfDTO("logo.png", "TestBedrijf", "IT", "Main St 123, 1000 City, Country",
				"BE0123456788", "John Doe", "+123456789", "john.doe@example.com",
				Arrays.asList(BetalingsMethodesEnum.PAYPAL));
		adc.createBedrijf(bedrijfDTO);
		verify(bedrijfBuilderMock).createBedrijf(bedrijfDTO);
	}

	@Test
	public void getBedrijfTest() throws InformationRequiredException {
		Bedrijf verwachteBedrijf = new Bedrijf("logo.png", "TestBedrijf", "IT", "Main St 123, 1000 City, Country",
				Arrays.asList(BetalingsMethodesEnum.PAYPAL),
				Map.of("contactpersoon", "John Doe", "email", "john.doe@example.com", "telefoon", "+123456789"),
				"BE0123456788", LocalDate.now(), true, true);
		when(bedrijfBuilderMock.getBedrijf()).thenReturn(verwachteBedrijf);
		Bedrijf echteBedrijf = adc.getBedrijf();
		verify(bedrijfBuilderMock).getBedrijf();
		assertNotNull(echteBedrijf);
		assertEquals(verwachteBedrijf, echteBedrijf);
	}

	@Test
	public void voegBedrijfToeTest() throws InformationRequiredException {
		when(bedrijfBuilderMock.getBedrijf()).thenReturn(bedrijfMock);
		doNothing().when(bedrijfBeheerderMock).voegBedrijfToe(bedrijfMock);
		adc.getBedrijf();
		adc.voegBedrijfToe();
		verify(bedrijfBeheerderMock).voegBedrijfToe(bedrijfMock);
	}

	@Test
	public void geefAlleBetalingsMethodenTest() {
		List<String> echteMethoden = adc.geefAlleBetalingsMethoden();

		List<String> verwachteMethoden = Arrays.asList("PayPal", "Bancontact", "Visa", "Maestro", "Overschrijving",
				"Factuur");
		assertEquals(verwachteMethoden, echteMethoden);
	}

	@Test
	public void getBedrijvenTest() {
		ObservableList<IBedrijf> mockBedrijvenList = FXCollections.observableArrayList(mock(IBedrijf.class));
		when(bedrijfBeheerderMock.getBedrijvenLijst()).thenReturn(mockBedrijvenList);
		ObservableList<IBedrijf> lijstBedrijven = adc.getBedrijven();
		verify(bedrijfBeheerderMock).getBedrijvenLijst();
		assertEquals(mockBedrijvenList, lijstBedrijven);
	}

	@Test
	public void logoutTest() {
		when(loginControllerMock.logout()).thenReturn(true);
		assertTrue(adc.logout());
	}

	@Test
	public void updateBedrijfTest() {
		Bedrijf mockBedrijf = mock(Bedrijf.class);
		adc.updateBedrijf(mockBedrijf);
		verify(bedrijfBeheerderMock).updateBedrijf(mockBedrijf);
	}

	@Test
	public void checkWijzigingBedrijfTest() throws InformationRequiredException {
		String logo = "new_logo";
		String sector = "new_sector";
		String address = "new_address";
		String contactPersoon = "new_contact";
		String phoneNumber = "new_phone";
		String email = "new_email";
		List<BetalingsMethodesEnum> betalingsMethodes = Arrays.asList(BetalingsMethodesEnum.PAYPAL,
				BetalingsMethodesEnum.FACTUUR);

		adc.checkWijzigingBedrijf(bedrijfMock, logo, sector, address, contactPersoon, phoneNumber, email,
				betalingsMethodes);

		verify(bedrijfMock).checkWijzigingBedrijf(logo, sector, address, contactPersoon, phoneNumber, email,
				betalingsMethodes);
	}

	@Test
	public void getBedrijvenMetAanVragenVoorWijzingenTest() {
		ObservableList<IBedrijfVerandering> mockVeranderingenList = FXCollections
				.observableArrayList(mock(IBedrijfVerandering.class));
		when(bedrijfBeheerderMock.getBedrijvenMetAanVragenVoorWijzingen()).thenReturn(mockVeranderingenList);
		ObservableList<IBedrijfVerandering> result = adc.getBedrijvenMetAanVragenVoorWijzingen();
		verify(bedrijfBeheerderMock).getBedrijvenMetAanVragenVoorWijzingen();
		assertEquals(mockVeranderingenList, result);
	}

	@Test
	public void setActiefLeverancierTest() {
		adc.setActiefLeverancier(true, bedrijfMock);
		verify(bedrijfMock).setActiefLeverancier(true);
	}

	@Test
	public void setActiefKlantTest() {
		adc.setActiefKlant(true, bedrijfMock);
		verify(bedrijfMock).setActiefKlant(true);
	}

	@Test
	public void afkeurenTest() {
		IBedrijfVerandering mockBedrijfVerandering = mock(IBedrijfVerandering.class);
		boolean verwacht = true;
		adc.afkeuren(mockBedrijfVerandering);
		verify(mockBedrijfVerandering).setAfgekeurd(verwacht);
	}
}
