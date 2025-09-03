package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.Bestelling;
import domein.BestellingBeheerder;
import domein.BetalingsStatusEnum;
import domein.IBestelling;
import domein.Klant;
import domein.LeveringStatusEnum;
import domein.User;
import javafx.collections.ObservableList;
import repository.BestellingDao;

@ExtendWith(MockitoExtension.class)
public class BestellingBeheerderTest {

	@Mock
	private BestellingDao bestellingDao;

	@InjectMocks
	private BestellingBeheerder bestellingBeheerder;

	@Test
	public void testGetBestellingLijst() {
		List<Bestelling> bestellingen = Arrays.asList(mock(Bestelling.class), mock(Bestelling.class));

		when(bestellingDao.getUserBestellingen(any(Klant.class))).thenReturn(bestellingen);

		ObservableList<IBestelling> result = bestellingBeheerder.getUserBestellingen(mock(Klant.class));

		assertEquals(bestellingen, result);
	}

	@Test
	public void testGetUserBestellingen() {

		User user = new Klant("", "");
		List<Bestelling> bestellingen = Arrays.asList(mock(Bestelling.class), mock(Bestelling.class));

		when(bestellingDao.getUserBestellingen(user)).thenReturn(bestellingen);

		ObservableList<IBestelling> result = bestellingBeheerder.getUserBestellingen(user);

		assertEquals(bestellingen, result);
	}

	@Test
	public void testUpdateBetalingsStatus() {

		String orderId = "ORD123";
		BetalingsStatusEnum status = BetalingsStatusEnum.BETAALD;

		bestellingBeheerder.updateBetalingsStatus(orderId, status);

		verify(bestellingDao).werkBetalingsstatusBij(orderId, status);
	}

	@Test
	public void testUpdateLeverStatus() {

		String orderId = "ORD123";
		LeveringStatusEnum status = LeveringStatusEnum.GELEVERD;

		bestellingBeheerder.updateLeverStatus(orderId, status);

		verify(bestellingDao).werkOrderstatusBij(orderId, status);
	}
}
