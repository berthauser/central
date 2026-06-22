-- Tabla de permisos por usuario para cada vista del sistema
CREATE TABLE IF NOT EXISTS permisos_usuario (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    vista_clase VARCHAR(255) NOT NULL,
    puede_ver BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT uk_permiso_usuario_vista UNIQUE (usuario_id, vista_clase)
);

COMMENT ON TABLE permisos_usuario IS 'Permisos de acceso a vistas por usuario';
COMMENT ON COLUMN permisos_usuario.usuario_id IS 'ID del usuario (FK a usuarios)';
COMMENT ON COLUMN permisos_usuario.vista_clase IS 'Fully qualified class name de la vista';
COMMENT ON COLUMN permisos_usuario.puede_ver IS 'true = puede acceder, false = denegado';
