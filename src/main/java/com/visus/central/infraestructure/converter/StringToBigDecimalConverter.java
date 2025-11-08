package com.visus.central.infraestructure.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

	private static final long serialVersionUID = 1L;
	
	private final String errorMessage;

    public StringToBigDecimalConverter(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Result<BigDecimal> convertToModel(String value, ValueContext context) {
        if (value == null || value.trim().isEmpty()) {
            return Result.ok(null);
        }
        try {
            // Reemplazar coma por punto (soporte regional)
            String normalized = value.replace(",", ".").trim();
            return Result.ok(new BigDecimal(normalized));
        } catch (NumberFormatException e) {
            return Result.error(errorMessage);
        }
    }

    @Override
    public String convertToPresentation(BigDecimal value, ValueContext context) {
        if (value == null) {
            return "";
        }
        // Formateo regional amigable
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        DecimalFormat df = new DecimalFormat("#,##0.##", symbols);
        return df.format(value);
    }
}

