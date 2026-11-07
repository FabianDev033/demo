package com.example.demo.service;
import com.example.demo.domain.Favorito; import com.example.demo.dto.favorito.*; import com.example.demo.exception.RecursoNoEncontradoException; import com.example.demo.repository.*; import com.example.demo.security.ListaOwnership; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.time.LocalDateTime; import java.util.List;
@Service public class FavoritoServiceImpl implements FavoritoService {
 private final FavoritoRepository repository; private final ListaRepository listas; private final ListaOwnership ownership;
 public FavoritoServiceImpl(FavoritoRepository r,ListaRepository l,ListaOwnership o){repository=r;listas=l;ownership=o;}
 public List<FavoritoResponse> listar(){return (ownership.admin()?repository.findAll():repository.findAll().stream().filter(f->listas.findById(f.listaId()).map(l -> Boolean.valueOf(l.usuarioId().equals(ownership.userId()))).orElse(false)).toList()).stream().map(this::response).toList();}
 public FavoritoResponse obtener(Long id){Favorito f=exigir(id);ownership.authorize(f.listaId());return response(f);}
 public FavoritoResponse crear(FavoritoRequest r){validarLista(r.listaId());return response(repository.save(new Favorito(null,r.productoId(),r.listaId(),r.nota(),LocalDateTime.now())));}
 @Transactional public FavoritoResponse actualizar(Long id,FavoritoRequest r){Favorito current=exigir(id);ownership.authorize(current.listaId());validarLista(r.listaId());return response(repository.save(new Favorito(current.id(),r.productoId(),r.listaId(),r.nota(),current.fechaAgregado())));}
 public void eliminar(Long id){Favorito f=exigir(id);ownership.authorize(f.listaId());repository.deleteById(id);}
 private Favorito exigir(Long id){return repository.findById(id).orElseThrow(()->new RecursoNoEncontradoException("No existe el favorito con id "+id));}
 private void validarLista(Long id){ownership.authorize(id);if(!listas.existsById(id))throw new RecursoNoEncontradoException("No existe la lista con id "+id);}
 private FavoritoResponse response(Favorito f){return new FavoritoResponse(f.id(),f.productoId(),f.listaId(),f.nota(),f.fechaAgregado());}
}
