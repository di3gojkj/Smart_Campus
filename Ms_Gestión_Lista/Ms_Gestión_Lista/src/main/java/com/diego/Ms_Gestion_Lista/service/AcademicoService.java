package com.diego.Ms_Gestion_Lista.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.diego.Ms_Gestion_Lista.client.UsuarioClient;
import com.diego.Ms_Gestion_Lista.dto.CalificacionRequestDTO;
import com.diego.Ms_Gestion_Lista.dto.CalificacionResponseDTO;
import com.diego.Ms_Gestion_Lista.dto.ListaRequestDTO;
import com.diego.Ms_Gestion_Lista.dto.ListaResponseDTO;
import com.diego.Ms_Gestion_Lista.exception.RegistroNotFoundException;
import com.diego.Ms_Gestion_Lista.model.Calificacion;
import com.diego.Ms_Gestion_Lista.model.Lista;
import com.diego.Ms_Gestion_Lista.repository.CalificacionRepository;
import com.diego.Ms_Gestion_Lista.repository.ListaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicoService {
    private final ListaRepository listaRepository;
    private final CalificacionRepository calificacionRepository;
    private final UsuarioClient usuarioClient;

    // Métodos de mapeo internos
    private ListaResponseDTO mapToListaDTO(Lista lista) {
        return new ListaResponseDTO(lista.getIdLista(), lista.getIdUser(), lista.getIdCurso(), lista.getFechaCreacion());
    }

    private CalificacionResponseDTO mapToCalificacionDTO(Calificacion c) {
        return new CalificacionResponseDTO(c.getIdCalificacion(), c.getNota(), c.getLista().getIdLista(), c.getIdCurEva());
    }

    // Validación distribuida perimetral mediante Feign Client hacia el puerto 8084
    public void verificarUsuarioExistente(Long idUser) {
        try {
            usuarioClient.obtenerUsuarioPorId(idUser);
        } catch (FeignException.NotFound ex) {
            throw new RuntimeException("El alumno/docente con ID " + idUser + " no existe en el sistema de usuarios.");
        } catch (FeignException ex) {
            throw new RuntimeException("No se pudo verificar el usuario. El MS Usuarios no responde.");
        }
    }

    // LÓGICA DE NEGOCIO PARA LISTAS
    @Transactional
    public ListaResponseDTO crearLista(ListaRequestDTO dto) {
        // Validación entre límites de red
        verificarUsuarioExistente(dto.getIdUser());
        
        Lista lista = new Lista(null, dto.getIdUser(), dto.getIdCurso(), LocalDateTime.now());
        return mapToListaDTO(listaRepository.save(lista));
    }

    public List<ListaResponseDTO> obtenerTodasLasListas() {
        return listaRepository.findAll().stream().map(this::mapToListaDTO).collect(Collectors.toList());
    }

    public ListaResponseDTO obtenerListaPorId(Long id) {
        return listaRepository.findById(id).map(this::mapToListaDTO)
                .orElseThrow(() -> new RegistroNotFoundException("Registro de Lista no localizado con ID: " + id));
    }

    // LÓGICA DE NEGOCIO PARA CALIFICACIONES
    @Transactional
    public CalificacionResponseDTO registrarCalificacion(CalificacionRequestDTO dto) {
        Lista listaExistente = listaRepository.findById(dto.getIdLista())
                .orElseThrow(() -> new RegistroNotFoundException("No se puede añadir calificación. La Lista con ID " + dto.getIdLista() + " no existe."));

        Calificacion calificacion = new Calificacion(null, dto.getNota(), listaExistente, dto.getIdCurEva());
        return mapToCalificacionDTO(calificacionRepository.save(calificacion));
    }

    public List<CalificacionResponseDTO> obtenerCalificacionesPorLista(Long idLista) {
        return calificacionRepository.buscarPorLista(idLista).stream()
                .map(this::mapToCalificacionDTO)
                .collect(Collectors.toList());
    }

}
