package repository;

import java.util.List;

import domein.BestellingProduct;
import domein.Product;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

public class BestellingProductDaoJpa extends GenericDaoJpa<BestellingProduct> implements BestellingProductDao{
	public BestellingProductDaoJpa() {
		super(BestellingProduct.class);
	}
	
	@Override
	public List<BestellingProduct> getProducten(int id) throws EntityNotFoundException{
		try {
			return em.createNamedQuery("Product.lijstBijBestelling", BestellingProduct.class).setParameter("id", id).getResultList();
		}catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
}