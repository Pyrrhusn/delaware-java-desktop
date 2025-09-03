package domein;

/**
 * Interface voor Klant
 */
public interface IKlant {

	public void setBedrijf(Bedrijf bedrijf);

	public String getUsername();

	public String getPassword();

	public IBedrijf getBedrijf();
}