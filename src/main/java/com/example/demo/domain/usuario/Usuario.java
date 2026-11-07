package com.example.demo.domain.usuario;

public record Usuario(Long id, String email, String passwordHash, Rol rol) {
    public enum Rol { USER, ADMIN }
}
