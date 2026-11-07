package com.example.demo.service;
import com.example.demo.domain.Favorito; import com.example.demo.domain.Lista; import com.example.demo.dto.favorito.FavoritoResponse; import com.example.demo.dto.lista.*; import com.example.demo.exception.*; import com.example.demo.repository.*; import com.example.demo.security.ListaOwnership; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.List;
@Service public class ListaServiceImpl implements ListaService {
 private final ListaRepository listas; private final FavoritoRepository favoritos; private final ListaOwnership ownership;
 public ListaServiceImpl(ListaRepository l,FavoritoRepository f,ListaOwnership o){listas=l;favoritos=f;ownership=o;}
 public ListaResponse crear(ListaRequest r){return response(listas.save(new Lista(null,r.nombre(),ownership.userId())));}
 public List<ListaResponse> listar(){return (ownership.admin()?listas.findAll():listas.findByUsuarioId(ownership.userId())).stream().map(this::response).toList();}
 public ListaResponse obtener(Long id){ownership.authorize(id);return response(exigir(id));}
 public List<FavoritoResponse> listarFavoritos(Long id){ownership.authorize(id);return favoritos.findByListaId(id).stream().map(this::favResponse).toList();}
 public void eliminar(Long id){ownership.authorize(id);exigir(id);if(!favoritos.findByListaId(id).isEmpty())throw new ListaNoVaciaException("no se puede eliminar una lista que tiene favoritos");listas.deleteById(id);}
 @Transactional public void moverFavoritos(Long origen,Long destino){if(origen.equals(destino))throw new IllegalArgumentException("La lista destino debe ser distinta del origen");ownership.authorize(origen);ownership.authorize(destino);exigir(origen);exigir(destino);for(Favorito f:favoritos.findByListaId(origen))favoritos.save(new Favorito(f.id(),f.productoId(),destino,f.nota(),f.fechaAgregado()));listas.deleteById(origen);}
 private Lista exigir(Long id){return listas.findById(id).orElseThrow(()->new RecursoNoEncontradoException("No existe la lista con id "+id));}
 private ListaResponse response(Lista l){return new ListaResponse(l.id(),l.nombre());}
 private FavoritoResponse favResponse(Favorito f){return new FavoritoResponse(f.id(),f.productoId(),f.listaId(),f.nota(),f.fechaAgregado());}
}
