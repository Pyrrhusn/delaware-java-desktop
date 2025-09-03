package domein;

import java.util.Arrays;

public enum LeveringStatusEnum {

	VERZONDEN("Verzonden"), GELEVERD("Geleverd"), INBEHANDELING("In behandeling");

	private final String status;

	LeveringStatusEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public static LeveringStatusEnum fromString(String text) {
		return Arrays.stream(LeveringStatusEnum.values()).filter(b -> b.status.equalsIgnoreCase(text)).findFirst()
				.orElse(null);
	}
}