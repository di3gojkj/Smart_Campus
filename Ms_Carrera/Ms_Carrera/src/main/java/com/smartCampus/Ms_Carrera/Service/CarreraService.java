package com.smartCampus.Ms_Carrera.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartCampus.Ms_Carrera.Client.EstadoClient;
import com.smartCampus.Ms_Carrera.DTO.CarreraRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraResponseDTO;
import com.smartCampus.Ms_Carrera.Exception.CarreraConflictException;
import com.smartCampus.Ms_Carrera.Exception.CarreraNotFoundException;
import com.smartCampus.Ms_Carrera.Repository.CarreraRepository;
import com.smartCampus.Ms_Carrera.model.Carrera;



@Service
public class CarreraService {

    private static final Logger logger = LoggerFactory.getLogger(CarreraService.class);

    private final CarreraRepository carreraRepository;
    private final EstadoClient estadoClient;

    public CarreraService(CarreraRepository carreraRepository,
        EstadoClient estadoClient){
        this.carreraRepository = carreraRepository;
        this.estadoClient = estadoClient;
    }

    
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> listarTodas() {
        return carreraRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CarreraResponseDTO buscarPorId(Long id) {
        return carreraRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new CarreraNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> listarPorEstado(Long idEstado) {
        return carreraRepository.findAll().stream()
                .filter(c -> c.getIdEstado().equals(idEstado))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CarreraResponseDTO crear(CarreraRequestDTO dto) {

        
        if (carreraRepository.findBySigla(dto.getSigla()).isPresent()) {
            throw new IllegalArgumentException("La sigla '" + dto.getSigla() + "' ya existe.");
        }

        Carrera carrera = carreraRepository.save(mapearAEntidad(dto));
        logger.info("Carrera creada con ID: {}", carrera.getIdCarrera());
        return toResponseDTO(carrera);
    }

    @Transactional
    public CarreraResponseDTO actualizar(Long id, CarreraRequestDTO dto) {

        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> 
                    new CarreraNotFoundException(id));

        if (carreraRepository.findBySiglaExcludingCurrent(dto.getSigla(), id).isPresent()) {
            throw new CarreraConflictException("La sigla '" + dto.getSigla() + "' ya pertenece a otra carrera.");
        }

        carrera.setNombre(dto.getNombre());
        carrera.setSigla(dto.getSigla());
        carrera.setIdEstado(dto.getIdEstado());
        
        Carrera actualizada = carreraRepository.save(carrera);
        logger.info("Carrera ID: {} actualizada", id);
        return toResponseDTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!carreraRepository.existsById(id)) {
            throw new CarreraNotFoundException(id);
        }
        carreraRepository.deleteById(id);
        logger.info("Carrera ID: {} eliminada", id);
    }


    private CarreraResponseDTO toResponseDTO(Carrera c) {
        CarreraResponseDTO dto = new CarreraResponseDTO();
        dto.setIdCarrera(c.getIdCarrera());
        dto.setNombre(c.getNombre());
        dto.setSigla(c.getSigla());
        dto.setIdEstado(c.getIdEstado());
        return dto;
    }

    private Carrera mapearAEntidad(CarreraRequestDTO dto) {
        Carrera c = new Carrera();
        c.setNombre(dto.getNombre());
        c.setSigla(dto.getSigla());
        c.setIdEstado(dto.getIdEstado());
        return c;
    }
}
