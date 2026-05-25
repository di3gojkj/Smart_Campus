package MS.tipo_asistencia.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import MS.tipo_asistencia.dto.AsistenciaRequestDTO;
import MS.tipo_asistencia.dto.AsistenciaResponseDTO;
import MS.tipo_asistencia.model.Asistencia;
import MS.tipo_asistencia.model.Tipo;
import MS.tipo_asistencia.repository.AsistenciaRepository;
import MS.tipo_asistencia.repository.TipoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsistenciaService {
    private static final Logger logger = LoggerFactory.getLogger(AsistenciaService.class);

    private final AsistenciaRepository asistenciaRepository;
    private final TipoService tipoService;
    private final TipoRepository tipoRepository;

    public AsistenciaService(AsistenciaRepository repository,
                                    TipoRespository TipoRepository) {
        this.repository = repository;
        this.tipoRepository = tipoRepository;
        
    }











    public List<AsistenciaResponseDTO> obtenerTodos() {
        return asistenciaRepository.findAll().stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }







    public Optional<AsistenciaResponseDTO> obtenerPorId(Long id) {
        return asistenciaRepository.findById(id).map(this::mapToDTO);
    }

    public List<AsistenciaResponseDTO> obtenerPorTipo(Long tipoId) {
        return asistenciaRepository.findByTipoId(tipoId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

     public AsistenciaResponseDTO guardar(AsistenciaRequestDTO dto) {
        Tipo tipo = tipoService.buscarEntidadPorId(dto.getTipoId());
        Asistencia a = new Asistencia(null, dto.getFecha(), tipo);
        return mapToDTO(asistenciaRepository.save(a));
    }

    public Optional<AsistenciaResponseDTO> actualizar(Long id, AsistenciaRequestDTO dto) {
        return asistenciaRepository.findById(id).map(existente -> {
            Tipo tipo = tipoService.buscarEntidadPorId(dto.getTipoId());
            existente.setFecha(dto.getFecha());
            existente.setTipo(tipo);
            return mapToDTO(asistenciaRepository.save(existente));
        });
    }

     public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }

}
