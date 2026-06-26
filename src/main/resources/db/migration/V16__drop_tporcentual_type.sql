ALTER TABLE porcentuales ALTER COLUMN clasificacion DROP DEFAULT;
ALTER TABLE porcentuales ALTER COLUMN clasificacion TYPE varchar(15) USING clasificacion::text;

DROP VIEW IF EXISTS v_listas_precios;
DROP VIEW IF EXISTS v_porcentuales_activos;

DROP TYPE IF EXISTS tporcentual;
