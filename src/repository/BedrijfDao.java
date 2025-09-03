package repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import domein.Bedrijf;
import jakarta.persistence.EntityNotFoundException;

public interface BedrijfDao extends GenericDao<Bedrijf>{

	Bedrijf getBedrijf(String btwNummer);
	List<Bedrijf> getBedrijvenMetAanVragenVoorWijzingen();

	void voegBedrijfToe(Bedrijf b);

	void updateBedrijf(Bedrijf b);

	

}