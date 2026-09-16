package com.example.demo.controller.favorito;

import com.example.demo.dto.favorito.FavoritoRequestDTO;
import com.example.demo.dto.favorito.FavoritoResponseDTO;
import com.example.demo.service.favorito.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@Tag(
        name = "Favoritos",
        description = "Operaciones relacionadas con favoritos"
)
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @Operation(
            summary = "Obtener todos los favoritos",
            description = "Obtiene el listado de favoritos."
    )
    @GetMapping
    public List<FavoritoResponseDTO> obtenerTodos() {
        return favoritoService.obtenerTodos();
    }

    @Operation(
            summary = "Obtener un favorito por ID",
            description = "Obtiene un favorito específico."
    )
    @GetMapping("/{id}")
    public FavoritoResponseDTO obtenerPorId(@PathVariable Long id) {
        return favoritoService.obtenerPorId(id);
    }

    @Operation(
            summary = "Crear un favorito",
            description = "Crea un nuevo favorito asociado a un producto."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritoResponseDTO crear(
            @Valid @RequestBody FavoritoRequestDTO request
    ) {
        return favoritoService.crear(request);
    }

    @Operation(
            summary = "Actualizar un favorito",
            description = "Actualiza el producto asociado a un favorito."
    )
    @PutMapping("/{id}")
    public FavoritoResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FavoritoRequestDTO request
    ) {
        return favoritoService.actualizar(id, request);
    }

    @Operation(
            summary = "Eliminar un favorito",
            description = "Elimina un favorito por su ID."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        favoritoService.eliminar(id);
    }
}