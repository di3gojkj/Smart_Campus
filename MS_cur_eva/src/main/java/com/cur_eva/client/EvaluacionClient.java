package com.cur_eva.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cur_eva.dto.EvaluacionResponseDTO;

@FeignClient(name = "Ms-Evaluacion", url = "http://localhost:8090")
public interface EvaluacionClient {

    @GetMapping("/api/evaluacion/{id}")
    EvaluacionResponseDTO buscarPorId(@PathVariable("id") Long id);
}
