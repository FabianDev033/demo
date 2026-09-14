package com.example.demo.service.producto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.client.dummyjson.DummyJsonClient;
import com.example.demo.client.dummyjson.DummyJsonProducto;
import com.example.demo.client.dummyjson.DummyJsonProductosResponse;
import com.example.demo.dto.producto.ProductoDTO;

@Service 
public class ProductoService {
    private final DummyJsonClient dummyJsonClient;
    
    public ProductoService(DummyJsonClient dummyJsonClient) {
        this.dummyJsonClient = dummyJsonClient;
    }
    public ProductoDTO obtenerProductoPorId(Long id) {
        DummyJsonProducto producto = dummyJsonClient.obtenerProductoPorId(id);

        return new ProductoDTO(
            producto.id(),
            producto.title(),
            producto.description(),
            producto.category(),
            producto.brand(),
            producto.price(),
            producto.discountPercentage(),
            producto.stock(),
            producto.rating(),
            producto.thumbnail()
        );
    }

    public List<ProductoDTO> obtenerProductos() {
        DummyJsonProductosResponse respuesta = dummyJsonClient.obtenerProductos();

        List<ProductoDTO> productos = new ArrayList<>();

        for (DummyJsonProducto producto : respuesta.products()) {
            ProductoDTO dto = new ProductoDTO(
                producto.id(),
                producto.title(),
                producto.description(),
                producto.category(),
                producto.brand(),
                producto.price(),
                producto.discountPercentage(),
                producto.stock(),
                producto.rating(),
                producto.thumbnail()
            );
            productos.add(dto);
        }
        return productos;
    }
}
