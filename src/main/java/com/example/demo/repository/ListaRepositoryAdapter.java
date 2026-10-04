package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.Lista;
import com.example.demo.entity.ListaEntity;

@Repository 
public class ListaRepositoryAdapter implements ListaRepository {
    
    private final ListaJpaRepository jpaRepository;

    public ListaRepositoryAdapter(ListaJpaRepository jpaRepository){
        this.jpaRepository = jpaRepository;
    }

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
        return new ListaEntity(lista.id(), lista.nombre());
    }

    private Lista aDominio(ListaEntity entity) {
        return new Lista(entity.getId(), entity.getNombre());
    }
}
