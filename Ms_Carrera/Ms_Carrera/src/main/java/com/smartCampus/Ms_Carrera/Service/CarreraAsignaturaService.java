package com.smartCampus.Ms_Carrera.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartCampus.Ms_Carrera.Client.AsignaturaClient;
import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaResponseDTO;
import com.smartCampus.Ms_Carrera.Repository.CarreraAsignaturaRepository;
import com.smartCampus.Ms_Carrera.Repository.CarreraRespository;
import com.smartCampus.Ms_Carrera.model.Carrera;
import com.smartCampus.Ms_Carrera.model.CarreraAsignatura;

@Service
public class CarreraAsignaturaService {

    private static final Logger logger = LoggerFactory.getLogger(CarreraAsignaturaService.class);

    private final CarreraAsignaturaRepository repository;
    private final CarreraRespository carreraRepository;
    private final AsignaturaClient asignaturaClient;

    public CarreraAsignaturaService(CarreraAsignaturaRepository repository,
                                    CarreraRespository carreraRepository,
                                    AsignaturaClient asignaturaClient
                                    ) {
        this.repository = repository;
        this.carreraRepository = carreraRepository;
        this.asignaturaClient = asignaturaClient;
    }

    @Transactional(readOnly = true)
    public List<CarreraAsignaturaResponseDTO> listarPorCarrera(Long idCarrera) {
        logger.info("Listando asignaturas para la carrera ID: {}", idCarrera);
        return repository.findByCarrera_IdCarrera(idCarrera).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CarreraAsignaturaResponseDTO crear(CarreraAsignaturaRequestDTO dto) {
        logger.info("Asignando asignatura {} a carrera {} en semestre {}", 
                     dto.getIdAsignatura(), dto.getIdCarrera(), dto.getIdSemestre());

        // Validamos que no sea un duplicado
        if (repository.existsByCarrera_IdCarreraAndIdAsignaturaAndIdSemestre(
                dto.getIdCarrera(), dto.getIdAsignatura(), dto.getIdSemestre())) {
            throw new IllegalArgumentException("Conflicto: Esta asignatura ya está asignada a esta carrera en este semestre.");
        }

        // Buscamos la carrera local para la relación ManyToOne
        Carrera carrera = carreraRepository.findById(dto.getIdCarrera())
                .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada con ID: " + dto.getIdCarrera()));

        CarreraAsignatura ca = new CarreraAsignatura();
        ca.setCarrera(carrera);
        ca.setIdAsignatura(dto.getIdAsignatura());
        ca.setIdSemestre(dto.getIdSemestre());

        return toResponseDTO(repository.save(ca));
    }

    @Transactional
    public CarreraAsignaturaResponseDTO actualizar(Long id, CarreraAsignaturaRequestDTO dto) {
        logger.info("Actualizando relación Carrera-Asignatura con ID: {}", id);
        
        CarreraAsignatura existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la relación con ID: " + id));

        // Actualizamos los campos necesarios
        existente.setIdAsignatura(dto.getIdAsignatura());
        existente.setIdSemestre(dto.getIdSemestre());

        // Si necesitas cambiar la carrera asociada (poco común pero posible):
        if (!existente.getCarrera().getIdCarrera().equals(dto.getIdCarrera())) {
             var nuevaCarrera = carreraRepository.findById(dto.getIdCarrera())
                     .orElseThrow(() -> new IllegalArgumentException("Carrera no encontrada"));
             existente.setCarrera(nuevaCarrera);
        }

        return toResponseDTO(repository.save(existente));
    }

    @Transactional
    public void eliminar(Long id) {
        logger.info("Eliminando relación Carrera-Asignatura ID: {}", id);
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar, ID inexistente: " + id);
        }
        repository.deleteById(id);
        logger.info("Relación eliminada exitosamente");
    }

    private CarreraAsignaturaResponseDTO toResponseDTO(CarreraAsignatura ca) {
        CarreraAsignaturaResponseDTO dto = new CarreraAsignaturaResponseDTO();
        dto.setIdCarreraAsignatura(ca.getIdCarreraAsignatura());
        dto.setIdCarrera(ca.getCarrera().getIdCarrera());
        dto.setIdAsignatura(ca.getIdAsignatura());
        dto.setIdSemestre(ca.getIdSemestre());

        // Enriquecimiento (Resiliencia si fallan otros MS)
        try {
            var asignatura = asignaturaClient.obtenerAsignaturaPorId(ca.getIdAsignatura());
            dto.setNombreAsignatura(asignatura.getNombre());
        } catch (Exception e) {
            logger.error("Error al consultar MS Asignatura (ID: {}): {}", ca.getIdAsignatura(), e.getMessage());
            dto.setNombreAsignatura("Nombre no disponible");
        }

        try {
            
        } catch (Exception e) {
            logger.error("Error al consultar MS Semestre (ID: {}): {}", ca.getIdSemestre(), e.getMessage());
            dto.setNombreSemestre("Nombre no disponible");
        }

        return dto;
    }
}
