package testen;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.Leverancier;
import domein.Product;
import exception.ProductInformationRequiredException;

@ExtendWith(MockitoExtension.class)
public class ProductTest {

	@Mock
	private Leverancier leverancierMock;

	@InjectMocks
	private Product product;

	@Test
	public void testProductCreatie() throws ProductInformationRequiredException {

		Mockito.when(leverancierMock.getUsername()).thenReturn("testLeverancier");

		Product.ProductBuilder builder = new Product.ProductBuilder();
		product = builder.leverancier(leverancierMock).naam("Test Product").inStock(10).eenheidsprijs(10.0)
				.fotoUrl("http://example.com").build();

		assertEquals("Test Product", product.getNaam());
		assertEquals(10, product.getInStock());

		assertEquals(10.0, product.getEenheidsprijs(), 0.001);
		assertEquals("http://example.com", product.getFotoUrl());
		assertEquals("testLeverancier", product.getLeverancier().getUsername());

		Mockito.verify(leverancierMock).getUsername();
	}

	private static Stream<Arguments> validProductData() {
		return Stream.of(Arguments.of("Laptop", "http://example.com/laptop.png", 10, 1000.0),
				Arguments.of("Gsm", "http://example.com/gsm.png", 5, 800.0),
				Arguments.of("TopGsm", "http://example.com/gsm.png", 1, 100000.0),
				Arguments.of("NietTopGsm", "http://example.com/gsmNietTop.png", 22, 0.5),
				Arguments.of("Toestenbord", "http://example.com/toestenbrd.png", 15, 50.0),
				Arguments.of("Toestenbord2", "http://example.com/toestenbrd.png", 1, 1.0)

		);
	}

	@ParameterizedTest
	@MethodSource("validProductData")
	public void testValidProductCreation(String name, String url, int inStock, double eenheidsprijs) {
		Product.ProductBuilder builder = new Product.ProductBuilder();

		assertDoesNotThrow(() -> {
			builder.leverancier(leverancierMock).naam(name).inStock(inStock).eenheidsprijs(eenheidsprijs).fotoUrl(url)
					.build();
		});
	}

	private static Stream<Arguments> ongeldigeProducten() {
		return Stream.of(
				Arguments.of("", null, 0, -1.0,
						"Productnaam is vereist, Positieve eenheidsprijs is vereist, Url voor productfoto is vereist"),
				Arguments.of("Laptop", "http://example.com/test.png", 2, -12, "Positieve eenheidsprijs is vereist"),
				Arguments.of("Lamp", "", 3, 45.0, "Url voor productfoto is vereist"),
				Arguments.of("Laptop", "   ", 2, 12, "Url voor productfoto is vereist"),
				Arguments.of("Toetsenbord", "http://example.com/keyboard.png", -2, 20.0,
						"Het aantal in stock kan niet kleiner zijn dan 0 en is verplicht op te geven!"),
				Arguments.of("Stoel", "http://example.com/chair.png", 5, -10.0, "Positieve eenheidsprijs is vereist"),
				Arguments.of("Muis", null, 3, 15.0, "Url voor productfoto is vereist"),
				Arguments.of(null, "http://example.com/test.png", 2, 20.0, "Productnaam is vereist"),
				Arguments.of("Tabel", "http://example.com/table.png", null, 50.0, "Aantal in stock is vereist"),
				Arguments.of("Kabel usbc", "http://example.com/kabel.png", 5, -0.5,
						"Positieve eenheidsprijs is vereist"),
				Arguments.of("Iets dat bijna niets kost", "http://example.com/kast.png", -1, -0.001,
						"Positieve eenheidsprijs is vereist, Het aantal in stock kan niet kleiner zijn dan 0 en is verplicht op te geven!"),
				Arguments.of("Laptop2", "http://example.com/laptop.png", 10, -1.0,
						"Positieve eenheidsprijs is vereist"),
				Arguments.of("   ", "http://example.com/laptop.png", 10, -1.0,
						"Productnaam is vereist, Positieve eenheidsprijs is vereist")

		);
	}

	@ParameterizedTest
	@MethodSource("ongeldigeProducten")
	public void testProductBuilderVerkeerdeGegevens(String name, String url, Integer aantal, double prijs,
			String verwachtBericht) {
		Product.ProductBuilder builder = new Product.ProductBuilder();

		builder.leverancier(null).naam(name).fotoUrl(url).inStock(aantal).eenheidsprijs(prijs);

		ProductInformationRequiredException exception = assertThrows(ProductInformationRequiredException.class, () -> {
			builder.build();
		});

		Set<String> actualValues = new HashSet<>(exception.getInformationRequired().values());
		Set<String> expectedValues = new HashSet<>(List.of(verwachtBericht.split(", ")));

		assertTrue(actualValues.containsAll(expectedValues) && expectedValues.containsAll(actualValues));
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, -2, 0, -10, 11, 12, 55, 500, 300 })
	public void testWerkStockBij_geldig(int change) throws ProductInformationRequiredException {
		Product.ProductBuilder builder = new Product.ProductBuilder();
		builder.inStock(10);
		Product testProduct = builder.build();

		testProduct.werkStockBij(change);

		assertEquals(10 + change, testProduct.getInStock());
	}

	@ParameterizedTest
	@ValueSource(ints = { -11, -12, -55, -500, -300, Integer.MIN_VALUE })
	public void testWerkStockBij_nietGeldig(int change) throws ProductInformationRequiredException {
		Product.ProductBuilder builder = new Product.ProductBuilder();
		builder.inStock(10);
		Product testProduct = builder.build();

		assertThrows(ProductInformationRequiredException.class, () -> testProduct.werkStockBij(change));
		assertEquals(10, testProduct.getInStock());
	}
}