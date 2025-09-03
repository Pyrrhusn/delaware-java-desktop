package domein;

import javafx.collections.ObservableList;

/**
 * Interface voor User
 */
public interface IUser {

	public IBedrijf getBedrijf();

	public String getPassword();

	public String getUsername();

	public ObservableList<IBestelling> getBestellingen();
}