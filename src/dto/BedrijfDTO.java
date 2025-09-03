package dto;

import java.util.List;

import domein.BetalingsMethodesEnum;

public record BedrijfDTO(String logo, String name, String sector, String address, String btwNumber,
		String contactPersoon, String phoneNumber, String email, List<BetalingsMethodesEnum> betalingsmethodes) {
}
