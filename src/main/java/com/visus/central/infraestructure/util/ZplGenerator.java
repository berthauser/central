package com.visus.central.infraestructure.util;

public final class ZplGenerator {

	private ZplGenerator() {
	}

	public static String generarEtiqueta(String ean13, String lineaLibre, String codigoInterno,
			String descripcion, String medida, String presentacion, Integer stock,
			int labelWidthDots, int labelHeightDots) {
		StringBuilder zpl = new StringBuilder();
		zpl.append("^XA\n");
		zpl.append("^LL").append(labelHeightDots).append("\n");
		zpl.append("^PW").append(labelWidthDots).append("\n");
		zpl.append("^LS0\n");
		zpl.append("^BY2,3,70\n");

		int x = 30;
		int y = 20;

		zpl.append("^FO").append(x).append(",").append(y).append("^BEN,70,Y,N^FD").append(ean13).append("^FS\n");

		y += 80;
		zpl.append("^FO").append(x).append(",").append(y).append("^A0N,22,22^FD").append(escape(descripcion)).append("^FS\n");

		y += 28;
		String info = "";
		if (presentacion != null && !presentacion.isBlank()) {
			info += presentacion;
		}
		if (medida != null && !medida.isBlank()) {
			if (!info.isEmpty()) info += " - ";
			info += medida;
		}
		if (codigoInterno != null && !codigoInterno.isBlank()) {
			if (!info.isEmpty()) info += " | ";
			info += codigoInterno;
		}
		zpl.append("^FO").append(x).append(",").append(y).append("^A0N,16,16^FD").append(escape(info)).append("^FS\n");

		y += 22;
		String footer = "";
		if (lineaLibre != null && !lineaLibre.isBlank()) {
			footer += lineaLibre;
		}
		if (stock != null) {
			if (!footer.isEmpty()) footer += " | ";
			footer += "Stock: " + stock;
		}
		zpl.append("^FO").append(x).append(",").append(y).append("^A0N,14,14^FD").append(escape(footer)).append("^FS\n");

		zpl.append("^XZ\n");
		return zpl.toString();
	}

	public static String generarEtiqueta(String ean13, String descripcion,
			String presentacion, String medida, Integer stock) {
		return generarEtiqueta(ean13, null, null, descripcion, medida, presentacion, stock, 800, 300);
	}

	public static String generarEtiquetaCabezal() {
		return "";
	}

	private static String escape(String value) {
		if (value == null) return "";
		return value
				.replace("\\", "\\\\")
				.replace("^", "^^")
				.replace("~", "~~")
				.replace("\"", "");
	}
}
