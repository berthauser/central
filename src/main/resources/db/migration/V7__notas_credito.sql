CREATE TABLE IF NOT EXISTS notas_credito (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER NOT NULL REFERENCES clientes(id),
    monto NUMERIC(12,2) NOT NULL,
    fecha DATE NOT NULL,
    observaciones VARCHAR(500)
);

COMMENT ON TABLE notas_credito IS 'Notas de crédito generadas por saldo a favor en pagos';
COMMENT ON COLUMN notas_credito.cliente_id IS 'ID del cliente (FK a clientes)';
COMMENT ON COLUMN notas_credito.monto IS 'Monto del saldo a favor';
COMMENT ON COLUMN notas_credito.fecha IS 'Fecha de generación';
