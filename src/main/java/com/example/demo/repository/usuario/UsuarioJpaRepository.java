package com.example.demo.repository.usuario;
import com.example.demo.entity.usuario.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity,Long> { Optional<UsuarioEntity> findByEmail(String email); boolean existsByEmail(String email); }
