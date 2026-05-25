package com.diego.Ms_Gestion_Lista.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.diego.Ms_Gestion_Lista.dto.CursoResponseDTO;

@FeignClient(name = "curso-seccion", url = "http://localhost:8090"  )
public interface CursoClient {
    @GetMapping
    CursoResponseDTO obtenerPorId(@PathVariable Long id);
    


}
