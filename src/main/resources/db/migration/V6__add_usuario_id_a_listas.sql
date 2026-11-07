-- Owner técnico sin credenciales utilizables: conserva datos TP2 sin asignarlos a una cuenta registrada.
INSERT INTO usuarios (email, password_hash, rol)
VALUES ('legacy-migration@invalid.local', '!DISABLED!legacy-migration-account-no-login', 'USER');
ALTER TABLE listas ADD COLUMN usuario_id BIGINT;
UPDATE listas SET usuario_id = (SELECT id FROM usuarios WHERE email = 'legacy-migration@invalid.local');
ALTER TABLE listas ALTER COLUMN usuario_id SET NOT NULL;
ALTER TABLE listas ADD CONSTRAINT fk_listas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id);
CREATE INDEX idx_listas_usuario_id ON listas(usuario_id);
