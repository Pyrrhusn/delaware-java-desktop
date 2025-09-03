package repository;

import java.util.List;

import domein.ILeverancier;
import domein.Product;
import jakarta.persistence.EntityNotFoundException;

public interface ProductDao extends GenericDao<Product>{

	List<Product> getProducten(int id) throws EntityNotFoundException;

	void updateProduct(Product p);

	List getProductenLeverancier(ILeverancier l) throws EntityNotFoundException;

	void voegProductToe(Product p);

}