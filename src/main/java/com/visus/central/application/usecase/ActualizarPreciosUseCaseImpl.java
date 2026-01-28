package com.visus.central.application.usecase;

/* Problema Original:

Error "Could not initialize proxy [JpaRubroEntity#22] - no session" al buscar artículo por código de barras. 
Este error ocurre debido a:
Lazy Loading fuera de sesión: El método findByCodigoBarra() devuelve entidades con relaciones LAZY
Acceso indirecto a relaciones: Aunque solo necesitamos descripción y precioCosto, el mapper u otras partes del código acceden a linea.rubro
Sesión cerrada: La sesión de Hibernate se cierra antes de inicializar los proxies LAZY

Justificación del Diseño:
Por qué no modificar el mapper general:
El mapper JpaArticuloMapper es usado por múltiples componentes
Cambiarlo afectaría a toda la aplicación (pantallas de listado, edición, etc.)
Solo este caso de uso tiene problemas específicos de Lazy Loading

Por qué no cambiar fetch type a EAGER:
Impactaría el rendimiento en otras partes del sistema
Se cargarían relaciones innecesarias en consultas que no las requieren
Violaría el principio de carga perezosa óptima

Por qué no habilitar Open Session in View:
Considerado anti-patrón en aplicaciones de alta concurrencia
Mantiene conexiones de base de datos abiertas más tiempo del necesario
Puede causar memory leaks y problemas de rendimiento

Consideraciones Futuras:
DTOs para operaciones específicas: Para evitar estos problemas, considerar crear DTOs especializados
Consultas con JOIN FETCH: Para casos que requieran relaciones, usar consultas explícitas con JOIN FETCH
Patrón CQRS: Separar consultas (que necesitan relaciones) de comandos (que solo actualizan datos)

*/

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.port.in.ActualizarPreciosUseCase;
import com.visus.central.domain.port.out.ArticuloRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ActualizarPreciosUseCaseImpl implements ActualizarPreciosUseCase {
	
	private final ArticuloRepository articuloRepository;

    public ActualizarPreciosUseCaseImpl(ArticuloRepository articuloRepository) {
        this.articuloRepository = articuloRepository;
    }
    
    @Override
    @Transactional
    public int actualizarPreciosPorRubroYLineas(Integer rubroId, List<Integer> lineasIds, BigDecimal porcentaje) {
        
    	System.out.println("=== INICIO ACTUALIZACIÓN DE PRECIOS ===");
        System.out.println("Rubro ID: " + rubroId);
        System.out.println("Líneas IDs: " + lineasIds);
        System.out.println("Porcentaje: " + porcentaje);
        
        // Verificar que el repositorio no sea null
        if (articuloRepository == null) {
            System.out.println("ERROR: articuloRepository es NULL!");
            return 0;
        }
     // Obtener los artículos
        List<Articulo> articulos = articuloRepository.findByRubroIdAndLineaIdIn(rubroId, lineasIds);
        
        System.out.println("Artículos encontrados: " + articulos.size());
        
        if (articulos.isEmpty()) {
            System.out.println("ADVERTENCIA: No se encontraron artículos con los criterios especificados");
            System.out.println("Rubro ID: " + rubroId);
            System.out.println("Líneas IDs: " + lineasIds);
            return 0;
        }
        
     // Mostrar información de los primeros 5 artículos
        for (int i = 0; i < Math.min(5, articulos.size()); i++) {
            Articulo a = articulos.get(i);
            System.out.println("Artículo " + (i+1) + ": ID=" + a.getId() + 
                             ", Descripción=" + a.getDescripcion() +
                             ", Precio Actual=" + a.getPrecioCosto() +
                             ", Fecha Actualización Actual=" + a.getFechaActualizPrecios() +
                             ", Línea ID=" + (a.getLinea() != null ? a.getLinea().getIdLinea() : "null") +
                             ", Rubro ID=" + (a.getLinea() != null && a.getLinea().getRubro() != null ? a.getLinea().getRubro().getId() : "null"));
        }
        
        // Calcular el factor de actualización (1 + porcentaje/100)
        BigDecimal factor = BigDecimal.ONE.add(
            porcentaje.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
        );
        
        System.out.println("Factor de actualización: " + factor);
        
     // Obtener fecha actual
        LocalDate fechaActualizacion = LocalDate.now();
        System.out.println("Fecha de actualización que se usará: " + fechaActualizacion);
        
     // Actualizar cada artículo
        int contador = 0;
        for (Articulo articulo : articulos) {
            BigDecimal precioCosto = articulo.getPrecioCosto();
            if (precioCosto != null) {
                BigDecimal nuevoPrecio = precioCosto.multiply(factor);
                nuevoPrecio = nuevoPrecio.setScale(2, RoundingMode.HALF_UP);
                
                System.out.println("Actualizando artículo ID=" + articulo.getId() + 
                                 " | Precio anterior: " + precioCosto + 
                                 " | Precio nuevo: " + nuevoPrecio +
                                 " | Fecha nueva: " + fechaActualizacion);
                
                // Imprimir fecha anterior para comparar
                System.out.println("Fecha anterior de actualización: " + articulo.getFechaActualizPrecios());
                
                articulo.setPrecioCosto(nuevoPrecio);
                articulo.setFechaActualizPrecios(fechaActualizacion);
                
                try {
                    Articulo articuloGuardado = articuloRepository.save(articulo);
                    System.out.println("DEBUG: Artículo guardado - ID=" + articuloGuardado.getId() + 
                                     " | Fecha guardada: " + articuloGuardado.getFechaActualizPrecios());
                    contador++;
                } catch (Exception e) {
                    System.out.println("ERROR al guardar artículo ID=" + articulo.getId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        System.out.println("=== FIN ACTUALIZACIÓN ===");
        System.out.println("Total artículos actualizados: " + contador);
        
        return contador;
    }

    @Override
    public int actualizarPrecioPorCodigoBarra(String codigoBarra, BigDecimal porcentaje) {

    	System.out.println("=== INICIO ACTUALIZACIÓN POR CÓDIGO DE BARRAS ===");
    	System.out.println("Código de barras: " + codigoBarra);
    	System.out.println("Porcentaje: " + porcentaje);

    	// Buscar el artículo por código de barras
    	Optional<Articulo> articuloOpt = articuloRepository.findByCodigoBarra(codigoBarra);

    	if (articuloOpt.isEmpty()) {
    		System.out.println("ERROR: No se encontró artículo con código de barras: " + codigoBarra);
    		return 0;
    	}

    	Articulo articulo = articuloOpt.get();
    	
    	
    	System.out.println("Artículo encontrado: ID=" + articulo.getId() + ", Descripción=" + articulo.getDescripcion()
    	+ ", Precio Actual=" + articulo.getPrecioCosto());

    	// Calcular el factor de actualización
    	BigDecimal factor = BigDecimal.ONE.add(porcentaje.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
    	System.out.println("Factor de actualización: " + factor);

    	BigDecimal precioCosto = articulo.getPrecioCosto();
    	if (precioCosto == null) {
    		System.out.println("ERROR: El artículo no tiene precio de costo");
    		return 0;
    	}

    	BigDecimal nuevoPrecio = precioCosto.multiply(factor);
    	nuevoPrecio = nuevoPrecio.setScale(2, RoundingMode.HALF_UP);

    	System.out.println("Precio nuevo: " + nuevoPrecio);

    	// Actualizar el artículo
    	articulo.setPrecioCosto(nuevoPrecio);
    	articulo.setFechaActualizPrecios(LocalDate.now());
    	articuloRepository.save(articulo);

    	System.out.println("=== FIN ACTUALIZACIÓN ===");
    	return 1;
    }
    
}
