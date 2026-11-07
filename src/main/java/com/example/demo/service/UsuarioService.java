package com.example.demo.service;

import com.example.demo.domain.usuario.Usuario;

public interface UsuarioService {
    Usuario registrar(String email, String password);

    Usuario autenticar(String email, String password);

    String token(Usuario usuario);
}
