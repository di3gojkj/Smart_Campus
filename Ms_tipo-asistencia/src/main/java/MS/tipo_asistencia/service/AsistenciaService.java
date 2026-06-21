package MS.tipo_asistencia.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import feign.FeignException;

import MS.tipo_asistencia.client.GestionListaClient;
import MS.tipo_asistencia.dto.AsistenciaRequestDTO;
import MS.tipo_asistencia.dto.AsistenciaResponseDTO;
import MS.tipo_asistencia.dto.ListaResponseDTO;
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
    private final TipoRepository tipoRepository;
    private final GestionListaClient listaClient; // 🛠️ Nuestro cliente OpenFeign

    /**
     * Mapea la entidad de persistencia hacia su DTO de salida incorporando
     * el enriquecimiento dinámico sincrónico desde el microservicio externo.
     */
    private AsistenciaResponseDTO toResponseDTO(Asistencia a) {
        AsistenciaResponseDTO dto = new AsistenciaResponseDTO();
        dto.setIdAsistencia(a.getIdAsistencia());
        dto.setFecha(a.getFecha());
        dto.setIdLista(a.getIdLista());
        dto.setTipo(a.getTipo());

        // Bloque de integración distribuida sincrónica y tolerante a fallos
        if (a.getIdLista() != null) {
            try {
                ListaResponseDTO inscripcionRemota = listaClient.buscarPorId(a.getIdLista());
                dto.setDatosInscripcion(inscripcionRemota);
            } catch (Exception e) {
                logger.error("Error al consultar MS Gestion Lista (ID Lista: {}): {}", a.getIdLista(), e.getMessage());
                // Tolerancia a fallos: se entregan datos locales sin tumbar la API de asistencia
                dto.setDatosInscripcion(null);
            }
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResponseDTO> obtenerTodas() {
        logger.info("[AsistenciaService] Consultando listado completo de asistencias registradas");
        return asistenciaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AsistenciaResponseDTO> obtenerPorId(Long id) {
        logger.info("[AsistenciaService] Buscando registro de asistencia por ID: {}", id);
        return asistenciaRepository.findById(id).map(this::toResponseDTO);
    }

    /**
     * Almacena un registro tras ejecutar la validación perimetral dual (local + remota)
     */
    @Transactional
    public AsistenciaResponseDTO guardar(AsistenciaRequestDTO dto) {
        logger.info("[AsistenciaService] Procesando alta de asistencia para la fecha: {}", dto.getFecha());

        // 1. Validación de Integridad Lógica Local (Tipo de asistencia)
        Tipo tipoLocal = tipoRepository.findById(dto.getIdTipo())
                .orElseThrow(() -> new RuntimeException("Error de Negocio: El ID de Tipo de asistencia especificado no existe en el catálogo local."));

        // 2. Validación de Integridad Referencial Distribuida (Lista de inscripción)
        if (dto.getIdLista() != null) {
            try {
                logger.info("[AsistenciaService] Verificando existencia de ID de lista remota: {}", dto.getIdLista());
                listaClient.buscarPorId(dto.getIdLista());
            } catch (FeignException.NotFound e) {
                throw new RuntimeException("Error de Consistencia Distribuida: La lista/inscripción con ID " + dto.getIdLista() + " no existe en gestion_lista.");
            } catch (Exception e) {
                throw new RuntimeException("El servicio externo gestion_lista no se encuentra disponible temporalmente.");
            }
        }

        // Mapeo e inserción física en MySQL
        Asistencia asistencia = new Asistencia();
        asistencia.setFecha(dto.getFecha());
        asistencia.setIdLista(dto.getIdLista());
        asistencia.setTipo(tipoLocal);

        Asistencia guardada = asistenciaRepository.save(asistencia);
        logger.info("[AsistenciaService] Asistencia guardada con éxito bajo el ID: {}", guardada.getIdAsistencia());

        return toResponseDTO(guardada);
    }

    @Transactional
    public void eliminar(Long id) {
        logger.info("[AsistenciaService] Removiendo registro físico de asistencia con ID: {}", id);
        asistenciaRepository.deleteById(id);
    }
}


