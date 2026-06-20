package com.smartCampus.Ms_Evaluacion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartCampus.Ms_Evaluacion.DTO.TipoEvaluacionRequestDTO;
import com.smartCampus.Ms_Evaluacion.DTO.TipoEvaluacionResponseDTO;
import com.smartCampus.Ms_Evaluacion.exception.TipoEvaluacionConflictException;
import com.smartCampus.Ms_Evaluacion.exception.TipoEvaluacionNotFoundException;
import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;
import com.smartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test de integracion: TipoEvaluacionService")
public class TipoEvaluacionServiceTest {

    @Mock
    private TipoEvaluacionRepository repository;

    @InjectMocks
    private TipoEvaluacionService tipoEvaluacionService;

    private TipoEvaluacion tipo;
    private TipoEvaluacionRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        tipo = new TipoEvaluacion(1L, "Certamen", null);
        requestDTO = new TipoEvaluacionRequestDTO();
        requestDTO.setNombreTipo("Certamen");
    }

    @Test
    @DisplayName("listarTodos() retorna la lista de DTO de todos los tipos")
    void listarTodos_debeRetornarListaDeTipos() {
        when(repository.findAll()).thenReturn(List.of(tipo));

        List<TipoEvaluacionResponseDTO> resultado = tipoEvaluacionService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Certamen", resultado.get(0).getNombreTipo());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("listarTodos() retorna lista vacia cuando no hay tipos")
    void listarTodos_debeRetornarListaVacia_SiNoHayTipos() {
        when(repository.findAll()).thenReturn(List.of());

        List<TipoEvaluacionResponseDTO> resultado = tipoEvaluacionService.listarTodos();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarPorId() retorna el DTO mapeado si el tipo existe")
    void buscarPorId_cuandoExiste_debeRetornarTipoDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(tipo));

        TipoEvaluacionResponseDTO resultado = tipoEvaluacionService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Certamen", resultado.getNombreTipo());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId() lanza TipoEvaluacionNotFoundException si el ID no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TipoEvaluacionNotFoundException.class, () -> tipoEvaluacionService.buscarPorId(99L));
        verify(repository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("crear() guarda un nuevo tipo cuando el nombre no existe")
    void crear_debeGuardarYRetornarDTO() {
        when(repository.existsByNombreTipoIgnoreCase("Certamen")).thenReturn(false);
        when(repository.save(any(TipoEvaluacion.class))).thenReturn(tipo);

        TipoEvaluacionResponseDTO resultado = tipoEvaluacionService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Certamen", resultado.getNombreTipo());
        verify(repository, times(1)).save(any(TipoEvaluacion.class));
    }

    @Test
    @DisplayName("crear() lanza TipoEvaluacionConflictException si el nombre ya existe")
    void crear_debeLanzarException_SiNombreYaExiste() {
        when(repository.existsByNombreTipoIgnoreCase("Certamen")).thenReturn(true);

        assertThrows(TipoEvaluacionConflictException.class, () -> tipoEvaluacionService.crear(requestDTO));
        verify(repository, never()).save(any(TipoEvaluacion.class));
    }

    @Test
    @DisplayName("actualizar() modifica el tipo si existe y el nombre no genera conflicto")
    void actualizar_cuandoExiste_debeGuardarYRetornarDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(tipo));
        when(repository.save(any(TipoEvaluacion.class))).thenReturn(tipo);

        TipoEvaluacionResponseDTO resultado = tipoEvaluacionService.actualizar(1L, requestDTO);

        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(TipoEvaluacion.class));
    }

    @Test
    @DisplayName("actualizar() lanza TipoEvaluacionNotFoundException si el ID no existe")
    void actualizar_cuandoNoExiste_debeLanzarException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TipoEvaluacionNotFoundException.class, () -> tipoEvaluacionService.actualizar(99L, requestDTO));
        verify(repository, never()).save(any(TipoEvaluacion.class));
    }

    @Test
    @DisplayName("actualizar() lanza TipoEvaluacionConflictException si el nuevo nombre ya pertenece a otro tipo")
    void actualizar_debeLanzarException_SiNombreYaExisteEnOtroTipo() {
        TipoEvaluacionRequestDTO nuevoDTO = new TipoEvaluacionRequestDTO();
        nuevoDTO.setNombreTipo("Control");

        when(repository.findById(1L)).thenReturn(Optional.of(tipo));
        when(repository.existsByNombreTipoIgnoreCase("Control")).thenReturn(true);

        assertThrows(TipoEvaluacionConflictException.class, () -> tipoEvaluacionService.actualizar(1L, nuevoDTO));
        verify(repository, never()).save(any(TipoEvaluacion.class));
    }

    @Test
    @DisplayName("eliminar() borra el tipo si el ID existe")
    void eliminar_cuandoExiste_debeEjecutarElBorrado() {
        when(repository.existsById(1L)).thenReturn(true);

        tipoEvaluacionService.eliminar(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar() lanza TipoEvaluacionNotFoundException si el ID no existe")
    void eliminar_cuandoNoExiste_debeLanzarException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(TipoEvaluacionNotFoundException.class, () -> tipoEvaluacionService.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }
}
