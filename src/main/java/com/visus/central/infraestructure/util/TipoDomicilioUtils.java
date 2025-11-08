package com.visus.central.infraestructure.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.visus.central.infraestructure.persistence.entity.JpaDomicilioEntity.TipoDomicilio;

public class TipoDomicilioUtils {
	public static List<String> obtenerTiposDomicilioLegibles() {
		return Arrays.stream(TipoDomicilio.values())
                .map(TipoDomicilio::getLabel)
                .collect(Collectors.toList());
    }

}
