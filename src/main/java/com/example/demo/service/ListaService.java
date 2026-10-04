package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.favorito.FavoritoResponse;
import com.example.demo.dto.lista.ListaRequest;
import com.example.demo.dto.lista.ListaResponse;

public interface ListaService {

    ListaResponse crear(ListaRequest request);
    List<ListaResponse> listar();
    ListaResponse obtener(Long id);
    List<FavoritoResponse> listarFavoritos(Long listaId);
    void eliminar(Long id);
    void moverFavoritos(Long origenId, Long destinoId);
}
