package com.smartCampus.Ms_Evaluacion.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.smartCampus.Ms_Evaluacion.model.*;
import com.smartCampus.Ms_Evaluacion.DTO.EvaluacionRequestDTO;
import com.smartCampus.Ms_Evaluacion.DTO.EvaluacionResponseDTO;
import com.smartCampus.Ms_Evaluacion.repository.EvaluacionRepository;
import com.smartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;

import org.springframework.transaction.annotation.Transactional;



@Service
public class EvaluacionService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluacionService.class);
    private final EvaluacionRepository evaluacionRepository;
    private final TipoEvaluacionRepository tipoEvaluacionRepository;

    public EvaluacionService(EvaluacionRepository evaluacionRepository,
                             TipoEvaluacionRepository tipoEvaluacionRepository) {
        this.evaluacionRepository = evaluacionRepository;
        this.tipoEvaluacionRepository = tipoEvaluacionRepository;
    }

    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> buscarPorTipo(Long tipoId) {
        return evaluacionRepository.findByTipo(tipoId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> buscarPorNombreYPorcentaje(String nombre, Double min) {
        return evaluacionRepository.findByNameAndMinPorcentaje(nombre, min).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> listarTodas() {
        return evaluacionRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EvaluacionResponseDTO buscarPorId(Long id) {
        return evaluacionRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la evaluación con ID: " + id));
    }

    @Transactional
    public EvaluacionResponseDTO crear(EvaluacionRequestDTO dto) {
        if (evaluacionRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalStateException("Ya existe una evaluación con ese nombre");
        }

        TipoEvaluacion tipoEval = tipoEvaluacionRepository.findById(dto.getIdTipoEval())
                .orElseThrow(() -> new IllegalArgumentException("El tipo de evaluación especificado no existe"));

        Evaluacion eval = new Evaluacion();
        eval.setNombre(dto.getNombre());
        eval.setPorcentaje(dto.getPorcentaje());
        eval.setTipoEvaluacion(tipoEval);

        Evaluacion guardada = evaluacionRepository.save(eval);
        logger.info("Evaluación creada correctamente con ID: {}", guardada.getIdEvaluacion());
        return toResponseDTO(guardada);
    }

    @Transactional
    public EvaluacionResponseDTO actualizar(Long id, EvaluacionRequestDTO dto) {
        Evaluacion eval = evaluacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada para actualizar"));

        TipoEvaluacion nuevoTipo = tipoEvaluacionRepository.findById(dto.getIdTipoEval())
                .orElseThrow(() -> new IllegalArgumentException("El tipo de evaluación especificado no existe"));

        if (evaluacionRepository.existsByNameAndTipoExcludingId(dto.getNombre(), nuevoTipo.getIdTipoEval(), id)) {
            throw new IllegalStateException("Conflicto: Ya existe otra evaluación con ese nombre en este tipo");
        }

        eval.setNombre(dto.getNombre());
        eval.setPorcentaje(dto.getPorcentaje());
        eval.setTipoEvaluacion(nuevoTipo);

        Evaluacion actualizada = evaluacionRepository.save(eval);
        logger.info("Evaluación ID: {} actualizada correctamente", id);
        return toResponseDTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!evaluacionRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar, ID inexistente: " + id);
        }
        evaluacionRepository.deleteById(id);
        logger.info("Evaluación ID: {} eliminada correctamente", id);
    }

    private EvaluacionResponseDTO toResponseDTO(Evaluacion e) {
        EvaluacionResponseDTO dto = new EvaluacionResponseDTO();
        dto.setIdEvaluacion(e.getIdEvaluacion());
        dto.setNombre(e.getNombre());
        dto.setPorcentaje(e.getPorcentaje());
        if (e.getTipoEvaluacion() != null) {
            dto.setIdTipoEval(e.getTipoEvaluacion().getIdTipoEval());
            dto.setNombreTipo(e.getTipoEvaluacion().getNombreTipo());
        }
        return dto;
    }
}
