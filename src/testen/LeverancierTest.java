package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.Bestelling;
import domein.IProduct;
import domein.Klant;
import domein.Leverancier;
import domein.Product;
import javafx.collections.ObservableList;

@ExtendWith(MockitoExtension.class)
class LeverancierTest {

	@Mock
	private List<Klant> mockKlanten;

	@Mock
	private List<IProduct> mockProducten;

	private Leverancier leverancier;

	@Mock
	private Product mockProduct1;

	@Mock
	private Product mockProduct2;

	@BeforeEach
	void setUp() {
		leverancier = new Leverancier("gebruikersnaam", "wachtwoord");
	}

	@Test
	void testGetAantalKlanten() {
		when(mockKlanten.size()).thenReturn(5);
		leverancier.setKlanten(mockKlanten);

		assertEquals(5, leverancier.getAantalKlanten());
	}

	@Test
	void testGetProducten_EmptyList() {
		ObservableList<IProduct> returnedProducts = leverancier.getProducten();

		assertTrue(returnedProducts.isEmpty(), "List of products should be empty");
	}

	@Test
	void testGetProducten() {

		Product mockProduct1 = mock(Product.class);
		Product mockProduct2 = mock(Product.class);

		List<Product> products = new ArrayList<>();
		products.add(mockProduct1);
		products.add(mockProduct2);

		leverancier.setProducten(products);

		ObservableList<IProduct> returnedProducts = leverancier.getProducten();

		assertFalse(returnedProducts.isEmpty(), "List of products should not be empty");

		assertEquals(products.size(), returnedProducts.size(), "Size of the returned list is incorrect");

	}

	@Test
	void testSetProducten() {
		List<Product> products = new ArrayList<>();
		Product mockProduct1 = mock(Product.class);
		Product mockProduct2 = mock(Product.class);
		products.add(mockProduct1);
		products.add(mockProduct2);

		leverancier.setProducten(products);

		assertEquals(2, leverancier.getProducten().size(),
				"Number of products should match the size of the added list");
	}

	@Test
	void testSetBestellingen() {

		List<Bestelling> mockBestellingen = new ArrayList<>();
		Bestelling mockBestelling1 = mock(Bestelling.class);
		Bestelling mockBestelling2 = mock(Bestelling.class);
		mockBestellingen.add(mockBestelling1);
		mockBestellingen.add(mockBestelling2);

		leverancier.setBestellingen(mockBestellingen);

		assertEquals(2, leverancier.getBestellingen().size(),
				"Number of orders should match the size of the added list");
	}

}
