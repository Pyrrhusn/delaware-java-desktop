package testen;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import domein.Bestelling;
import domein.BetalingsStatusEnum;
import domein.Klant;
import domein.Leverancier;
import domein.LeveringStatusEnum;

public class BestellingTest {
	private Klant klant;
	private Leverancier leverancier;

	@BeforeEach
	public void setUp() {
		klant = new Klant("klantUsername", "klantPassword");
		leverancier = new Leverancier("leverancierUsername", "leverancierPassword");
	}

	@Test
	void fouteBetalingStatusWijziging() {
		Bestelling bestelling = new Bestelling("ORD456", LocalDate.now(), "456 Pine St", LeveringStatusEnum.GELEVERD,
				BetalingsStatusEnum.BETAALD, klant, leverancier, LocalDate.now().plusDays(30),30);
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> bestelling.wijzigBetalingsstatus("Niet Betaald", bestelling.isBetalingsstatus()));
	}

	@Test
	void correcteBetalingStatusWijziging() {
		Bestelling bestelling = new Bestelling("ORD456", LocalDate.now(), "456 Pine St", LeveringStatusEnum.GELEVERD,
				BetalingsStatusEnum.NIETBETAALD, klant, leverancier, LocalDate.now().plusDays(30),30);
		bestelling.wijzigBetalingsstatus("Betaald", bestelling.isBetalingsstatus());
		Assertions.assertEquals(BetalingsStatusEnum.BETAALD, bestelling.isBetalingsstatus());
	}

	@ParameterizedTest
	@CsvSource({ "Verzonden,In behandeling ", "Geleverd,Verzonden " })
	void testCorrecteLeveringStatus(String volgende, String huidige) {
		LeveringStatusEnum huidigeEnum = LeveringStatusEnum.fromString(huidige);
		Bestelling bestelling = new Bestelling("ORD456", LocalDate.now(), "456 Pine St", huidigeEnum,
				BetalingsStatusEnum.BETAALD, klant, leverancier, LocalDate.now().plusDays(30),30);
		bestelling.wijzigLeveringStatus(volgende, huidigeEnum);
		Assertions.assertEquals(LeveringStatusEnum.fromString(volgende), bestelling.getOrderstatus());
	}

	@ParameterizedTest
	@CsvSource({ "Geleverd,In Behandeling ", "In behandeling,Verzonden ", "In behandeling, Geleverd" })
	void testFouteLeveringStatus(String volgende, String huidige) {
		LeveringStatusEnum huidigeEnum = LeveringStatusEnum.fromString(huidige);
		Bestelling bestelling = new Bestelling("ORD456", LocalDate.now(), "456 Pine St", huidigeEnum,
				BetalingsStatusEnum.BETAALD, klant, leverancier, LocalDate.now().plusDays(30),30);

		Assertions.assertThrows(IllegalArgumentException.class,
				() -> bestelling.wijzigLeveringStatus(volgende, huidigeEnum));
	}

}