package MS.tipo_asistencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import MS.tipo_asistencia.dto.ListaResponseDTO;

@FeignClient(name = "ms-gestion-lista", url = "http://localhost:8095/api/listas") // Ajusta el endpoint base real de tu MS
public interface GestionListaClient {

    @GetMapping("/{id}")
    ListaResponseDTO buscarPorId(@PathVariable("id") Long id);
}
