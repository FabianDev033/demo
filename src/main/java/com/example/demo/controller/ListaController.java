package com.example.demo.controller;

import com.example.demo.dto.favorito.FavoritoResponse;
import com.example.demo.dto.lista.ListaRequest;
import com.example.demo.dto.lista.ListaResponse;
import com.example.demo.service.ListaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.lista.MoverFavoritosRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "listas", description = "Listas para organizar favoritos")
public class ListaController {

    private final ListaService service;

    public ListaController(ListaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Crear una lista")
    public ResponseEntity<ListaResponse> crear(@Valid @RequestBody ListaRequest request) {
        ListaResponse creada = service.crear(request);
        return ResponseEntity
                .created(URI.create("/api/listas/" + creada.id()))
                .body(creada);
    }

    @GetMapping
    @Operation(summary = "Listar listas")
    public List<ListaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una lista por id")
    public ListaResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @GetMapping("/{id}/favoritos")
    @Operation(summary = "Listar los favoritos de una lista")
    public List<FavoritoResponse> listarFavoritos(@PathVariable Long id) {
        return service.listarFavoritos(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una lista vacía")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{origenId}/mover-favoritos")
    @Operation(summary = "Mover favoritos a otra lista", description = "Reasigna los favoritos y elimina la lista de origen")
    public ResponseEntity<Void> moverFavoritos(
        @PathVariable Long origenId,
        @Valid @RequestBody MoverFavoritosRequest request
    ) {
        service.moverFavoritos(origenId, request.destinoId());
        return ResponseEntity.noContent().build();
    }
    
}