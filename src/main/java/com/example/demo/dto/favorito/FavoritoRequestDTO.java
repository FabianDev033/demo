package com.example.demo.dto.favorito;

import jakarta.validation.constraints.NotNull;

public record FavoritoRequestDTO (
    @NotNull(message = "El productoId es obligatorio")
    Long productoId
){}
