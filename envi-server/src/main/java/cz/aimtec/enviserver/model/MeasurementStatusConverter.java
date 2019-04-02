package cz.aimtec.enviserver.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MeasurementStatusConverter implements AttributeConverter<MeasurementStatus, String> {

	@Override
	public String convertToDatabaseColumn(MeasurementStatus attribute) {
		return attribute.name().toLowerCase();
	}

	@Override
	public MeasurementStatus convertToEntityAttribute(String dbData) {
		return MeasurementStatus.valueOf(dbData.toUpperCase());		
	}
}