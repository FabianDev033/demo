package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity 
@Table(name = "favoritos")
public class FavoritoEntity {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(nullable = false, length = 500)
    private String nota;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAlta;

    @ManyToOne 
    @JoinColumn(name = "lista_id", nullable = false)
    private ListaEntity lista;

    protected FavoritoEntity(){

    }
    
    public FavoritoEntity(Long id, Long productoId, String nota, LocalDateTime fechaAlta){
        this.id = id;
        this.productoId = productoId;
        this.nota = nota;
        this.fechaAlta = fechaAlta;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public ListaEntity getLista() {
    return lista;
    }

    public void setLista(ListaEntity lista) {
        this.lista = lista;
    }
    
}
