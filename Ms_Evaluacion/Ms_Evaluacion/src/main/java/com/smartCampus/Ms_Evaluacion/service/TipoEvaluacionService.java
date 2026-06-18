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
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
public TipoEvaluacionResponseDTO buscarPorId(Long id) {
    return repository.findById(id)
            .map(this::toResponseDTO)
            .orElseThrow(() -> new RuntimeException("El tipo de evaluación especificado no existe con ID: " + id));
}

    @Transactional
    public TipoEvaluacionResponseDTO crear(TipoEvaluacionRequestDTO dto) {
        if (repository.existsByNombreTipoIgnoreCase(dto.getNombreTipo())) {
            throw new RuntimeException("El nombre del tipo de evaluación ya existe");
        }

        TipoEvaluacion tipo = new TipoEvaluacion();
        tipo.setNombreTipo(dto.getNombreTipo());

        TipoEvaluacion guardado = repository.save(tipo);
        logger.info("Tipo de evaluación creado correctamente con ID: {}", guardado.getIdTipoEval());
        return toResponseDTO(guardado);
    }

    @Transactional
    public TipoEvaluacionResponseDTO actualizar(Long id, TipoEvaluacionRequestDTO dto) {
        TipoEvaluacion tipo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el tipo de evaluación a actualizar"));

        if (!tipo.getNombreTipo().equalsIgnoreCase(dto.getNombreTipo())
             && repository.existsByNombreTipoIgnoreCase(dto.getNombreTipo())) {
            throw new IllegalStateException("El nombre ya está ocupado por otro tipo de evaluación");
        }

        tipo.setNombreTipo(dto.getNombreTipo());
        TipoEvaluacion actualizado = repository.save(tipo);
        logger.info("Tipo de evaluación ID: {} actualizado correctamente", id);
        return toResponseDTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, ID inexistente: " + id);
        }
        repository.deleteById(id);
        logger.info("Tipo de evaluación ID: {} eliminado correctamente", id);
    }

    private TipoEvaluacionResponseDTO toResponseDTO(TipoEvaluacion t) {
        TipoEvaluacionResponseDTO dto = new TipoEvaluacionResponseDTO();
        dto.setIdTipoEval(t.getIdTipoEval());
        dto.setNombreTipo(t.getNombreTipo());
        return dto;
    }
}
