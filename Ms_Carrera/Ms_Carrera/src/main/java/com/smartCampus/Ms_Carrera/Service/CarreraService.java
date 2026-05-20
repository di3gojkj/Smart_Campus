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
import com.smartCampus.Ms_Carrera.DTO.EstadoResponseDTO;
import com.smartCampus.Ms_Carrera.Repository.CarreraRespository;
import com.smartCampus.Ms_Carrera.model.Carrera;



@Service
public class CarreraService {

    private static final Logger logger = LoggerFactory.getLogger(CarreraService.class);

    private final CarreraRespository carreraRespository;
    private final EstadoClient estadoClient;

    public CarreraService(CarreraRespository carreraRespository,
        EstadoClient estadoClient){
        this.carreraRespository = carreraRespository;
        this.estadoClient = estadoClient;
    }

    
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> listarTodas() {
        logger.info("Listando todas las carreras");
        return carreraRespository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CarreraResponseDTO buscarPorId(Long id) {
        logger.info("Buscando carrera con ID: {}", id);
        Carrera carrera = carreraRespository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Carrera no encontrada con ID: {}", id);
                    return new RuntimeException("Carrera no encontrada con ID: " + id);
                });
        return toResponseDTO(carrera);
    }

    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> buscarPorFiltro(String filtro) {
        logger.info("Buscando carreras con filtro: {}", filtro);
        return carreraRespository.buscarPorNombreOSigla(filtro).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CarreraResponseDTO crear(CarreraRequestDTO dto) {
        logger.info("Creando nueva carrera: {} [{}]", dto.getNombre(), dto.getSigla());

        // Usando tu método real de interfaz
        if (carreraRespository.findBySigla(dto.getSigla()).isPresent()) {
            logger.warn("Intento de duplicación de sigla: {}", dto.getSigla());
            throw new IllegalArgumentException("La sigla '" + dto.getSigla() + "' ya existe.");
        }

        Carrera carrera = mapearAEntidad(dto);
        Carrera guardada = carreraRespository.save(carrera);
        logger.info("Carrera creada exitosamente con ID: {}", guardada.getIdCarrera());

        return toResponseDTO(guardada);
    }

    @Transactional
    public CarreraResponseDTO actualizar(Long id, CarreraRequestDTO dto) {
        logger.info("Actualizando carrera con ID: {}", id);

        Carrera carrera = carreraRespository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se pudo actualizar. Carrera ID: {} no existe", id);
                    return new RuntimeException("Carrera no encontrada con ID: " + id);
                });

        // Usando tu método real para excluir el actual en la validación
        carreraRespository.findBySiglaExcludingCurrent(dto.getSigla(), id)
                .ifPresent(existente -> {
                    logger.warn("Colisión de sigla: '{}' ya pertenece a otra carrera", dto.getSigla());
                    throw new IllegalArgumentException("La sigla '" + dto.getSigla() + "' ya pertenece a otra carrera.");
                });

        carrera.setNombre(dto.getNombre());
        carrera.setSigla(dto.getSigla());
        carrera.setIdEstado(dto.getIdEstado());

        Carrera actualizada = carreraRespository.save(carrera);
        logger.info("Carrera ID: {} actualizada exitosamente", id);

        return toResponseDTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        logger.info("Eliminando carrera con ID: {}", id);
        if (!carreraRespository.existsById(id)) {
            logger.warn("No se pudo eliminar. Carrera ID: {} no existe", id);
            throw new RuntimeException("No se puede eliminar. Carrera no encontrada con ID: " + id);
        }
        carreraRespository.deleteById(id);
        logger.info("Carrera ID: {} eliminada correctamente", id);
    }


    private CarreraResponseDTO toResponseDTO(Carrera c) {
        CarreraResponseDTO dto = new CarreraResponseDTO();
        dto.setIdCarrera(c.getIdCarrera());
        dto.setNombre(c.getNombre());
        dto.setSigla(c.getSigla());
        dto.setIdEstado(c.getIdEstado());

        // Mapeo inteligente con protección de fallo Feign
        try {
            if (c.getIdEstado() != null) {
                EstadoResponseDTO estado = estadoClient.obtenerEstadoPorId(c.getIdEstado());
                dto.setNombre(estado.getNombre());
            }
        } catch (Exception e) {
            logger.error("Error al conectar con MS Gestión Estado para el ID: {}. Error: {}", 
                         c.getIdEstado(), e.getMessage());
            dto.setNombre("Estado no disponible");
        }

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
