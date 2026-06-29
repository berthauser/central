package com.visus.central.infraestructure.converter;

import java.text.Normalizer;

import com.visus.central.domain.model.Provincia;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProvinciaConverter implements AttributeConverter<Provincia, String> {

	private static String normalize(String s) {
		if (s == null) return null;
		return Normalizer.normalize(s, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "");
	}

	@Override
	public String convertToDatabaseColumn(Provincia attribute) {
		if (attribute == null) return null;
		return attribute.name();
	}

	@Override
	public Provincia convertToEntityAttribute(String dbData) {
		if (dbData == null) return null;

		try {
			return Provincia.valueOf(dbData);
		} catch (IllegalArgumentException e) {
			String withUnderscores = dbData.replace(' ', '_');
			try {
				return Provincia.valueOf(withUnderscores);
			} catch (IllegalArgumentException e2) {
				String normInput = normalize(dbData);
				String normUnderscores = normalize(withUnderscores);
				for (Provincia p : Provincia.values()) {
					if (p.getLabel().equalsIgnoreCase(dbData) || p.getLabel().equalsIgnoreCase(dbData.replace('_', ' '))) {
						return p;
					}
					String normName = normalize(p.name());
					if (normName.equalsIgnoreCase(normInput) || normName.equalsIgnoreCase(normUnderscores)) {
						return p;
					}
				}
				throw new IllegalArgumentException("Valor inválido para Provincia: " + dbData);
			}
		}
	}
}
