package com.visus.central.infraestructure.converter;

import org.springframework.stereotype.Component;

import com.visus.central.domain.model.Alicuota;
import com.visus.central.domain.model.Articulo;
import com.visus.central.domain.model.Linea;
import com.visus.central.domain.model.Medida;
import com.visus.central.domain.model.Presentacion;
import com.visus.central.domain.model.Proveedor;
import com.visus.central.domain.model.Rubro;
import com.visus.central.infraestructure.persistence.entity.JpaAlicuotaEntity;
import com.visus.central.infraestructure.persistence.entity.JpaArticuloEntity;
import com.visus.central.infraestructure.persistence.entity.JpaLineaEntity;
import com.visus.central.infraestructure.persistence.entity.JpaMedidaEntity;
import com.visus.central.infraestructure.persistence.entity.JpaPresentacionEntity;
import com.visus.central.infraestructure.persistence.entity.JpaProveedorEntity;
import com.visus.central.infraestructure.persistence.entity.JpaRubroEntity;


@Component
public class JpaArticuloMapper {
	
	public Articulo toModel(JpaArticuloEntity entity) {
        
		if (entity == null) return null;

		Articulo model = new Articulo();
        model.setId(entity.getId());
        model.setCodigo_interno(entity.getCodigoInterno());
        model.setCodigo_barra(entity.getCodigoBarra());
        model.setDescripcion(entity.getDescripcion());
        model.setNrolote(entity.getNumeroLote());
        model.setStock(entity.getStock());
        model.setStock_minimo(entity.getStock_minimo());
        model.setStock_maximo(entity.getStock_maximo());
        
        if (entity.getLinea() != null) {
            Linea linea = new Linea();
            linea.setIdLinea(entity.getLinea().getIdLinea());
            linea.setDescripcion(entity.getLinea().getDescripcion());
            
            // Mapear el rubro de la línea (si existe)
            if (entity.getLinea().getRubro() != null) {
                Rubro rubro = new Rubro();
                rubro.setId(entity.getLinea().getRubro().getId());
                rubro.setDescripcion(entity.getLinea().getRubro().getDescripcion());
                linea.setRubro(rubro);  // Ahora sí es del tipo correcto (Rubro)
            }
            
            model.setLinea(linea);
        }
        
        if (entity.getMedida() != null) {
        	Medida medida = new Medida();
        	medida.setId(entity.getMedida().getId());
        	medida.setDescripcion(entity.getMedida().getDescripcion());
        	model.setMedida(medida);
        }
        
        if (entity.getPresentacion() != null) {
        	Presentacion presentacion = new Presentacion();
        	presentacion.setId(entity.getPresentacion().getId());
        	presentacion.setDescripcion(entity.getPresentacion().getDescripcion());
        	model.setPresentacion(presentacion);
        }
        
        if (entity.getProveedor() != null) {
        	Proveedor proveedor = new Proveedor();
        	proveedor.setId(entity.getProveedor().getId());
        	proveedor.setNombreFantasia(entity.getProveedor().getNombreFantasia());
        	model.setProveedor(proveedor);
        }
        
        model.setFechaCompra(entity.getFechaCompra());
        model.setFechaVencimiento(entity.getFechaVencimiento());
        model.setFechaBaja(entity.getFechaBaja());
        model.setFechaActualizPrecios(entity.getFechaActualizPrecios());
        model.setFila(entity.getFila());
        model.setColumna(entity.getColumna());
        model.setPrecioCosto(entity.getPrecio_costo());
        model.setMargenUtilidad(entity.getMargen_utilidad());
        
        if (entity.getAlicuota() != null) {
            Alicuota alicuota = new Alicuota();
            alicuota.setId(entity.getAlicuota().getId());
            alicuota.setDescripcion(entity.getAlicuota().getDescripcion());
            alicuota.setGravamen(entity.getAlicuota().getGravamen());
            model.setAlicuota(alicuota);
        }
        
        model.setEsBonificado(entity.isEsBonificado());
        model.setBonificacion(entity.getBonificacion());
        model.setEstado(entity.getEstado());

        return model;
    }

	public JpaArticuloEntity toEntity(Articulo model) {
        
		if (model == null) return null;

        JpaArticuloEntity entity = new JpaArticuloEntity();
        entity.setId(model.getId());
        entity.setCodigoInterno(model.getCodigo_interno());
        entity.setCodigoBarra(model.getCodigo_barra());
        entity.setDescripcion(model.getDescripcion());
        entity.setNumeroLote(model.getNrolote());
        entity.setStock(model.getStock());
        entity.setStock_minimo(model.getStock_minimo());
        entity.setStock_maximo(model.getStock_maximo());
        
        // MAPEO INVERSO DE LÍNEA CON RUBRO
        if (model.getLinea() != null) {
            JpaLineaEntity linea = new JpaLineaEntity();
            linea.setIdLinea(model.getLinea().getIdLinea());
            linea.setDescripcion(model.getLinea().getDescripcion());
            
            // Mapear el rubro de la línea (si existe)
            if (model.getLinea().getRubro() != null) {
                JpaRubroEntity rubro = new JpaRubroEntity();
                rubro.setId(model.getLinea().getRubro().getId());
                rubro.setDescripcion(model.getLinea().getRubro().getDescripcion());
                linea.setRubro(rubro);  // Ahora es JpaRubroEntity
            }
            
            entity.setLinea(linea);
        }
        
        if (model.getMedida() != null) {
        	JpaMedidaEntity medida = new JpaMedidaEntity();
        	medida.setId(model.getMedida().getId());
        	medida.setDescripcion(model.getMedida().getDescripcion());
        	entity.setMedida(medida);
        }
        
        if (model.getPresentacion() != null) {
        	JpaPresentacionEntity presentacion = new JpaPresentacionEntity();
        	presentacion.setId(model.getPresentacion().getId());
        	presentacion.setDescripcion(model.getPresentacion().getDescripcion());
        	entity.setPresentacion(presentacion);
        }
        
        if (model.getProveedor() != null) {
        	JpaProveedorEntity proveedor = new JpaProveedorEntity();
        	proveedor.setId(model.getProveedor().getId());
        	proveedor.setNombreFantasia(model.getProveedor().getNombreFantasia());
        	entity.setProveedor(proveedor);
        }
        
        entity.setFechaCompra(model.getFechaCompra());
        entity.setFechaVencimiento(model.getFechaVencimiento());
        entity.setFechaBaja(model.getFechaBaja());
        entity.setFechaActualizPrecios(model.getFechaActualizPrecios());
        entity.setFila(model.getFila());
        entity.setColumna(model.getColumna());
        entity.setPrecio_costo(model.getPrecioCosto());
        entity.setMargen_utilidad(model.getMargenUtilidad());
        
        if (model.getAlicuota() != null) {
            JpaAlicuotaEntity alicuota = new JpaAlicuotaEntity();
            alicuota.setId(model.getAlicuota().getId());
            alicuota.setDescripcion(model.getAlicuota().getDescripcion());
            alicuota.setGravamen(model.getAlicuota().getGravamen());
            entity.setAlicuota(alicuota);
        }
        
        entity.setEsBonificado(model.getEsBonificado());
        entity.setBonificacion(model.getBonificacion());
        entity.setEstado(model.getEstado());

        return entity;
    }

}
