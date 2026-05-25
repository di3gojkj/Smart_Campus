package com.diego.Ms_Gestion_Estado.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.diego.Ms_Gestion_Estado.dto.EstadoRequestDTO;
import com.diego.Ms_Gestion_Estado.dto.EstadoResponseDTO;
import com.diego.Ms_Gestion_Estado.exception.EstadoNotFoundException;
import com.diego.Ms_Gestion_Estado.model.Estado;
import com.diego.Ms_Gestion_Estado.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private static final Logger logger = LoggerFactory.getLogger(EstadoService.class);
    private final EstadoRepository estadoRepository;
    
    private EstadoResponseDTO mapToDTO(Estado e){
        return new EstadoResponseDTO(e.getIdEstado(), e.getNombre());
    }

    @Transactional(readOnly = true)
    public List<EstadoResponseDTO> obtenerTodos(){
        logger.info("Consultando la lista completa de estados registrados");
        return estadoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstadoResponseDTO obtenerPorId(Long id) {
        logger.info("Buscando estado con ID: {}", id);
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("El estado con ID {} no existe en la base de datos", id);
                    return new EstadoNotFoundException(id);
                });
        return mapToDTO(estado);
    }

    @Transactional
    public EstadoResponseDTO guardar(EstadoRequestDTO dto){
        logger.info("Iniciando creación de nuevo estado: {}", dto.getNombre());
        
        if (estadoRepository.findByNombreIgnoreCase(dto.getNombre()).isPresent()){
            logger.warn("Intento de duplicación fallido: Ya existe el estado '{}'", dto.getNombre());
            throw new RuntimeException("Ya existe un estado con el nombre: " + dto.getNombre());
        }
        
        Estado estado = new Estado(null, dto.getNombre().toUpperCase().trim());
        Estado guardado = estadoRepository.save(estado);
        
        logger.info("Estado '{}' creado exitosamente con ID: {}", guardado.getNombre(), guardado.getIdEstado());
        return mapToDTO(guardado);
    }
}
