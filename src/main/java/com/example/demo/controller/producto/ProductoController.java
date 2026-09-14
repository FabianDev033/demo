package com.example.demo.controller.producto;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.producto.dto.ProductoDTO;
import com.example.demo.service.producto.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController 
@RequestMapping("/api/productos")
@Tag(
    name = "Productos",
    description = "Operacions relacionadas con el catalogo de productos"
)
public class ProductoController {
    
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    
    @Operation(
        summary = "Obtener todos los productos",
        description = "Obtiene todos los productos del catalogo externo."
    )
    @GetMapping
    public List<ProductoDTO> obtenerProductos() {
        return productoService.obtenerProductos();
    }
    
    @Operation(
        summary = "Obtener producto por ID",
        description = "Obtiene un producto específico por su ID."
    )
    @GetMapping("/{id}")
    public ProductoDTO obtenerProductoPorId(@PathVariable Long id){
        return productoService.obtenerProductoPorId(id);
    }
}
