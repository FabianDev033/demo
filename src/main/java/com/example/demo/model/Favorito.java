package com.example.demo.model;

public class Favorito {
    private Long id;
    private Long productoId;
    public Long getId() {
        return id;
    }
    public Long getProductoId() {
        return productoId;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
}
