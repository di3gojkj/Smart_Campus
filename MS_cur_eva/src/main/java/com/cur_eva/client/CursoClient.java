package com.cur_eva.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.cur_eva.dto.CursoResponseDTO;

@FeignClient(name = "curso-seccion", url = "http://localhost:8080/api/cursos")
public interface CursoClient {

    @GetMapping("/{id}")
    CursoResponseDTO buscarCursoPorId(@PathVariable("id") Long id);
}
