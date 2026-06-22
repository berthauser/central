package com.visus.central.infraestructure.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Clase utilitaria para conversiones entre tipos de fecha de Java 8 (LocalDate,
 * LocalDateTime) y el heredado java.util.Date.
 * 
 * @author eazi
 * @version 1.0
 */

public class DateTimeUtils {

	private DateTimeUtils() {
		// Constructor privado para evitar instanciación
		throw new UnsupportedOperationException("Clase utilitaria - no instanciable");
	}

	/**
	 * Convierte LocalDate a Date (asume medianoche en la zona horaria por defecto)
	 * 
	 * @param localDate Fecha a convertir (puede ser null)
	 * @return Date convertido, o null si localDate es null
	 */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) return null;
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
	
	/**
	 * Convierte LocalDateTime a Date
	 * 
	 * @param localDateTime Fecha/hora a convertir (puede ser null)
	 * @return Date convertido, o null si localDateTime es null
	 */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

	/**
	 * Convierte un Date a LocalDate.
	 * 
	 * @param date Date a convertir (puede ser null)
	 * @return LocalDate convertido, o null si date es null
	 */
	public static LocalDate toLocalDate(Date date) {
		if (date == null) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * Convierte un Date a LocalDateTime.
	 * 
	 * @param date Date a convertir (puede ser null)
	 * @return LocalDateTime convertido, o null si date es null
	 */
	public static LocalDateTime toLocalDateTime(Date date) {
		if (date == null) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

}
