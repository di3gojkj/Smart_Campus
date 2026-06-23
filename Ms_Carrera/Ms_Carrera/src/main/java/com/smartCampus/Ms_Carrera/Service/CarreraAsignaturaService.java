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
import com.smartCampus.Ms_Carrera.Exception.CarreraNotFoundException;
import com.smartCampus.Ms_Carrera.Exception.CarreraAsignaturaConflictException;
import com.smartCampus.Ms_Carrera.Exception.CarreraAsignaturaNotFoundException;
import com.smartCampus.Ms_Carrera.Repository.CarreraAsignaturaRepository;
import com.smartCampus.Ms_Carrera.Repository.CarreraRepository;
import com.smartCampus.Ms_Carrera.model.CarreraAsignatura;

@Service
public class CarreraAsignaturaService {

    private static final Logger logger = LoggerFactory.getLogger(CarreraAsignaturaService.class);

    private final CarreraAsignaturaRepository repository;
    private final CarreraRepository carreraRepository;
    private final AsignaturaClient asignaturaClient;

    public CarreraAsignaturaService(CarreraAsignaturaRepository repository,
                                    CarreraRepository carreraRepository,
                                    AsignaturaClient asignaturaClient
                                    ) {
        this.repository = repository;
        this.carreraRepository = carreraRepository;
        this.asignaturaClient = asignaturaClient;
    }

    @Transactional(readOnly = true)
    public List<CarreraAsignaturaResponseDTO> listarTodas(Long idCarrera) {

        List<CarreraAsignatura> lista = repository.findByCarrera_IdCarrera(idCarrera);

        if (lista.isEmpty()) {
            
            return List.of();
        }
        return lista.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CarreraAsignaturaResponseDTO crear(CarreraAsignaturaRequestDTO dto) {
    
        boolean existe = repository.existsByCarrera_IdCarreraAndIdAsignaturaAndIdSemestre(
                            dto.getIdCarrera(), dto.getIdAsignatura(), dto.getIdSemestre());
                            
        if (existe) {
            throw new CarreraAsignaturaConflictException("La asignatura ya esta asignada a esta carrera en este semestre.");
        }

        CarreraAsignatura nuevaRelacion = new CarreraAsignatura();
        nuevaRelacion.setCarrera(carreraRepository.findById(dto.getIdCarrera())
                .orElseThrow(() -> new CarreraNotFoundException(dto.getIdCarrera())));
        nuevaRelacion.setIdAsignatura(dto.getIdAsignatura());
        nuevaRelacion.setIdSemestre(dto.getIdSemestre());
        
        CarreraAsignatura guardada = repository.save(nuevaRelacion);
        logger.info("Relacion Carrera-Asignatura creada con ID: {}", guardada.getIdCarreraAsignatura());
        return toResponseDTO(guardada);
    }

    @Transactional
    public CarreraAsignaturaResponseDTO actualizar(Long id, CarreraAsignaturaRequestDTO dto) {

    CarreraAsignatura entidad = repository.findById(id)
            .orElseThrow(() -> new CarreraAsignaturaNotFoundException(id));

    
    boolean cambioIdentidad = !entidad.getCarrera().getIdCarrera().equals(dto.getIdCarrera()) ||
                             !entidad.getIdAsignatura().equals(dto.getIdAsignatura()) ||
                             !entidad.getIdSemestre().equals(dto.getIdSemestre());

    if (cambioIdentidad && repository.existsByCarrera_IdCarreraAndIdAsignaturaAndIdSemestre(
            dto.getIdCarrera(), dto.getIdAsignatura(), dto.getIdSemestre())) {
        throw new CarreraAsignaturaConflictException("Conflicto: Esta asignatura ya está asignada a esta carrera en el semestre indicado.");
    }
    
    entidad.setIdAsignatura(dto.getIdAsignatura());
    entidad.setIdSemestre(dto.getIdSemestre());
    
    if (!entidad.getCarrera().getIdCarrera().equals(dto.getIdCarrera())) {
         var nuevaCarrera = carreraRepository.findById(dto.getIdCarrera())
                 .orElseThrow(() -> new CarreraNotFoundException(dto.getIdCarrera()));
         entidad.setCarrera(nuevaCarrera);
    }

        return toResponseDTO(repository.save(entidad));
    }

    
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new CarreraAsignaturaNotFoundException(id);
        }
        repository.deleteById(id);
        logger.info("Auditoría: Relación Carrera-Asignatura ID: {} eliminada", id);
    }

    private CarreraAsignaturaResponseDTO toResponseDTO(CarreraAsignatura ca) {
        CarreraAsignaturaResponseDTO dto = new CarreraAsignaturaResponseDTO();
        dto.setIdCarreraAsignatura(ca.getIdCarreraAsignatura());
        dto.setIdCarrera(ca.getCarrera().getIdCarrera());
        dto.setIdAsignatura(ca.getIdAsignatura());
        dto.setIdSemestre(ca.getIdSemestre());

        try {
            var asignatura = asignaturaClient.obtenerAsignaturaPorId(ca.getIdAsignatura());
            if (asignatura != null) {
                dto.setNombreAsignatura(asignatura.getNombre());
            }

            var semestre = asignaturaClient.obtenerSemestrePorId(ca.getIdSemestre());
            if (semestre != null) {
                dto.setNombreSemestre(semestre.getNombre());
            }

        } catch (Exception e) {
            logger.error("Error al consultar MS Asignatura (ID Asignatura: {} | ID semestre {}): {}",
             ca.getIdAsignatura(), e.getMessage());
            dto.setNombreAsignatura("Nombre no disponible");
            dto.setNombreSemestre("nombre no disponible");
        }
        return dto;
    }

}
