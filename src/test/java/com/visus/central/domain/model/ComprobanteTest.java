package com.visus.central.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas del modelo Comprobante")
public class ComprobanteTest {

	@Test
	@DisplayName("Crear comprobante con datos válidos")
	void testCrearComprobanteValido() {
		Comprobante comp = new Comprobante();
		comp.setId(1);
		comp.setNombreLargo("REMITO");
		comp.setNumeroFinal(100);

		assertAll(() -> assertEquals(1, comp.getId()), () -> assertEquals("REMITO", comp.getNombreLargo()),
				() -> assertEquals(100, comp.getNumeroFinal()));
	}

	@Test
	@DisplayName("Lanzar excepción cuando el número final es negativo")
	void testNumeroFinalNegativoLanzaExcepcion() {
		Comprobante comp = new Comprobante();
		assertThrows(IllegalArgumentException.class, () -> comp.setNumeroFinal(-5));
	}

	@Test
	@DisplayName("Calcular próximo número de comprobante")
	void testProximoNumero() {
		Comprobante comp = new Comprobante();
		comp.setNumeroFinal(100);
		int proximo = comp.getNumeroFinal() + 1;
		assertEquals(101, proximo);
	}

}
