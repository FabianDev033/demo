package com.example.demo.producto.dto;

public record ProductoDTO (
    Long id,
    String titulo, 
    String descripcion,
    String categoria,
    String marca,
    double precio,
    double porcentajeDescuento,
    int stock,
    double rating,
    String miniatura
){}
