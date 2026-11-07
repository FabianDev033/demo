package com.example.demo.service;

import com.example.demo.domain.Lista;
import com.example.demo.dto.lista.ListaRequest;
import com.example.demo.repository.FavoritoRepository;
import com.example.demo.repository.ListaRepository;
import com.example.demo.security.ListaOwnership;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListaServiceImplTest {
    @Mock
    private ListaRepository listas;

    @Mock
    private FavoritoRepository favoritos;

    @Mock
    private ListaOwnership ownership;

    @InjectMocks
    private ListaServiceImpl servicio;

    @Test
    void crear_listaNueva_asignaAlUsuarioAutenticado() {
        when(ownership.userId()).thenReturn(42L);
        when(listas.save(any(Lista.class))).thenAnswer(invocation -> {
            Lista nueva = invocation.getArgument(0);
            return new Lista(100L, nueva.nombre(), nueva.usuarioId());
        });

        var response = servicio.crear(new ListaRequest("Lecturas"));

        ArgumentCaptor<Lista> listaGuardada = ArgumentCaptor.forClass(Lista.class);
        verify(listas).save(listaGuardada.capture());
        assertEquals(42L, listaGuardada.getValue().usuarioId());
        assertEquals(100L, response.id());
        assertEquals("Lecturas", response.nombre());
    }
}
