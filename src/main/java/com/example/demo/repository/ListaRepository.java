package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.Lista;

public interface ListaRepository {
    List<Lista> findAll();
    List<Lista> findByUsuarioId(Long usuarioId);
    Optional<Lista> findById(Long id);
    Lista save (Lista lista);
    void deleteById(Long id);
    boolean existsById(Long id);    
}
