package domein;

import java.util.Arrays;

public enum BetalingsMethodesEnum {
	PAYPAL("PayPal"), BANCONTACT("Bancontact"), VISA("Visa"), MAESTRO("Maestro"), OVERSCHRIJVING("Overschrijving"),
	FACTUUR("Factuur");

	private final String methode;

	BetalingsMethodesEnum(String methode) {
		this.methode = methode;
	}

	public String getStatus() {
		return methode;
	}

	public static BetalingsMethodesEnum fromString(String text) {
		return Arrays.stream(BetalingsMethodesEnum.values()).filter(b -> b.methode.equalsIgnoreCase(text)).findFirst()
				.orElse(null);
	}
}