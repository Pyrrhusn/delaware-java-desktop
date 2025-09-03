package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import domein.ILeverancier;
import domein.IProduct;
import domein.Product;
import domein.ProductBeheerder;
import javafx.collections.ObservableList;
import repository.ProductDao;

@ExtendWith(MockitoExtension.class)
public class ProductBeheerderTest {

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductBeheerder productBeheerder;


	@Test
	public void testUpdateProduct() {

		IProduct product = mock(Product.class);

		productBeheerder.updateProduct(product);

		verify(productDao).updateProduct((Product) product);
	}

	@Test
	public void testGetProductenLeverancier() {

		ILeverancier leverancier = mock(ILeverancier.class);
		List<IProduct> products = Arrays.asList(mock(IProduct.class), mock(IProduct.class));

		when(productDao.getProductenLeverancier(leverancier)).thenReturn(products);

		ObservableList<IProduct> result = productBeheerder.getProductenLeverancier(leverancier);

		assertEquals(products, result);
	}

	@Test
	public void testVoegProductToe() {

		IProduct product = mock(Product.class);

		productBeheerder.voegProductToe(product);

		verify(productDao).voegProductToe((Product) product);
	}
}
