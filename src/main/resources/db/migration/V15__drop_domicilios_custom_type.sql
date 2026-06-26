-- Remove custom type reference from domicilios
-- tipodomicilio: DEFAULT 'Fiscal Pcial Jurisdicción Sede'::tdomicilio → 'Fiscal_Pcial_Jurisdicción_Sede'::character varying

ALTER TABLE domicilios ALTER COLUMN tipodomicilio DROP DEFAULT;
ALTER TABLE domicilios ALTER COLUMN tipodomicilio SET DEFAULT 'Fiscal_Pcial_Jurisdicción_Sede'::character varying;
