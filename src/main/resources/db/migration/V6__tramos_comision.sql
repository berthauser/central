CREATE TABLE IF NOT EXISTS tramos_comision (
    idtramo BIGSERIAL PRIMARY KEY,
    idregla BIGINT NOT NULL,
    desde NUMERIC(15,2) NOT NULL DEFAULT 0,
    hasta NUMERIC(15,2),
    porcentaje NUMERIC(5,2) NOT NULL,

    CONSTRAINT fk_tramo_regla FOREIGN KEY (idregla) REFERENCES reglas_comision(idregla) ON DELETE CASCADE
);

CREATE INDEX idx_tramo_regla ON tramos_comision(idregla);

COMMENT ON TABLE tramos_comision IS 'Tramos para comisiones escalonadas';
COMMENT ON COLUMN tramos_comision.idtramo IS 'Identificador del tramo';
COMMENT ON COLUMN tramos_comision.idregla IS 'Regla de comisión asociada';
COMMENT ON COLUMN tramos_comision.desde IS 'Límite inferior del tramo (inclusive)';
COMMENT ON COLUMN tramos_comision.hasta IS 'Límite superior del tramo (exclusive, NULL = sin tope)';
COMMENT ON COLUMN tramos_comision.porcentaje IS 'Porcentaje aplicable en este tramo';
