package com.diego.Ms_Gestion_Lista.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.diego.Ms_Gestion_Lista.client.UsuarioClient;
import com.diego.Ms_Gestion_Lista.dto.*;
import com.diego.Ms_Gestion_Lista.exception.RegistroNotFoundException;
import com.diego.Ms_Gestion_Lista.model.Calificacion;
import com.diego.Ms_Gestion_Lista.model.Lista;
import com.diego.Ms_Gestion_Lista.repository.CalificacionRepository;
import com.diego.Ms_Gestion_Lista.repository.ListaRepository;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unitario de AcademicoService")
public class AcademicoServiceTest {

    @Mock
    private ListaRepository listaRepository;

    @Mock
    private CalificacionRepository calificacionRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private AcademicoService academicoService;

    private Lista listaEjemplo;
    private Calificacion calificacionEjemplo;

    @BeforeEach
    void setUp() {
        listaEjemplo = new Lista(1L, 10L, 5L, LocalDateTime.now());
        calificacionEjemplo = new Calificacion(1L, new BigDecimal("6.5"), listaEjemplo, 2L);
    }

    // --- TESTS DE LISTA ---

    @Test
    @DisplayName("crearLista() guarda y retorna la ListaDTO si el usuario existe")
    void crearLista_exito() {
        ListaRequestDTO request = new ListaRequestDTO();
        request.setIdUser(10L);
        request.setIdCurso(5L);

        when(usuarioClient.obtenerUsuarioPorId(10L)).thenReturn(new UsuarioResponseDTO());
        when(listaRepository.save(any(Lista.class))).thenReturn(listaEjemplo);

        ListaResponseDTO resultado = academicoService.crearLista(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdLista());
    }

    @Test
    @DisplayName("crearLista() lanza RuntimeException si el usuario NO existe en MS-Usuarios")
    void crearLista_usuarioNoExiste() {
        ListaRequestDTO request = new ListaRequestDTO();
        request.setIdUser(99L); 

        FeignException.NotFound feignNotFound = mock(FeignException.NotFound.class);
        when(usuarioClient.obtenerUsuarioPorId(99L)).thenThrow(feignNotFound);

        assertThrows(RuntimeException.class, () -> academicoService.crearLista(request));
    }

    @Test
    @DisplayName("crearLista() lanza excepcion generica si MS-Usuarios no responde")
    void crearLista_fallaMsUsuarios() {
        ListaRequestDTO request = new ListaRequestDTO();
        request.setIdUser(10L); 
        when(usuarioClient.obtenerUsuarioPorId(10L)).thenThrow(new RuntimeException("Connection Refused"));

        assertThrows(RuntimeException.class, () -> academicoService.crearLista(request));
    }

    @Test
    @DisplayName("obtenerTodasLasListas() retorna la lista completa")
    void obtenerTodasLasListas_exito() {
        when(listaRepository.findAll()).thenReturn(List.of(listaEjemplo));
        List<ListaResponseDTO> resultado = academicoService.obtenerTodasLasListas();
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("obtenerListaPorId() retorna la lista si existe")
    void obtenerListaPorId_exito() {
        when(listaRepository.findById(1L)).thenReturn(Optional.of(listaEjemplo));
        ListaResponseDTO resultado = academicoService.obtenerListaPorId(1L);
        assertEquals(10L, resultado.getIdUser());
    }

    @Test
    @DisplayName("obtenerListaPorId() lanza excepción si no encuentra la lista")
    void obtenerListaPorId_noEncontrado() {
        when(listaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RegistroNotFoundException.class, () -> academicoService.obtenerListaPorId(99L));
    }

    // --- TESTS DE CALIFICACIONES ---

    @Test
    @DisplayName("registrarCalificacion() guarda la nota si la lista existe")
    void registrarCalificacion_exito() {
        CalificacionRequestDTO request = new CalificacionRequestDTO();
        request.setIdLista(1L);
        request.setNota(new BigDecimal("7.0"));
        request.setIdCurEva(2L);

        when(listaRepository.findById(1L)).thenReturn(Optional.of(listaEjemplo));
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(calificacionEjemplo);

        CalificacionResponseDTO resultado = academicoService.registrarCalificacion(request);
        assertEquals(new BigDecimal("6.5"), resultado.getNota()); 
    }

    @Test
    @DisplayName("registrarCalificacion() lanza excepcion si la Lista no existe")
    void registrarCalificacion_fallaSiNoExisteLista() {
        CalificacionRequestDTO request = new CalificacionRequestDTO();
        request.setIdLista(99L);
        
        when(listaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RegistroNotFoundException.class, () -> academicoService.registrarCalificacion(request));
    }

    @Test
    @DisplayName("obtenerCalificacionesPorLista() retorna la lista de notas")
    void obtenerCalificacionesPorLista_exito() {
        when(calificacionRepository.buscarPorLista(1L)).thenReturn(List.of(calificacionEjemplo));
        List<CalificacionResponseDTO> resultados = academicoService.obtenerCalificacionesPorLista(1L);
        assertEquals(1L, resultados.get(0).getIdCalificacion());
    }
}