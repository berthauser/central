-- Migrate display labels to enum constant names for remaining entities
-- Old converter stored getLabel() values; @Enumerated(STRING) expects name()

-- SituacionFiscal: "IVA Responsable No Inscripto" → IVA_Responsable_No_Inscripto, etc.
UPDATE vendedores SET situacion_fiscal = REPLACE(situacion_fiscal, ' ', '_');
UPDATE proveedores SET situacion_fiscal = REPLACE(situacion_fiscal, ' ', '_');

-- TipoDocumento: "CUIT/CUIL" → Cuit_Cuil, "DNI" → Dni, "No Corresponde" → No_Corresponde
-- Old converter used equalsIgnoreCase, so handle any casing
UPDATE vendedores SET documento = 'Cuit_Cuil' WHERE LOWER(documento) = 'cuit/cuil';
UPDATE vendedores SET documento = 'Dni' WHERE LOWER(documento) = 'dni';
UPDATE vendedores SET documento = 'No_Corresponde' WHERE LOWER(documento) = 'no corresponde';

UPDATE proveedores SET documento = 'Cuit_Cuil' WHERE LOWER(documento) = 'cuit/cuil';
UPDATE proveedores SET documento = 'Dni' WHERE LOWER(documento) = 'dni';
UPDATE proveedores SET documento = 'No_Corresponde' WHERE LOWER(documento) = 'no corresponde';

UPDATE grupos_fam SET documento = 'Cuit_Cuil' WHERE LOWER(documento) = 'cuit/cuil';
UPDATE grupos_fam SET documento = 'Dni' WHERE LOWER(documento) = 'dni';
UPDATE grupos_fam SET documento = 'No_Corresponde' WHERE LOWER(documento) = 'no corresponde';

-- EstadoArticulo: labels contain spaces that don't match enum names
UPDATE articulos SET estado = 'EnExistencias' WHERE estado = 'En Existencias';
UPDATE articulos SET estado = 'NoDisponible' WHERE estado = 'No Disponible';
-- Disponible, Comprometido, Entrante, Baja already match their names

-- Parentesco: labels contain "/" that doesn't match enum names
UPDATE grupos_fam SET parentesco = 'Hijo' WHERE parentesco = 'Hijo/a';
UPDATE grupos_fam SET parentesco = 'Esposo' WHERE parentesco = 'Esposo/a';
UPDATE grupos_fam SET parentesco = 'Nieto' WHERE parentesco = 'Nieto/a';
UPDATE grupos_fam SET parentesco = 'Sobrino' WHERE parentesco = 'Sobrino/a';
-- "Otros" already matches

-- Provincia: "Buenos Aires" → Buenos_Aires, etc.
UPDATE departamentos SET provincia = REPLACE(provincia, ' ', '_');

-- TipoDomicilio: "Fiscal Pcial Jurisdicción Sede" → Fiscal_Pcial_Jurisdicción_Sede, etc.
UPDATE domicilios SET tipodomicilio = REPLACE(tipodomicilio, ' ', '_');
