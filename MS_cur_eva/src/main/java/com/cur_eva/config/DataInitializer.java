package com.cur_eva.config;

import org.springframework.stereotype.Component;


import com.cur_eva.repository.CursoEvaluacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {
    private final CursoEvaluacionRepository cursoEvaluacionRepository;
}
