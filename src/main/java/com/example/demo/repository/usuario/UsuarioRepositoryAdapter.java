package com.example.demo.repository.usuario;
import com.example.demo.domain.usuario.Usuario;
import com.example.demo.entity.usuario.UsuarioEntity;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository public class UsuarioRepositoryAdapter implements UsuarioRepository {
 private final UsuarioJpaRepository jpa;
 public UsuarioRepositoryAdapter(UsuarioJpaRepository jpa){this.jpa=jpa;}
 public Usuario save(Usuario u){return toDomain(jpa.save(new UsuarioEntity(u.id(),u.email(),u.passwordHash(),u.rol())));}
 public Optional<Usuario> findByEmail(String email){return jpa.findByEmail(email).map(this::toDomain);}
 public Optional<Usuario> findById(Long id){return jpa.findById(id).map(this::toDomain);}
 public boolean existsByEmail(String email){return jpa.existsByEmail(email);}
 private Usuario toDomain(UsuarioEntity e){return new Usuario(e.getId(),e.getEmail(),e.getPasswordHash(),e.getRol());}
}
