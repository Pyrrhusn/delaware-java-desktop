package domein;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ListEnumToStringConverter implements AttributeConverter<List<BetalingsMethodesEnum>, String> {

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<BetalingsMethodesEnum> enumList) {
		try {
			return mapper.writeValueAsString(enumList);
		} catch (JsonProcessingException e) {
			return "";
		}
	}

	@Override
	public List<BetalingsMethodesEnum> convertToEntityAttribute(String enumString) {
		try {
			return mapper.readValue(enumString,
					mapper.getTypeFactory().constructCollectionType(List.class, BetalingsMethodesEnum.class));
		} catch (IOException e) {
			return new ArrayList<>();
		}
	}

}
