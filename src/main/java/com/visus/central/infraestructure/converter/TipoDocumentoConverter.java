package com.visus.central.infraestructure.converter;

import com.visus.central.domain.model.TipoDocumento;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoDocumentoConverter implements AttributeConverter<TipoDocumento, String> {

	@Override
	public String convertToDatabaseColumn(TipoDocumento attribute) {
		if (attribute == null) return null;
		return attribute.name();
	}

	@Override
	public TipoDocumento convertToEntityAttribute(String dbData) {
		if (dbData == null) return null;

		try {
			return TipoDocumento.valueOf(dbData);
		} catch (IllegalArgumentException e) {
			String withUnderscores = dbData.replace(' ', '_');
			try {
				return TipoDocumento.valueOf(withUnderscores);
			} catch (IllegalArgumentException e2) {
				for (TipoDocumento t : TipoDocumento.values()) {
					if (t.getLabel().equalsIgnoreCase(dbData) || t.getLabel().equalsIgnoreCase(dbData.replace('_', ' '))) {
						return t;
					}
				}
				throw new IllegalArgumentException("Valor inválido para TipoDocumento: " + dbData);
			}
		}
	}
}
