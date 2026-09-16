package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Favorito;

@Repository 
public class FavoritoRepository {
    private final List<Favorito> favoritos = new ArrayList<>();
    private Long siguienteId = 1l;

    public List<Favorito> findAll() {
        return new ArrayList<>(favoritos);
    }

    public Optional<Favorito> findById(Long id) {
        for (Favorito favorito : favoritos) {
            if(favorito.getId().equals(id)) {
                return Optional.of(favorito);
            }
        }
        return Optional.empty();
    }

    public Favorito save(Favorito favorito) {
        if (favorito.getId() == null){
            favorito.setId(siguienteId);
            siguienteId++;
            favoritos.add(favorito);
        }else{
            for(int i = 0; i<favoritos.size(); i++){
                if(favoritos.get(i).getId().equals(favorito.getId())){
                    favoritos.set(i,favorito);
                    break;
                }
            }
        }
        return favorito;
    }

    public void deleteById(Long id) {
        favoritos.removeIf(favorito -> favorito.getId().equals(id));
    }
}
