-- Remove custom type references from proveedores
-- situacion_fiscal: DEFAULT 'IVA Responsable Inscripto'::sfiscal NOT NULL → 'IVA_Responsable_Inscripto'::character varying NOT NULL
-- documento: DEFAULT 'No corresponde'::tdocumento NOT NULL → 'No_Corresponde'::character varying NOT NULL

ALTER TABLE proveedores ALTER COLUMN situacion_fiscal DROP DEFAULT;
ALTER TABLE proveedores ALTER COLUMN situacion_fiscal SET DEFAULT 'IVA_Responsable_Inscripto'::character varying;

ALTER TABLE proveedores ALTER COLUMN documento DROP DEFAULT;
ALTER TABLE proveedores ALTER COLUMN documento SET DEFAULT 'No_Corresponde'::character varying;
