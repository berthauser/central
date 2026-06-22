CREATE TABLE IF NOT EXISTS comisiones_ventas (
    idcomision BIGSERIAL PRIMARY KEY,
    idvendedor INTEGER NOT NULL,
    idventa INTEGER NOT NULL,
    idpago BIGINT NOT NULL,
    base_comisionable NUMERIC(15,2) NOT NULL,
    porcentaje NUMERIC(5,2) NOT NULL,
    comision_bruta NUMERIC(15,2) NOT NULL,
    ajustes NUMERIC(15,2) NOT NULL DEFAULT 0,
    comision_final NUMERIC(15,2) NOT NULL,
    fecha_calculo DATE NOT NULL DEFAULT CURRENT_DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    observaciones TEXT,

    CONSTRAINT fk_comision_vendedor FOREIGN KEY (idvendedor) REFERENCES vendedores(idvendedor),
    CONSTRAINT fk_comision_venta FOREIGN KEY (idventa) REFERENCES ventas(idventa),
    CONSTRAINT fk_comision_pago FOREIGN KEY (idpago) REFERENCES pagos(idpago)
);

COMMENT ON TABLE comisiones_ventas IS 'Comisiones generadas por ventas cobradas';
COMMENT ON COLUMN comisiones_ventas.idcomision IS 'Identificador único de la comisión';
COMMENT ON COLUMN comisiones_ventas.idvendedor IS 'Vendedor que recibe la comisión';
COMMENT ON COLUMN comisiones_ventas.idventa IS 'Venta que originó la comisión';
COMMENT ON COLUMN comisiones_ventas.idpago IS 'Pago que disparó el cálculo';
COMMENT ON COLUMN comisiones_ventas.base_comisionable IS 'Monto base sobre el cual se calcula (monto del pago)';
COMMENT ON COLUMN comisiones_ventas.porcentaje IS 'Porcentaje de comisión aplicado';
COMMENT ON COLUMN comisiones_ventas.comision_bruta IS 'Comisión antes de ajustes';
COMMENT ON COLUMN comisiones_ventas.ajustes IS 'Ajustes aplicados (devoluciones, NC, etc.)';
COMMENT ON COLUMN comisiones_ventas.comision_final IS 'Comisión neta final';
COMMENT ON COLUMN comisiones_ventas.fecha_calculo IS 'Fecha en que se calculó la comisión';
COMMENT ON COLUMN comisiones_ventas.estado IS 'PENDIENTE, PAGADA, AJUSTADA, ANULADA';
COMMENT ON COLUMN comisiones_ventas.observaciones IS 'Notas o referencia del cálculo';
