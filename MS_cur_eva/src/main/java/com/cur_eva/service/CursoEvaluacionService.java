package com.cur_eva.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import feign.FeignException; 

import com.cur_eva.client.EvaluacionClient; 
import com.cur_eva.client.CursoClient; 
import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.dto.EvaluacionResponseDTO;
import com.cur_eva.dto.TipoEvaluacionResponseDTO;
import com.cur_eva.dto.CursoResponseDTO;
import com.cur_eva.model.CursoEvaluacion;
import com.cur_eva.repository.CursoEvaluacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoEvaluacionService {
    
    private static final Logger logger = LoggerFactory.getLogger(CursoEvaluacionService.class);

    private final CursoEvaluacionRepository cursoEvaluacionRepository;
    private final EvaluacionClient evaluacionClient; 
    private final CursoClient cursoClient; 
    
  
    private CursoEvaluacionResponseDTO toResponseDTO(CursoEvaluacion c) {
        CursoEvaluacionResponseDTO dto = new CursoEvaluacionResponseDTO();
        
        dto.setIdCursoEvaluacion(c.getIdCursoEvaluacion());
        dto.setIdCurso(c.getIdCurso());
        dto.setIdEvaluacion(c.getIdEvaluacion());
        dto.setNombre(c.getNombre());
        dto.setFApertura(c.getFApertura());
        dto.setFCierre(c.getFCierre());

        
        try {
            if (c.getIdEvaluacion() != null) {
                EvaluacionResponseDTO evaluacion = evaluacionClient.buscarPorId(c.getIdEvaluacion());
                if (evaluacion != null) {
                    dto.setNombreEvaluacion(evaluacion.getNombre());
                    dto.setPorcentajeEvaluacion(evaluacion.getPorcentaje());
                    dto.setIdTipoEval(evaluacion.getIdTipoEval());
                    
                    if (evaluacion.getIdTipoEval() != null) {
                        TipoEvaluacionResponseDTO tipo = evaluacionClient.buscarTipoPorId(evaluacion.getIdTipoEval());
                        if (tipo != null) {
                            dto.setNombreTipoEvaluacion(tipo.getNombreTipo());
                        }
                    }
                }
            }

            
            if (c.getIdCurso() != null) {
                CursoResponseDTO cursoRemoto = cursoClient.buscarCursoPorId(c.getIdCurso());
                if (cursoRemoto != null) {
                    dto.setNombreCurso(cursoRemoto.getNombre());
                    dto.setFechaCreacionCurso(cursoRemoto.getFechaCreacion());
                }
            }

        } catch (Exception e) {
            logger.error("Fallo parcial en el bloque de enriquecimiento distribuido: {}", e.getMessage());
            if (dto.getNombreEvaluacion() == null) dto.setNombreEvaluacion("Nombre no disponible");
            if (dto.getNombreTipoEvaluacion() == null) dto.setNombreTipoEvaluacion("Tipo no disponible");
            if (dto.getNombreCurso() == null) dto.setNombreCurso("Curso no disponible");
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public List<CursoEvaluacionResponseDTO> obtenerTodos() {
        logger.info("Consultando el listado completo y enriquecido de CursoEvaluacion");
        return cursoEvaluacionRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CursoEvaluacionResponseDTO obtenerPorId(Long id) {
        logger.info("Buscando CursoEvaluacion consolidado por ID: {}", id);
        CursoEvaluacion cursoEvaluacion = cursoEvaluacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el registro CursoEvaluacion con el ID especificado."));
        return toResponseDTO(cursoEvaluacion);
    }

    @Transactional
    public CursoEvaluacionResponseDTO guardar(CursoEvaluacionRequestDTO dto) {
        logger.info("Procesando solicitud de alta para CursoEvaluacion: {}", dto.getNombre());
        
        if (cursoEvaluacionRepository.findByNombreIgnoreCase(dto.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un CursoEvaluacion con el nombre: " + dto.getNombre());
        }

        // 🛠️ VALIDACIÓN PERIMETRAL DISTRIBUIDA 1: Validar integridad en curso_seccion
        if (dto.getIdCurso() != null) {
            try {
                logger.info("Validando existencia del ID de curso remoto en curso_seccion: {}", dto.getIdCurso());
                cursoClient.buscarCursoPorId(dto.getIdCurso());
            } catch (FeignException.NotFound e) {
                throw new RuntimeException("Error de Consistencia: El Curso con ID " + dto.getIdCurso() + " no existe en el sistema remoto de curso_seccion.");
            } catch (FeignException e) {
                throw new RuntimeException("El microservicio curso_seccion no se encuentra disponible temporalmente.");
            }
        }

        
        if (dto.getIdEvaluacion() != null) {
            try {
                logger.info("Validando existencia del ID de evaluación remota en Ms_Evaluacion: {}", dto.getIdEvaluacion());
                evaluacionClient.buscarPorId(dto.getIdEvaluacion());
            } catch (FeignException.NotFound e) {
                throw new RuntimeException("Error de Consistencia: La evaluación con ID " + dto.getIdEvaluacion() + " no existe en el sistema remoto.");
            } catch (FeignException e) {
                throw new RuntimeException("El servicio de evaluaciones no se encuentra disponible temporalmente.");
            }
        }

        CursoEvaluacion cursoEvaluacion = new CursoEvaluacion();
        cursoEvaluacion.setNombre(dto.getNombre().toUpperCase());
        cursoEvaluacion.setIdCurso(dto.getIdCurso());
        cursoEvaluacion.setIdEvaluacion(dto.getIdEvaluacion());
        cursoEvaluacion.setFApertura(dto.getFApertura());
        cursoEvaluacion.setFCierre(dto.getFCierre());
        cursoEvaluacion.setFCreacion("2026-06-21"); 

        CursoEvaluacion guardado = cursoEvaluacionRepository.save(cursoEvaluacion);
        logger.info("CursoEvaluacion registrado con éxito localmente con ID: {}", guardado.getIdCursoEvaluacion());
        
        return toResponseDTO(guardado);
    }
}

