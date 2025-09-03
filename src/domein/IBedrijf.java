package domein;

import java.util.Map;

import javafx.collections.ObservableList;

/**
 * Interface voor bedrijf
 */
public interface IBedrijf {
	public String getNaam();

	public void setNaam(String naam);

	public String getSector();

	public void setSector(String sector);

	public String getAdres();

	public void setAdres(String adres);

	public Map<String, String> getContactGegevens();

	public void setContactGegevens(Map<String, String> contactGegevens);

	public String getLogoUrl();

	public void setLogoUrl(String logoUrl);

	public String getBTWNummer();

	public ILeverancier getLeverancier();

	public IKlant getKlant();

	public ObservableList<BetalingsMethodesEnum> getBetalingsOpties();

	public boolean isActiefKlant();

	public boolean isActiefLeverancier();

	public void setActiefLeverancier(boolean b);

	public void setActiefKlant(boolean b);

}