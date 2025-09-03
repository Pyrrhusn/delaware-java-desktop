package domein;

import java.util.Arrays;

public enum BetalingsStatusEnum {
	BETAALD("Betaald"), NIETBETAALD("Niet Betaald");

	private final String status;

	BetalingsStatusEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static BetalingsStatusEnum fromString(String text) {
		return Arrays.stream(BetalingsStatusEnum.values()).filter(b -> b.status.equalsIgnoreCase(text)).findFirst()
				.orElse(null);
	}

}