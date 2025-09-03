package repository;

import java.util.List;

import domein.Bedrijf;
import domein.ILeverancier;
import domein.Product;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

public class ProductDaoJpa extends GenericDaoJpa<Product> implements ProductDao{
	public ProductDaoJpa() {
		super(Product.class);
	}
	
	@Override
	public List<Product> getProducten(int id) throws EntityNotFoundException{
		try {
			return em.createNamedQuery("Product.lijstBijBestelling", Product.class).setParameter("id", id).getResultList();
		}catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
	}
	
	@Override
	public void updateProduct(Product p) {
		try {
			GenericDaoJpa.startTransaction();
			update(p);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			GenericDaoJpa.rollbackTransaction();
			throw new RuntimeException("fout bij udpaten bedrijf");
		}
	}

	@Override
	public List<Product> getProductenLeverancier(ILeverancier l) throws EntityNotFoundException{
		try {
			return em.createNamedQuery("Product.getPerLeverancier", Product.class).setParameter("gebruikernaam", l.getUsername()).getResultList();
		}catch (NoResultException ex) {
			throw new EntityNotFoundException();
		}
		
	}

	EntityTransaction trans = null;
	@Override
	public void voegProductToe(Product p) {
		try {
			
			trans = em.getTransaction();
			trans.begin();
			insert(p);
			trans.commit();
		} catch (Exception ex) {
			if (trans != null && trans.isActive()) {
				trans.rollback();
				throw new RuntimeException();
			}
				
		    
	    }

	}
}