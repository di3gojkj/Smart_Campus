package com.diego.Ms_Gestion_Lista.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.diego.Ms_Gestion_Lista.client.UsuarioClient;
import com.diego.Ms_Gestion_Lista.dto.*;
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

    private static final Logger logger = LoggerFactory.getLogger(AcademicoService.class);
    
    private final ListaRepository listaRepository;
    private final CalificacionRepository calificacionRepository;
    private final UsuarioClient usuarioClient;

    private ListaResponseDTO mapToListaDTO(Lista lista) {
        return new ListaResponseDTO(lista.getIdLista(), lista.getIdUser(), lista.getIdCurso(), lista.getFechaCreacion());
    }

    private CalificacionResponseDTO mapToCalificacionDTO(Calificacion c) {
        return new CalificacionResponseDTO(c.getIdCalificacion(), c.getNota(), c.getLista().getIdLista(), c.getIdCurEva());
    }

    @Transactional(readOnly = true)
    public void verificarUsuarioExistente(Long idUser) {
        logger.info("Validando en red MS-Usuarios la existencia del Docente/Alumno ID: {}", idUser);
        try {
            UsuarioResponseDTO usuario = usuarioClient.obtenerUsuarioPorId(idUser);
            logger.debug("Usuario verificado exitosamente: {} {}", usuario.getNombre(), usuario.getApellido());
        } catch (FeignException.NotFound ex) {
            logger.warn("Rechazo: El usuario ID {} no existe en el sistema central", idUser);
            throw new RuntimeException("El alumno/docente con ID " + idUser + " no existe en el sistema de usuarios.");
        } catch (Exception ex) {
            logger.error("Error crítico de red contactando MS-Usuarios: {}", ex.getMessage());
            throw new RuntimeException("No se pudo verificar el usuario. El MS Usuarios no responde.");
        }
    }

    // --- MÓDULO LISTAS ---

    @Transactional
    public ListaResponseDTO crearLista(ListaRequestDTO dto) {
        logger.info("Solicitud para crear Lista - Usuario ID: {}, Curso ID: {}", dto.getIdUser(), dto.getIdCurso());
        
        verificarUsuarioExistente(dto.getIdUser());
        
        Lista lista = new Lista(null, dto.getIdUser(), dto.getIdCurso(), LocalDateTime.now());
        Lista guardada = listaRepository.save(lista);
        
        logger.info("Registro de Lista creado exitosamente con ID: {}", guardada.getIdLista());
        return mapToListaDTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<ListaResponseDTO> obtenerTodasLasListas() {
        logger.info("Consultando todas las listas académicas");
        return listaRepository.findAll().stream().map(this::mapToListaDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ListaResponseDTO obtenerListaPorId(Long id) {
        logger.info("Buscando Lista con ID: {}", id);
        return listaRepository.findById(id).map(this::mapToListaDTO)
                .orElseThrow(() -> {
                    logger.warn("Lista ID {} no localizada", id);
                    return new RegistroNotFoundException("Registro de Lista no localizado con ID: " + id);
                });
    }

    // --- MÓDULO CALIFICACIONES ---

    @Transactional
    public CalificacionResponseDTO registrarCalificacion(CalificacionRequestDTO dto) {
        logger.info("Registrando calificación ({}) para Lista ID: {}", dto.getNota(), dto.getIdLista());
        
        Lista listaExistente = listaRepository.findById(dto.getIdLista())
                .orElseThrow(() -> {
                    logger.warn("Intento de calificar Lista inexistente ID: {}", dto.getIdLista());
                    return new RegistroNotFoundException("No se puede añadir calificación. La Lista con ID " + dto.getIdLista() + " no existe.");
                });

        Calificacion calificacion = new Calificacion(null, dto.getNota(), listaExistente, dto.getIdCurEva());
        Calificacion guardada = calificacionRepository.save(calificacion);
        
        logger.info("Calificación guardada exitosamente con ID: {}", guardada.getIdCalificacion());
        return mapToCalificacionDTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<CalificacionResponseDTO> obtenerCalificacionesPorLista(Long idLista) {
        logger.info("Consultando calificaciones asociadas a la Lista ID: {}", idLista);
        return calificacionRepository.buscarPorLista(idLista).stream()
                .map(this::mapToCalificacionDTO)
                .collect(Collectors.toList());
    }
}
