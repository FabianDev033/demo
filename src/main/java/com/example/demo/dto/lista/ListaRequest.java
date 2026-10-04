package com.example.demo.dto.lista;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ListaRequest (
    @NotBlank(message = "nombre no puede estar vacio")
    @Size(max = 150, message = "nombre no puede superar los 150 caracteres")
    String nombre
){
}
