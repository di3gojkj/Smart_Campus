package com.diego.Ms_Gestion_Estado.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.diego.Ms_Gestion_Estado.dto.EstadoRequestDTO;
import com.diego.Ms_Gestion_Estado.dto.EstadoResponseDTO;
import com.diego.Ms_Gestion_Estado.exception.EstadoNotFoundException;
import com.diego.Ms_Gestion_Estado.model.Estado;
import com.diego.Ms_Gestion_Estado.repository.EstadoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor//Inyector de dependencias por constructor, remplaza el @Autowired
public class EstadoService {
    private final EstadoRepository estadoRepository;
    
    //Mapeo de entidad a DTO
    private EstadoResponseDTO mapToDTO(Estado e){
        return new EstadoResponseDTO(e.getIdEstado(), e.getNombre());
    }

    public List<EstadoResponseDTO> obtenerTodos(){
        return estadoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public EstadoResponseDTO obtenerPorId(Long id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new EstadoNotFoundException(id));
        return mapToDTO(estado);
    }

    public EstadoResponseDTO guardar(EstadoRequestDTO dto){
        if (estadoRepository.findByNombreIgnoreCase(dto.getNombre()).isPresent()){//Antes de guardar, va a la base de datos a preguntar si ya existe ese nombre. Si existe, lanza un error para no tener la base de datos sucia.
            throw new RuntimeException("Ya existe un estado con el nombre: " + dto.getNombre());
        }
        Estado estado = new Estado(null, dto.getNombre().toUpperCase());
        return mapToDTO(estadoRepository.save(estado));
    }
    

}
