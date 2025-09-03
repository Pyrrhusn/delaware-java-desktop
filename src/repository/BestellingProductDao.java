package repository;

import java.util.List;

import domein.BestellingProduct;

import jakarta.persistence.EntityNotFoundException;

public interface BestellingProductDao extends GenericDao<BestellingProduct>{

	List<BestellingProduct> getProducten(int id) throws EntityNotFoundException;

}