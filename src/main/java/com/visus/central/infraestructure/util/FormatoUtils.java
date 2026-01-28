package com.visus.central.infraestructure.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utilidades para formateo de valores monetarios y porcentajes
 */
public class FormatoUtils {

	private static final Locale LOCALE_AR = Locale.forLanguageTag("es-AR");

	// Formateador para pesos argentinos
	private static final DecimalFormat FORMATO_PESOS;

	// Formateador para porcentajes
	private static final DecimalFormat FORMATO_PORCENTAJE;

	static {
		// Configurar símbolos para Argentina
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols(LOCALE_AR);
		simbolos.setDecimalSeparator(',');
		simbolos.setGroupingSeparator('.');
		simbolos.setCurrencySymbol("$");

		// Inicializar formateador de pesos
		FORMATO_PESOS = new DecimalFormat("$ #,##0.00", simbolos);
		FORMATO_PESOS.setDecimalFormatSymbols(simbolos);

		// Inicializar formateador de porcentajes
		FORMATO_PORCENTAJE = new DecimalFormat("#,##0.00 %", simbolos);
	}

	/**
     * Constructor privado para prevenir instanciación.
     * Esta es una clase utilitaria con solo métodos estáticos.
     */
	private FormatoUtils() {
		// Clase utilitaria, no instanciable
	}


	/**
	 * Formatea un valor BigDecimal a pesos argentinos.
	 * Ejemplo: 1234567.89 -> "$ 1.234.567,89"
	 * 
	 * @param valor Valor a formatear
	 * @return String formateado o cadena vacía si el valor es null
	 */
	public static String formatPesos(BigDecimal valor) {
		if (valor == null) {
			return "";
		}
		return FORMATO_PESOS.format(valor);
	}

	/**
	 * Formatea un valor BigDecimal a pesos argentinos, 
	 * permitiendo especificar si mostrar símbolo de moneda.
	 * 
	 * @param valor Valor a formatear
	 * @param conSimbolo true para incluir "$", false para solo el número
	 * @return String formateado o cadena vacía si el valor es null
	 */

	public static String formatPesos(BigDecimal valor, boolean conSimbolo) {
		if (valor == null) {
			return "";
		}

		if (conSimbolo) {
			return FORMATO_PESOS.format(valor);
		} else {
			// Usar patrón sin símbolo de moneda
			DecimalFormatSymbols simbolos = new DecimalFormatSymbols(LOCALE_AR);
			simbolos.setDecimalSeparator(',');
			simbolos.setGroupingSeparator('.');

			DecimalFormat df = new DecimalFormat("#,##0.00", simbolos);
			return df.format(valor);
		}
	}

	/**
	 * Formatea un valor BigDecimal como porcentaje.
	 * Ejemplo: 0.215 -> "21,50 %"
	 * 
	 * @param valor Valor decimal a formatear (ej: 0.215 para 21.5%)
	 * @return String formateado o cadena vacía si el valor es null
	 */
	public static String formatPorcentaje(BigDecimal valor) {
		if (valor == null) {
			return "";
		}
		return FORMATO_PORCENTAJE.format(valor);
	}

	/**
	 * Formatea un valor BigDecimal como porcentaje.
	 * Ejemplo: 21.5 -> "21,50 %"
	 * 
	 * @param valor Valor entero/decimal a formatear (ej: 21.5 para 21.5%)
	 * @param esDecimal true si el valor ya está en decimal (0.215), false si está en entero (21.5)
	 * @return String formateado o cadena vacía si el valor es null
	 */
	public static String formatPorcentaje(BigDecimal valor, boolean esDecimal) {
		if (valor == null) {
			return "";
		}

		if (esDecimal) {
			return FORMATO_PORCENTAJE.format(valor);
		} else {
			// Si viene como entero (21.5), convertir a decimal (0.215)
			return FORMATO_PORCENTAJE.format(valor.divide(new BigDecimal("100")));
		}
	}

	/**
	 * Formatea un gravamen (similar a porcentaje pero con lógica de "No Aplica").
	 * 
	 * @param gravamen Valor del gravamen
	 * @return "No Aplica" si es null o cero, o el valor formateado como porcentaje
	 */
	public static String formatGravamen(BigDecimal gravamen) {
		if (gravamen == null || gravamen.compareTo(BigDecimal.ZERO) == 0) {
			return "No Aplica";
		}

		// Asumimos que el gravamen viene como entero/decimal (ej: 21.5)
		return formatPorcentaje(gravamen, false); // false = viene como entero/decimal, no como decimal
	}

	/**
	 * Formatea un número con separadores de miles.
	 * Ejemplo: 1234567 -> "1.234.567"
	 * 
	 * @param numero Número a formatear
	 * @return String formateado o cadena vacía si el valor es null
	 */
	public static String formatNumero(Number numero) {
		if (numero == null) {
			return "";
		}

		DecimalFormatSymbols simbolos = new DecimalFormatSymbols(LOCALE_AR);
		simbolos.setGroupingSeparator('.');

		DecimalFormat df = new DecimalFormat("#,##0", simbolos);
		return df.format(numero);
	}

	/**
	 * Formatea un número con decimales y separadores de miles.
	 * Ejemplo: 1234567.89 -> "1.234.567,89"
	 * 
	 * @param numero Número a formatear
	 * @param decimales Cantidad de decimales a mostrar (0-6)
	 * @return String formateado o cadena vacía si el valor es null
	 */
	public static String formatNumero(Number numero, int decimales) {
		if (numero == null) {
			return "";
		}

		if (decimales < 0 || decimales > 6) {
			decimales = 2; // Valor por defecto
		}

		DecimalFormatSymbols simbolos = new DecimalFormatSymbols(LOCALE_AR);
		simbolos.setDecimalSeparator(',');
		simbolos.setGroupingSeparator('.');

		// Construir patrón dinámico
		StringBuilder patron = new StringBuilder("#,##0");
		if (decimales > 0) {
			patron.append(".");
			for (int i = 0; i < decimales; i++) {
				patron.append("0");
			}
		}

		DecimalFormat df = new DecimalFormat(patron.toString(), simbolos);
		return df.format(numero);
	}
	
	/**
	 * Calcula el precio de venta aplicando margen y gravamen.
	 * 
	 * @param precioCosto Precio base
	 * @param margen Margen de utilidad (porcentaje entero, ej: 65,29)
	 * @param gravamen Gravamen/IVA (porcentaje entero, ej: 21)
	 * @return Precio final de venta
	 */
	public static BigDecimal calcularPrecioVenta(BigDecimal precioCosto, BigDecimal margen, BigDecimal gravamen) {
	    if (precioCosto == null) {
	        return null;
	    }
	    
	    BigDecimal precioFinal = precioCosto;
	    
	    // Aplicar margen
	    if (margen != null && margen.compareTo(BigDecimal.ZERO) > 0) {
	        BigDecimal factorMargen = BigDecimal.ONE.add(
	            margen.divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP)
	        );
	        precioFinal = precioFinal.multiply(factorMargen);
	    }
	    
	    // Aplicar gravamen
	    if (gravamen != null && gravamen.compareTo(BigDecimal.ZERO) > 0) {
	        BigDecimal factorGravamen = BigDecimal.ONE.add(
	            gravamen.divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP)
	        );
	        precioFinal = precioFinal.multiply(factorGravamen);
	    }
	    
	    return precioFinal.setScale(2, RoundingMode.HALF_UP);
	}

}
