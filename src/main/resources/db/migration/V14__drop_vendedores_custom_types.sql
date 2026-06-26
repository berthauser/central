-- Remove custom type references from vendedores
-- situacion_fiscal: DEFAULT 'Consumidor Final'::sfiscal → 'Consumidor_Final'::character varying
-- documento: DEFAULT 'No corresponde'::tdocumento → 'No_Corresponde'::character varying

ALTER TABLE vendedores ALTER COLUMN situacion_fiscal DROP DEFAULT;
ALTER TABLE vendedores ALTER COLUMN situacion_fiscal SET DEFAULT 'Consumidor_Final'::character varying;

ALTER TABLE vendedores ALTER COLUMN documento DROP DEFAULT;
ALTER TABLE vendedores ALTER COLUMN documento SET DEFAULT 'No_Corresponde'::character varying;
