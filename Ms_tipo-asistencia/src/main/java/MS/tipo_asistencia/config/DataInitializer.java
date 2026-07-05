package MS.tipo_asistencia.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import MS.tipo_asistencia.model.Tipo;
import MS.tipo_asistencia.model.Asistencia;
import MS.tipo_asistencia.repository.TipoRepository;
import MS.tipo_asistencia.repository.AsistenciaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TipoRepository tipoRepository;
    private final AsistenciaRepository asistenciaRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("[DataInitializer]: Verificando estado de la BD para tipo-asistencia...");
        if (tipoRepository.count() == 0) {
            log.info("[DataInitializer]: Base de datos vacía. Insertando catálogo de tipos...");

            Tipo tipoPresente = tipoRepository.save(new Tipo(null, "PRESENTE"));
            Tipo tipoAusente = tipoRepository.save(new Tipo(null, "AUSENTE"));
            Tipo tipoJustificado = tipoRepository.save(new Tipo(null, "JUSTIFICADO"));

            log.info("[DataInitializer]: Tipos insertados. Procediendo con la precarga de asistencias...");

            asistenciaRepository.save(new Asistencia(null, "2026-06-21", 45L, tipoPresente));
            asistenciaRepository.save(new Asistencia(null, "2026-06-21", 46L, tipoAusente));
            asistenciaRepository.save(new Asistencia(null, "2026-06-22", 45L, tipoJustificado));

            log.info("[DataInitializer]: Datos iniciales cargados correctamente en tipo-asistencia");
        } else {
            log.info("[DataInitializer]: La tabla Tipo ya contiene registros, omitiendo inicialización...");
        }
    }
}
