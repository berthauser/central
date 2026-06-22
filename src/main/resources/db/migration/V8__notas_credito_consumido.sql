ALTER TABLE notas_credito ADD COLUMN IF NOT EXISTS consumido BOOLEAN NOT NULL DEFAULT false;

COMMENT ON COLUMN notas_credito.consumido IS 'true = ya fue aplicado a una compra, false = disponible';
