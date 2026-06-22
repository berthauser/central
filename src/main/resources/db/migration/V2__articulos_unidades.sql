CREATE TABLE IF NOT EXISTS public.articulos_unidades (
    idarticulo integer NOT NULL,
    idunidad integer NOT NULL,
    CONSTRAINT articulos_unidades_pkey PRIMARY KEY (idarticulo, idunidad),
    CONSTRAINT fk_au_articulo FOREIGN KEY (idarticulo)
        REFERENCES public.articulos (idarticulo) ON DELETE CASCADE,
    CONSTRAINT fk_au_unidad FOREIGN KEY (idunidad)
        REFERENCES public.unidades (idunidades) ON DELETE CASCADE
);
