package com.example.demo.service;

import com.example.demo.domain.usuario.Usuario;
import com.example.demo.repository.usuario.UsuarioRepository;
import com.example.demo.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioServiceImplTest {
    @Test
    void registrar_contrasenaEnClaro_guardaHashBcrypt() {
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        JwtService jwt = mock(JwtService.class);
        String password = "un-password-seguro";
        when(usuarios.existsByEmail("ana@ejemplo.com")).thenReturn(false);
        when(usuarios.save(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioServiceImpl servicio = new UsuarioServiceImpl(usuarios, encoder, jwt);
        Usuario guardado = servicio.registrar("Ana@ejemplo.com", password);

        assertNotEquals(password, guardado.passwordHash());
        assertTrue(guardado.passwordHash().startsWith("$2a$"));
        assertTrue(encoder.matches(password, guardado.passwordHash()));
    }

    @Test
    void autenticar_passwordHashCoincide_devuelveUsuario() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("clave-segura");
        Usuario usuario = new Usuario(4L, "ana@ejemplo.com", hash, Usuario.Rol.USER);
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        when(usuarios.findByEmail("ana@ejemplo.com")).thenReturn(Optional.of(usuario));

        UsuarioServiceImpl servicio = new UsuarioServiceImpl(usuarios, encoder, mock(JwtService.class));

        assertTrue(servicio.autenticar("ANA@ejemplo.com", "clave-segura") == usuario);
    }
}
