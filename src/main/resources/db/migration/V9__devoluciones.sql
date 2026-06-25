CREATE TABLE IF NOT EXISTS devoluciones (
    iddevolucion BIGSERIAL PRIMARY KEY,
    idventa INTEGER NOT NULL,
    idarticulo INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,
    monto_devuelto NUMERIC(15,2) NOT NULL,
    tipo_operacion VARCHAR(20) NOT NULL,
    idnota_credito BIGINT,
    idpago BIGINT,
    idmovimiento_caja INTEGER,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    mal_estado BOOLEAN NOT NULL DEFAULT false,
    observaciones VARCHAR(500),
    usuario_id INTEGER NOT NULL,
    CONSTRAINT fk_dev_venta FOREIGN KEY (idventa) REFERENCES ventas(idventa),
    CONSTRAINT fk_dev_articulo FOREIGN KEY (idarticulo) REFERENCES articulos(idarticulo),
    CONSTRAINT fk_dev_nota_credito FOREIGN KEY (idnota_credito) REFERENCES notas_credito(id),
    CONSTRAINT fk_dev_pago FOREIGN KEY (idpago) REFERENCES pagos(idpago),
    CONSTRAINT fk_dev_movimiento_caja FOREIGN KEY (idmovimiento_caja) REFERENCES movimientos_caja(idmovimiento),
    CONSTRAINT fk_dev_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

COMMENT ON TABLE devoluciones IS 'Registro de devoluciones de artículos de una venta';
COMMENT ON COLUMN devoluciones.tipo_operacion IS 'EGRESO_CAJA o NOTA_CREDITO';
