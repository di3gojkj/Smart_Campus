package com.smartCampus.Ms_Evaluacion.service;

import org.springframework.stereotype.Service;

import com.smartCampus.Ms_Evaluacion.DTO.TipoEvaluacionRequestDTO;
import com.smartCampus.Ms_Evaluacion.DTO.TipoEvaluacionResponseDTO;
import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;
import com.smartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoEvaluacionService {

    private static final Logger logger = LoggerFactory.getLogger(TipoEvaluacionService.class);

    private final TipoEvaluacionRepository repository;

    public TipoEvaluacionService(TipoEvaluacionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TipoEvaluacionResponseDTO> listarTodos() {
        logger.info("Listando todos los tipos de evaluación");
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoEvaluacionResponseDTO buscarPorId(Long id) {
        logger.info("Buscando tipo de evaluación con ID: {}", id);
        return repository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de evaluación no encontrado con ID: "
                + id));
    }

    @Transactional
    public TipoEvaluacionResponseDTO crear(TipoEvaluacionRequestDTO dto) {
        logger.info("Creando nuevo tipo de evaluación: {}", dto.getNombreTipo());

        if (repository.existsByNombreTipoIgnoreCase(dto.getNombreTipo())) {
            throw new IllegalStateException("Ya existe un tipo de evaluación con el nombre: "
             + dto.getNombreTipo());
        }

        TipoEvaluacion tipo = new TipoEvaluacion();
        tipo.setNombreTipo(dto.getNombreTipo());

        TipoEvaluacion guardado = repository.save(tipo);
        logger.info("Tipo de evaluación creado con éxito, ID: {}", guardado.getIdTipoEval());
        return toResponseDTO(guardado);
    }

    @Transactional
    public TipoEvaluacionResponseDTO actualizar(Long id, TipoEvaluacionRequestDTO dto) {
        logger.info("Actualizando tipo de evaluación ID: {}", id);

        TipoEvaluacion tipo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el tipo de evaluación a actualizar"));

        // Verificamos duplicado solo si el nombre cambió
        if (!tipo.getNombreTipo().equalsIgnoreCase(dto.getNombreTipo())
             && repository.existsByNombreTipoIgnoreCase(dto.getNombreTipo())) {
            throw new IllegalStateException("El nombre ya está ocupado por otro tipo de evaluación");
        }

        tipo.setNombreTipo(dto.getNombreTipo());
        return toResponseDTO(repository.save(tipo));
    }

    @Transactional
    public void eliminar(Long id) {
        logger.info("Eliminando tipo de evaluación ID: {}", id);
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar, ID inexistente: " + id);
        }
        repository.deleteById(id);
        logger.info("Tipo de evaluación eliminado correctamente");
    }

    private TipoEvaluacionResponseDTO toResponseDTO(TipoEvaluacion t) {
        TipoEvaluacionResponseDTO dto = new TipoEvaluacionResponseDTO();
        dto.setIdTipoEval(t.getIdTipoEval());
        dto.setNombreTipo(t.getNombreTipo());
        return dto;
    }
}
