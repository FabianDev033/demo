package com.example.demo.service;

import com.example.demo.domain.usuario.Usuario;
import com.example.demo.repository.usuario.UsuarioRepository;
import com.example.demo.security.CredencialesInvalidasException;
import com.example.demo.security.EmailEnUsoException;
import com.example.demo.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public UsuarioServiceImpl(UsuarioRepository usuarios, PasswordEncoder encoder, JwtService jwt) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Override
    @Transactional
    public Usuario registrar(String email, String password) {
        String emailNormalizado = email.trim().toLowerCase();
        if (usuarios.existsByEmail(emailNormalizado)) {
            throw new EmailEnUsoException();
        }
        return usuarios.save(new Usuario(null, emailNormalizado, encoder.encode(password), Usuario.Rol.USER));
    }

    @Override
    public Usuario autenticar(String email, String password) {
        Usuario usuario = usuarios.findByEmail(email.trim().toLowerCase())
                .orElseThrow(CredencialesInvalidasException::new);
        if (!encoder.matches(password, usuario.passwordHash())) {
            throw new CredencialesInvalidasException();
        }
        return usuario;
    }

    @Override
    public String token(Usuario usuario) {
        return jwt.generar(usuario);
    }
}
