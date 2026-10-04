package com.example.demo.service;

import com.example.demo.domain.Favorito;
import com.example.demo.domain.Lista;
import com.example.demo.dto.favorito.FavoritoResponse;
import com.example.demo.dto.lista.ListaRequest;
import com.example.demo.dto.lista.ListaResponse;
import com.example.demo.exception.ListaNoVaciaException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.repository.FavoritoRepository;
import com.example.demo.repository.ListaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service 
public class ListaServiceImpl implements ListaService {

    private final ListaRepository listaRepository;
    private final FavoritoRepository favoritoRepository;

    public ListaServiceImpl(ListaRepository listaRepository, FavoritoRepository favoritoRepository){
        this.listaRepository = listaRepository;
        this.favoritoRepository = favoritoRepository;
    }

    @Override 
    public ListaResponse crear(ListaRequest request) {
        Lista guardada = listaRepository.save(new Lista(null, request.nombre()));
        return aResponse(guardada);
    }

    @Override 
    public List<ListaResponse> listar() {
        return listaRepository.findAll().stream().map(this::aResponse).toList();
    }
    
    @Override 
    public ListaResponse obtener(Long id) {
        return aResponse(buscarOFallar(id));
    }

    @Override 
    public List<FavoritoResponse> listarFavoritos(Long listaId) {
        buscarOFallar(listaId);
        return favoritoRepository.findByListaId(listaId).stream().map(this::aFavoritoResponse).toList();
    }

    @Override 
    public void eliminar(Long id) {
        buscarOFallar(id);

        if(!favoritoRepository.findByListaId(id).isEmpty()){
            throw new ListaNoVaciaException("no se puede eliminar una lista que tiene favoritos");
        }
        listaRepository.deleteById(id);
    }

    @Override 
    @Transactional 
    public void moverFavoritos(Long origenId, Long destinoId){
        buscarOFallar(origenId);
        buscarOFallar(destinoId);

        List<Favorito> favoritos = favoritoRepository.findByListaId(origenId);

        for(Favorito favorito : favoritos){
            Favorito movido = new Favorito(
                favorito.id(),
                favorito.productoId(),
                destinoId,
                favorito.nota(),
                favorito.fechaAgregado()
            );
            favoritoRepository.save(movido);
        }
        listaRepository.deleteById(origenId);
    }

    private Lista buscarOFallar(Long id) {
        return listaRepository.findById(id).orElseThrow(()-> new RecursoNoEncontradoException("No existe la lista con id " + id));
    }

    private ListaResponse aResponse(Lista lista) {
        return new ListaResponse(lista.id(), lista.nombre());
    }

    private FavoritoResponse aFavoritoResponse(Favorito favorito){
        return new FavoritoResponse(
            favorito.id(),
            favorito.productoId(),
            favorito.listaId(),
            favorito.nota(),
            favorito.fechaAgregado()
        );
    }
}
