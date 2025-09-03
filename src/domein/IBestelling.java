package domein;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface voor Bestelling
 */
public interface IBestelling {
	public Klant getKlant();

	public Leverancier getLeverancier();

	public String getOrderID();

	public LocalDate getDatumGeplaatst();

	public String getLeveradres();

	public LeveringStatusEnum getOrderstatus();

	public BetalingsStatusEnum isBetalingsstatus();

	public List<IBestellingProduct> getProducten();

	public double berekenBedrag();

	public void wijzigLeveringStatus(String status, LeveringStatusEnum leveringStatusEnum);

	public void wijzigBetalingsstatus(String betalingsstatus, BetalingsStatusEnum betalingsstatus2);

	public void removePropertyChangeListener(PropertyChangeListener listener);

	public void addPropertyChangeListener(PropertyChangeListener listener);

	public LocalDate getDatumLaatsteBetalingsherinnering();

	public void setDatumLaatsteBetalingsherinnering(LocalDate datum);
}