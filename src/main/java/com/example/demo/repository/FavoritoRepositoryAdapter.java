package com.example.demo.repository;

import com.example.demo.domain.Favorito;
import com.example.demo.entity.FavoritoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository 
public class FavoritoRepositoryAdapter implements FavoritoRepository {

    private final FavoritoJpaRepository jpaRepository;
    private final ListaJpaRepository listaJpaRepository;

    public FavoritoRepositoryAdapter(FavoritoJpaRepository jpaRepository, ListaJpaRepository listaJpaRepository){
        this.jpaRepository = jpaRepository;
        this.listaJpaRepository = listaJpaRepository;
    }

    @Override 
    public List<Favorito> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::aDominio)
                .toList();
    }

    @Override 
    public Optional<Favorito> findById(Long id){
        return jpaRepository.findById(id)
        .map(this::aDominio);
    }

    @Override 
    public Favorito save(Favorito favorito){
        FavoritoEntity entity = aEntidad(favorito);
        FavoritoEntity guardado = jpaRepository.save(entity);
        return aDominio(guardado);
    }

    @Override 
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override 
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override 
    public List<Favorito> findByListaId(Long listaId){
        return jpaRepository.findByListaId(listaId).stream().map(this::aDominio).toList();
    }

    private FavoritoEntity aEntidad(Favorito favorito) {
        FavoritoEntity entity = new FavoritoEntity(
            favorito.id(), 
            favorito.productoId(), 
            favorito.nota(), 
            favorito.fechaAgregado()
        );

        if(favorito.listaId() != null){
            entity.setLista(listaJpaRepository.getReferenceById(favorito.listaId()));
        }
        return entity;
    }
    private Favorito aDominio(FavoritoEntity entity) {
        Long listaId = entity.getLista() == null ? null : entity.getLista().getId();
        return new Favorito(
            entity.getId(),
            entity.getProductoId(),
            listaId,
            entity.getNota(),
            entity.getFechaAlta()
        );
    }
    
}
