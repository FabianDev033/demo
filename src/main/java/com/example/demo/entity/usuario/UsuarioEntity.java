package com.example.demo.entity.usuario;

import com.example.demo.domain.usuario.Usuario;
import jakarta.persistence.*;

@Entity @Table(name="usuarios")
public class UsuarioEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=254) private String email;
    @Column(name="password_hash", nullable=false, length=100) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private Usuario.Rol rol;
    protected UsuarioEntity() {}
    public UsuarioEntity(Long id, String email, String passwordHash, Usuario.Rol rol) { this.id=id; this.email=email; this.passwordHash=passwordHash; this.rol=rol; }
    public Long getId(){return id;} public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;} public Usuario.Rol getRol(){return rol;}
}
