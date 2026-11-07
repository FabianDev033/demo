package com.example.demo.security;
import com.example.demo.repository.ListaRepository; import org.springframework.security.core.Authentication; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component;
@Component public class ListaOwnership {
 private final ListaRepository listas; public ListaOwnership(ListaRepository listas){this.listas=listas;}
 public Long userId(){return principal().usuarioId();} public boolean admin(){return "ADMIN".equals(principal().rol());}
 public void authorize(Long id){var lista=listas.findById(id).orElseThrow(()->new com.example.demo.exception.RecursoNoEncontradoException("No existe la lista con id "+id));if(!admin()&&!lista.usuarioId().equals(userId()))throw new RecursoNoAutorizadoException();}
 private UsuarioPrincipal principal(){Authentication a=SecurityContextHolder.getContext().getAuthentication();if(a==null||!(a.getPrincipal() instanceof UsuarioPrincipal p))throw new RecursoNoAutorizadoException();return p;}
}
