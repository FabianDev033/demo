package com.example.demo.repository;

import com.example.demo.domain.Favorito;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación en memoria de FavoritoRepository (sin persistencia real —
 * eso llega en el TP2 con JPA). Usa ConcurrentHashMap y AtomicLong porque
 * Tomcat atiende requests en paralelo, en threads distintos: un HashMap
 * normal puede corromperse con escrituras concurrentes, y un contador
 * long++ común puede repetir un id si dos requests llegan al mismo tiempo.
 */
@Repository
public class InMemoryFavoritoRepository implements FavoritoRepository {

    private final Map<Long, Favorito> datos = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public List<Favorito> findAll() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public Optional<Favorito> findById(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public Favorito save(Favorito favorito) {
        Long id = favorito.id() != null ? favorito.id() : nextId.getAndIncrement();
        Favorito guardado = new Favorito(id, favorito.productoId(), favorito.nota(), favorito.fechaAgregado());
        datos.put(id, guardado);
        return guardado;
    }

    @Override
    public void deleteById(Long id) {
        datos.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return datos.containsKey(id);
    }
}
