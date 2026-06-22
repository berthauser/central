CREATE TABLE IF NOT EXISTS reglas_comision (
    idregla BIGSERIAL PRIMARY KEY,
    idvendedor INTEGER,
    nombre VARCHAR(100) NOT NULL,
    tipo_base_comisionable VARCHAR(30) NOT NULL DEFAULT 'MONTO_PAGO',
    tipo_calculo VARCHAR(20) NOT NULL DEFAULT 'PORCENTAJE',
    valor_calculo NUMERIC(12,4) NOT NULL,
    tipo_evento VARCHAR(20) NOT NULL DEFAULT 'COBRO',
    incluir_descuentos BOOLEAN NOT NULL DEFAULT true,
    ajustar_devoluciones BOOLEAN NOT NULL DEFAULT true,
    ventana_ajuste_dias INTEGER NOT NULL DEFAULT 30,
    activo BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion DATE NOT NULL DEFAULT CURRENT_DATE,

    CONSTRAINT fk_regla_vendedor FOREIGN KEY (idvendedor) REFERENCES vendedores(idvendedor)
);

CREATE INDEX idx_regla_vendedor ON reglas_comision(idvendedor);
CREATE INDEX idx_regla_activo ON reglas_comision(activo);

COMMENT ON TABLE reglas_comision IS 'Reglas de cálculo de comisiones por vendedor';
COMMENT ON COLUMN reglas_comision.idregla IS 'Identificador único de la regla';
COMMENT ON COLUMN reglas_comision.idvendedor IS 'Vendedor asociado (NULL = regla global)';
COMMENT ON COLUMN reglas_comision.nombre IS 'Nombre descriptivo de la regla';
COMMENT ON COLUMN reglas_comision.tipo_base_comisionable IS 'MONTO_PAGO, TOTAL_VENTA, NETO_VENTA, SUBTOTAL_VENTA';
COMMENT ON COLUMN reglas_comision.tipo_calculo IS 'PORCENTAJE, MONTO_FIJO';
COMMENT ON COLUMN reglas_comision.valor_calculo IS 'Valor del porcentaje (ej: 5.0000) o monto fijo';
COMMENT ON COLUMN reglas_comision.tipo_evento IS 'FACTURA o COBRO: evento que dispara la comisión';
COMMENT ON COLUMN reglas_comision.incluir_descuentos IS 'Si se descuentan bonificaciones de la base';
COMMENT ON COLUMN reglas_comision.ajustar_devoluciones IS 'Si se ajusta la comisión por devoluciones/NC';
COMMENT ON COLUMN reglas_comision.ventana_ajuste_dias IS 'Días para aplicar ajustes por devolución';
COMMENT ON COLUMN reglas_comision.activo IS 'Si la regla está vigente';
COMMENT ON COLUMN reglas_comision.fecha_creacion IS 'Fecha de creación de la regla';
