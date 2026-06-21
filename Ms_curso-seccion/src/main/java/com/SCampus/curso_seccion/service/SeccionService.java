package com.SCampus.curso_seccion.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SCampus.curso_seccion.client.CarreraAsignaturaClient;
import com.SCampus.curso_seccion.dto.CarreraAsignaturaResponseDTO;
import com.SCampus.curso_seccion.dto.SeccionResponseDTO;
import com.SCampus.curso_seccion.model.Seccion;
import com.SCampus.curso_seccion.repository.CursoRepository;
import com.SCampus.curso_seccion.repository.SeccionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeccionService {

    private static final Logger logger = LoggerFactory.getLogger(SeccionService.class);
    
    private final SeccionRepository seccionRepository;
    private final CursoRepository cursoRepository;
    private final CarreraAsignaturaClient carreraAsignaturaClient; // Inyección de Feign

    // Mapeador institucional que orquesta el enriquecimiento dinámico
    private SeccionResponseDTO mapToResponseDTO(Seccion seccion) {
        SeccionResponseDTO dto = new SeccionResponseDTO();
        dto.setId(seccion.getId());
        dto.setNombre(seccion.getNombre());
        dto.setCursoId(seccion.getCurso() != null ? seccion.getCurso().getId() : null);
        dto.setFechaCreacion(LocalDateTime.now()); // Simulación temporal

        // Llamada de integración sincrónica usando Feign para poblar la metadata externa
        try {
            if (seccion.getCurso() != null) {
                // Buscamos las asignaturas vinculadas a la carrera de referencia (ejemplo: Carrera ID 1)
                List<CarreraAsignaturaResponseDTO> externas = carreraAsignaturaClient.listarPorCarrera(1L);
                externas.stream()
                        .findFirst()
                        .ifPresent(dto::setDatosAcademicos);
            }
        } catch (Exception e) {
            logger.error("Fallo de comunicación sincrónica con Ms_Carrera: {}", e.getMessage());
            dto.setDatosAcademicos(null); // Resiliencia: se entrega el payload sin tumbar la API local
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public List<SeccionResponseDTO> obtenerTodasEnriquecidas() {
        logger.info("Consultando listado consolidado y enriquecido de secciones");
        return seccionRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SeccionResponseDTO guardarEnriquecido(Seccion seccion, Long idCarreraVerificar) {
        logger.info("Procesando almacenamiento perimetral de sección: {}", seccion.getNombre());
        
        // 1. Validación Lógica Local
        Long cursoId = (seccion.getCurso() != null) ? seccion.getCurso().getId() : null;
        if (cursoId == null || !cursoRepository.existsById(cursoId)) {
            throw new RuntimeException("Operación Cancelada: El ID de Curso asignado no existe localmente.");
        }

        // 2. Validación Cruzada Distribuida (Integridad Remota)
        try {
            logger.info("Validando existencia de la Carrera ID {} vía Feign", idCarreraVerificar);
            List<CarreraAsignaturaResponseDTO> relaciones = carreraAsignaturaClient.listarPorCarrera(idCarreraVerificar);
            if (relaciones == null || relaciones.isEmpty()) {
                throw new RuntimeException("Restricción Académica: La Carrera indicada no posee asignaturas asignadas.");
            }
        } catch (Exception e) {
            logger.error("Bloqueo Preventivo: Ms_Carrera inaccesible o ID inválido. Razón: {}", e.getMessage());
            throw new RuntimeException("Error Distribuido: No se pudo verificar la integridad de la asignatura remota.");
        }

        Seccion guardada = seccionRepository.save(seccion);
        return mapToResponseDTO(guardada);
    }

    @Transactional(readOnly = true)
    public Optional<SeccionResponseDTO> obtenerPorIdEnriquecido(Long id) {
        return seccionRepository.findById(id).map(this::mapToResponseDTO);
    }

    @Transactional
    public void eliminar(Long id) {
        seccionRepository.deleteById(id);
    }
}