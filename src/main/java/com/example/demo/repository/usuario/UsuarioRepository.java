package com.example.demo.repository.usuario;

import com.example.demo.domain.usuario.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
    boolean existsByEmail(String email);
}
