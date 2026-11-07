package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;

import com.example.demo.domain.Lista;
import com.example.demo.entity.ListaEntity;

@Repository 
public class ListaRepositoryAdapter implements ListaRepository {
    
    private final ListaJpaRepository jpaRepository;
    private final EntityManager entityManager;

    public ListaRepositoryAdapter(ListaJpaRepository jpaRepository, EntityManager entityManager){
        this.jpaRepository = jpaRepository;
        this.entityManager = entityManager;
    }
    @Override public List<Lista> findByUsuarioId(Long usuarioId) { return jpaRepository.findByUsuarioId(usuarioId).stream().map(this::aDominio).toList(); }

    @Override 
    public List<Lista> findAll(){
        return jpaRepository.findAll().stream().map(this::aDominio).toList();
    }
    
    @Override
    public Optional<Lista> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::aDominio);
    }

    @Override
    public Lista save(Lista lista) {
        ListaEntity entity = aEntidad(lista);
        ListaEntity guardada = jpaRepository.save(entity);
        return aDominio(guardada);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    private ListaEntity aEntidad(Lista lista) {
        return new ListaEntity(lista.id(), lista.nombre(), entityManager.getReference(com.example.demo.entity.usuario.UsuarioEntity.class, lista.usuarioId()));
    }

    private Lista aDominio(ListaEntity entity) {
        return new Lista(entity.getId(), entity.getNombre(), entity.getUsuario().getId());
    }
}
