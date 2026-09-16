package com.example.demo.client.dummyjson;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.ServicioExternoException;

@Component
public class DummyJsonClient {

    private final RestClient restClient;

    public DummyJsonClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public DummyJsonProductosResponse obtenerProductos() {
        return restClient
                .get()
                .uri("/products")
                .retrieve()
                .body(DummyJsonProductosResponse.class);
    }

    public DummyJsonProducto obtenerProductoPorId(Long id) {
        try {
            return restClient
                    .get()
                    .uri("/products/{id}", id)
                    .retrieve()
                    .body(DummyJsonProducto.class);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new RecursoNoEncontradoException("Producto con id " + id + " no encontrado en DummyJSON");
            }
            throw new ServicioExternoException(
                    "Error al consultar el producto en DummyJSON",
                    ex
            );
        }
    }
}