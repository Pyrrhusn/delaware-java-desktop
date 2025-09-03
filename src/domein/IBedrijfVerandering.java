package domein;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Interface voor BedrijfVerandering
 */
public interface IBedrijfVerandering {
	public IUser getAangevraagdDoor();

	public IBedrijf getBedrijf();

	public void setAangevraagdDoor(IUser user);

	public LocalDateTime getAangevraagdOp();

	public void setAangevraagdOp(LocalDateTime tijd);

	public String getSector();

	public void setSector(String sector);

	public String getAdres();

	public void setAdres(String adres);

	public Map<String, String> getContactGegevens();

	public void setContactGegevens(Map<String, String> contactGegevens);

	public String getLogo();

	public void setLogo(String logoUrl);

	public void setAfgekeurd(boolean status);

	public List<BetalingsMethodesEnum> getBetalingsInfo();

	public void setGoedgekeurd(boolean status);

	public void setIBedrijf(IBedrijf bedrijf);

}
