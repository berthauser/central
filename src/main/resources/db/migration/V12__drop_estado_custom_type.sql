-- Remove custom type einventario dependency from articulos.estado column
-- Old: estado varchar(15) DEFAULT 'En Existencias'::einventario NOT NULL
-- New: estado varchar(15) DEFAULT 'EnExistencias'::character varying NULL

ALTER TABLE articulos ALTER COLUMN estado DROP DEFAULT;
ALTER TABLE articulos ALTER COLUMN estado SET DEFAULT 'EnExistencias'::character varying;
ALTER TABLE articulos ALTER COLUMN estado DROP NOT NULL;
