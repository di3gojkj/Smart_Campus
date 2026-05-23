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
    public List<EvaluacionResponseDTO> buscarPorTipo(Long Tipoid) {
        logger.info("Buscando evaluaciones por tipo ID: {}", Tipoid);
        // Ahora sí usamos el método del repositorio que acabamos de crear
        return evaluacionRepository.findByTipo(Tipoid).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> buscarFiltrado(String nombre, Double minPorcentaje) {
        logger.info("Buscando evaluaciones con nombre: {} y minPorcentaje: {}", nombre, minPorcentaje);
        return evaluacionRepository.findByNameAndMinPorcentaje(nombre, minPorcentaje).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // --- CRUD ESTÁNDAR ---

    @Transactional(readOnly = true)
    public List<EvaluacionResponseDTO> listarTodas() {
        logger.info("Listando todas las evaluaciones...");
        return evaluacionRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EvaluacionResponseDTO buscarPorId(Long id) {
        logger.info("Buscando evaluación con ID: {}", id);
        return evaluacionRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada con ID: " + id));
    }

    @Transactional
    public EvaluacionResponseDTO crear(EvaluacionRequestDTO dto) {
        logger.info("Creando nueva evaluación: {}", dto.getNombre());
        
        TipoEvaluacion tipo = tipoEvaluacionRepository.findById(dto.getIdTipoEval())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de evaluación no existe: " + dto.getIdTipoEval()));

        if (evaluacionRepository.existsByNameAndTipoExcludingId(dto.getNombre(), tipo.getIdTipoEval(), 0L)) {
            logger.warn("Intento de crear evaluación duplicada: {}", dto.getNombre());
            throw new IllegalStateException("Ya existe una evaluación con ese nombre para este tipo");
        }

        Evaluacion eval = new Evaluacion();
        eval.setNombre(dto.getNombre());
        eval.setPorcentaje(dto.getPorcentaje());
        eval.setTipoEvaluacion(tipo);

        Evaluacion guardada = evaluacionRepository.save(eval);
        logger.info("Evaluación creada exitosamente con ID: {}", guardada.getId_Evaluacion());
        return toResponseDTO(guardada);
    }

    @Transactional
    public EvaluacionResponseDTO actualizar(Long id, EvaluacionRequestDTO dto) {
        logger.info("Actualizando evaluación con ID: {}", id);
        
        Evaluacion eval = evaluacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada para actualizar"));

        TipoEvaluacion nuevoTipo = tipoEvaluacionRepository.findById(dto.getIdTipoEval())
                .orElseThrow(() -> new IllegalArgumentException("Tipo inválido"));

        if (evaluacionRepository.existsByNameAndTipoExcludingId(dto.getNombre(), nuevoTipo.getIdTipoEval(), id)) {
            throw new IllegalStateException("Conflicto: Ya existe otra evaluación con ese nombre");
        }

        eval.setNombre(dto.getNombre());
        eval.setPorcentaje(dto.getPorcentaje());
        eval.setTipoEvaluacion(nuevoTipo);

        return toResponseDTO(evaluacionRepository.save(eval));
    }

    @Transactional
    public void eliminar(Long id) {
        logger.info("Eliminando evaluación con ID: {}", id);
        if (!evaluacionRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar, ID inexistente: " + id);
        }
        evaluacionRepository.deleteById(id);
        logger.info("Evaluación eliminada correctamente");
    }

    private EvaluacionResponseDTO toResponseDTO(Evaluacion e) {
        EvaluacionResponseDTO dto = new EvaluacionResponseDTO();
        dto.setId_Evaluacion(e.getId_Evaluacion());
        dto.setNombre(e.getNombre());
        dto.setPorcentaje(e.getPorcentaje());
        dto.setIdTipoEval(e.getTipoEvaluacion().getIdTipoEval());
        dto.setNombreTipo(e.getTipoEvaluacion().getNombreTipo());
        return dto;
    }
}
