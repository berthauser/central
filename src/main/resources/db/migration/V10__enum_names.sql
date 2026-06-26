-- Migrate display labels to enum constant names for Cliente enums
-- Old converter stored getLabel() values; @Enumerated(STRING) expects name()

UPDATE clientes SET sexo = 'No_Corresponde' WHERE sexo = 'No Corresponde';

UPDATE clientes SET documento = 'Cuit_Cuil' WHERE documento = 'CUIT/CUIL';
UPDATE clientes SET documento = 'Dni' WHERE documento = 'DNI';
UPDATE clientes SET documento = 'No_Corresponde' WHERE documento = 'No Corresponde';

UPDATE clientes SET situacion_fiscal = REPLACE(situacion_fiscal, ' ', '_');
