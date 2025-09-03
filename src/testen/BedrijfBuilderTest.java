package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import domein.Bedrijf;
import domein.BedrijfBuilder;
import domein.BetalingsMethodesEnum;
import domein.IKlant;
import domein.ILeverancier;
import dto.BedrijfDTO;
import exception.InformationRequiredException;

public class BedrijfBuilderTest {

    private BedrijfBuilder bedrijfBuilder;
    private BedrijfDTO bedrijfDTO;

    @BeforeEach
    public void setUp() {
        bedrijfBuilder = new BedrijfBuilder();
        bedrijfDTO = new BedrijfDTO("logo.png", "TestBedrijf", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                "John Doe", "+123456789", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL));
    }

    @Test
    public void testCreateBedrijf() throws InformationRequiredException {
        bedrijfBuilder.createBedrijf(bedrijfDTO);
        Bedrijf bedrijf = bedrijfBuilder.getBedrijf();
        
        

        assertEquals("logo.png", bedrijf.getLogoUrl());
        assertEquals("TestBedrijf", bedrijf.getNaam());
        assertEquals("IT", bedrijf.getSector());
        assertEquals("Main St 123, 1000 City, Country", bedrijf.getAdres());
        assertEquals("BE0123456788", bedrijf.getBTWNummer());
        assertEquals(LocalDate.now(), bedrijf.getAccountSinds());
        assertTrue(bedrijf.isActiefKlant());
        assertTrue(bedrijf.isActiefLeverancier());

        ILeverancier leverancier = bedrijf.getLeverancier();
        assertNotNull(leverancier);
        assertEquals("TestBedrijf_leverancier", leverancier.getUsername());
        assertEquals(bedrijf.getNaam() + "_leverancier", leverancier.getUsername());

        IKlant klant = bedrijf.getKlant();
        assertNotNull(klant);
        assertEquals("TestBedrijf_klant", klant.getUsername());
        assertEquals(bedrijf.getNaam() + "_klant", klant.getUsername());

        List<BetalingsMethodesEnum> betalingsMethodes = bedrijf.getBetalingsInfo();
        assertNotNull(betalingsMethodes);
        assertEquals(1, betalingsMethodes.size());
        assertEquals(BetalingsMethodesEnum.PAYPAL, betalingsMethodes.get(0));

        Map<String, String> contactGegevens = bedrijf.getContactGegevens();
        assertNotNull(contactGegevens);
        assertEquals("John Doe", contactGegevens.get("contactpersoon"));
        assertEquals("+123456789", contactGegevens.get("telefoon"));
        assertEquals("john.doe@example.com", contactGegevens.get("email"));
    }

    @ParameterizedTest
    @MethodSource("invalidBedrijfDTOs")
    public void testRequiredElementsException(BedrijfDTO invalidBedrijfDTO) {
        assertThrows(InformationRequiredException.class, () -> {
            bedrijfBuilder.createBedrijf(invalidBedrijfDTO);
            Bedrijf bedrijf = bedrijfBuilder.getBedrijf();
        });
    }

    private static Stream<BedrijfDTO> invalidBedrijfDTOs() {
        return Stream.of(
            new BedrijfDTO(null, null, null, null, null, null, null, null, null),
            new BedrijfDTO("", "", "", "", "", "", "", "", null),
            new BedrijfDTO(null, "TestBedrijf", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "+123456789", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Lege naam
            new BedrijfDTO("logo.png", "", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "+123456789", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            //Ongeldige naam
            new BedrijfDTO("logo.png", "A", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "+123456789", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            //Ongeldige sector
            new BedrijfDTO("logo.png", "TestBedrijf", "A", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "+123456789", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Lege sector
            new BedrijfDTO("logo.png", "TestBedrijf", "", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "+123456789", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Ongeldig adres
            new BedrijfDTO("logo.png", "TestBedrijf", "IT", "Invalid Address", "BE0123456788",
                    "John Doe", "+123456789", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Ongeldig BTW nr
            new BedrijfDTO("logo.png", "TestBedrijf", "IT", "Main St 123, 1000 City, Country", "InvalidBTWNumber",
                    "John Doe", "+123456789", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Ongeldig Belgisch BTW nr
            new BedrijfDTO("logo.png", "", "IT", "Main St 123, 1000 City, Country", "BE5123456788",
                    "John Doe", "+123456789", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Ongeldige contactpersoon
            new BedrijfDTO("logo.png", "TestBedrijf", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "", "+123456789", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Ongeldig tel nr
            new BedrijfDTO("logo.png", "TestBedrijf", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "InvalidPhoneNumber", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Ongeldige email
            new BedrijfDTO("logo.png", "TestBedrijf", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "+123456789", "invalid-email", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Lege email
            new BedrijfDTO("logo.png", "TestBedrijf", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "+123456789", "", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Leeg tel nr
            new BedrijfDTO("logo.png", "TestBedrijf", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Lege betalingsmogelijkheden
            new BedrijfDTO("logo.png", "TestBedrijf", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "+123456789", "john.doe@example.com", Collections.emptyList()),
            // Ongeldig logo, naam, en email
            new BedrijfDTO(null, "", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "+123456789", "invalid-email", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Leeg logo, naam, en email
            new BedrijfDTO("", "", "IT", "Main St 123, 1000 City, Country", "BE0123456788",
                    "John Doe", "+123456789", "", Arrays.asList(BetalingsMethodesEnum.PAYPAL)),
            // Ongeldige sector, adres, en tel nr
            new BedrijfDTO("logo.png", "TestBedrijf", "", "Invalid Address", "BE0123456788",
                    "John Doe", "InvalidPhoneNumber", "john.doe@example.com", Arrays.asList(BetalingsMethodesEnum.PAYPAL))
        );
    }
}