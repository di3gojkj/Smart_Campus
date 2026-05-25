package com.SCampus.curso_seccion.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    public List<Seccion> obtenerTodas() {
        logger.info("Consultando listado completo de secciones académicas activas");
        return seccionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Seccion> obtenerPorId(Long id) {
        logger.info("Buscando sección por ID: {}", id);
        return seccionRepository.findById(id);
    }

    @Transactional
    public Seccion guardar(Seccion seccion) {
        logger.info("Procesando almacenamiento/actualización de sección: {}", seccion.getNombre());
        
        // Validar que el curso lógico asignado exista antes de permitir inyectar la sección
        if (!cursoRepository.existsById(seccion.getIdCurso())) {
            logger.warn("Error de Integridad Lógica: No existe el curso ID {} para la sección {}", seccion.getIdCurso(), seccion.getNombre());
            throw new RuntimeException("Operación Cancelada: El ID de Curso asignado no existe.");
        }

        return seccionRepository.save(seccion);
    }

    @Transactional
    public void eliminar(Long id) {
        logger.info("Solicitud para eliminar la sección ID: {}", id);
        seccionRepository.deleteById(id);
        logger.info("Sección ID {} removida con éxito de los registros físicos", id);
    }
}
