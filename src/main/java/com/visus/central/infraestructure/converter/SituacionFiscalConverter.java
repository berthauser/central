package com.visus.central.infraestructure.converter;

import com.visus.central.domain.model.SituacionFiscal;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SituacionFiscalConverter implements AttributeConverter<SituacionFiscal, String> {

	@Override
	public String convertToDatabaseColumn(SituacionFiscal attribute) {
		if (attribute == null) return null;
		return attribute.name();
	}

	@Override
	public SituacionFiscal convertToEntityAttribute(String dbData) {
		if (dbData == null) return null;

		// Try exact match by name (underscore format: "Consumidor_Final")
		try {
			return SituacionFiscal.valueOf(dbData);
		} catch (IllegalArgumentException e) {
			// Fallback: try to match by replacing spaces with underscores
			String withUnderscores = dbData.replace(' ', '_');
			try {
				return SituacionFiscal.valueOf(withUnderscores);
			} catch (IllegalArgumentException e2) {
				// Fallback: try to match by replacing underscores with spaces (label format)
				for (SituacionFiscal s : SituacionFiscal.values()) {
					if (s.getLabel().equalsIgnoreCase(dbData) || s.getLabel().equalsIgnoreCase(dbData.replace('_', ' '))) {
						return s;
					}
				}
				throw new IllegalArgumentException("Valor inválido para SituacionFiscal: " + dbData);
			}
		}
	}
}
