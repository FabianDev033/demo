package com.example.demo.service.favorito;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.favorito.FavoritoRequestDTO;
import com.example.demo.dto.favorito.FavoritoResponseDTO;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.model.Favorito;
import com.example.demo.repository.FavoritoRepository;

@Service 
public class FavoritoService {
    private final FavoritoRepository favoritoRepository;

    public FavoritoService(FavoritoRepository favoritoRepository){
        this.favoritoRepository = favoritoRepository;
    }

    public List<FavoritoResponseDTO> obtenerTodos(){
        List<Favorito> favoritos = favoritoRepository.findAll();
        return favoritos.stream().map(this::convertirAResponse).toList();
    }

    private FavoritoResponseDTO convertirAResponse(Favorito favorito){
        return new FavoritoResponseDTO(favorito.getId(), favorito.getProductoId());
    }

    public FavoritoResponseDTO obtenerPorId(Long id) {
        Favorito favorito = favoritoRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Favorito con id " + id + " no encontrado"));
        return convertirAResponse(favorito);
    }

    public FavoritoResponseDTO crear(FavoritoRequestDTO request){
        Favorito favorito = new Favorito();
        favorito.setProductoId(request.productoId());
        Favorito guardado = favoritoRepository.save(favorito);
        return convertirAResponse(guardado);
    }

    public FavoritoResponseDTO actualizar(Long id, FavoritoRequestDTO request){
        Favorito favorito = favoritoRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Favorito con id " + id + " no encontrado"));
        favorito.setProductoId(request.productoId());
        Favorito actualizado = favoritoRepository.save(favorito);
        return convertirAResponse(actualizado);
    }

    public void eliminar(Long id){
        favoritoRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Favorito con id " + id + " no encontrado"));
        favoritoRepository.deleteById(id);
    }
}
