package com.visus.central.infraestructure.util;

import java.util.concurrent.atomic.AtomicLong;

public final class Ean13Generator {

	private static final AtomicLong counter = new AtomicLong(
			(System.currentTimeMillis() % 1000000000L) * 1000L);

	private Ean13Generator() {
	}

	public static String generar() {
		long seq = counter.incrementAndGet() % 1000000000L;
		String base = String.format("779%09d", seq);
		int check = calcularDigitoControl(base);
		return base + check;
	}

	public static boolean esValido(String ean13) {
		if (ean13 == null || ean13.length() != 13 || !ean13.matches("\\d{13}")) {
			return false;
		}
		int check = calcularDigitoControl(ean13.substring(0, 12));
		return check == (ean13.charAt(12) - '0');
	}

	static int calcularDigitoControl(String doceDigitos) {
		int sumaPar = 0;
		int sumaImpar = 0;
		for (int i = 0; i < 12; i++) {
			int d = doceDigitos.charAt(i) - '0';
			if (i % 2 == 0) {
				sumaImpar += d;
			} else {
				sumaPar += d;
			}
		}
		int total = sumaImpar + sumaPar * 3;
		int resto = total % 10;
		return resto == 0 ? 0 : 10 - resto;
	}
}
