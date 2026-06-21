package com.cur_eva.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cur_eva.dto.EvaluacionResponseDTO;
import com.cur_eva.dto.TipoEvaluacionResponseDTO; // Importación del nuevo DTO local

@FeignClient(name = "Ms-Evaluacion", url = "http://localhost:8090")
public interface EvaluacionClient {

    // Método original: Buscar Evaluación por ID
    @GetMapping("/api/evaluacion/{id}")
    EvaluacionResponseDTO buscarPorId(@PathVariable("id") Long id);

    // NUEVO MÉTODO: Buscar Tipo de Evaluación por ID de forma sincrónica
    @GetMapping("/api/tipo-evaluacion/{id}")
    TipoEvaluacionResponseDTO buscarTipoPorId(@PathVariable("id") Long id);
}
