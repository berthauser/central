package com.visus.central.infraestructure.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.visus.central.infraestructure.persistence.entity.JpaDepartamentoEntity.Provincia;

public class ProvinciaUtils {
	public static List<String> obtenerProvinciasLegibles() {
		return Arrays.stream(Provincia.values())
                .map(Provincia::getLabel)
                .collect(Collectors.toList());
    }
}