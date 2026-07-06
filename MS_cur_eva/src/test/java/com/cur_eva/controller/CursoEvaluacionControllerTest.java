package com.cur_eva.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.service.CursoEvaluacionService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para CursoEvaluacionController")
public class CursoEvaluacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CursoEvaluacionService cursoEvaluacionService;

    @InjectMocks
    private CursoEvaluacionController cursoEvaluacionController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private CursoEvaluacionResponseDTO responseMock;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cursoEvaluacionController)
                .setValidator(new org.springframework.validation.SmartValidator() {
                    @Override
                    public boolean supports(Class<?> clazz) { return true; }
                    @Override
                    public void validate(Object target, org.springframework.validation.Errors errors) {}
                    @Override
                    public void validate(Object target, org.springframework.validation.Errors errors, Object... validationHints) {}
                })
                .build();

        responseMock = new CursoEvaluacionResponseDTO();
        responseMock.setIdCursoEvaluacion(1L);
        responseMock.setNombre("ACTIVO");
        responseMock.setIdCurso(12L);
        responseMock.setIdEvaluacion(100L);
        responseMock.setNombreEvaluacion("Certamen 1");
        responseMock.setNombreTipoEvaluacion("Certamen");
    }

    @Test
    @DisplayName("GET /api/cur-eva - Debe retornar 200 con el listado consolidado")
    void obtenerTodos_DebeRetornarStatus200YLista() throws Exception {
        when(cursoEvaluacionService.obtenerTodos()).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/cur-eva")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idCursoEvaluacion").value(1))
                .andExpect(jsonPath("$[0].nombre").value("ACTIVO"))
                .andExpect(jsonPath("$[0].nombreEvaluacion").value("Certamen 1"));

        verify(cursoEvaluacionService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("GET /api/cur-eva/{id} - Debe retornar 200 si el registro existe en la base de datos")
    void obtenerPorId_DebeRetornarStatus200_CuandoIdExiste() throws Exception {
        when(cursoEvaluacionService.obtenerPorId(1L)).thenReturn(responseMock);

        mockMvc.perform(get("/api/cur-eva/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCursoEvaluacion").value(1))
                .andExpect(jsonPath("$.nombreTipoEvaluacion").value("Certamen"));

        verify(cursoEvaluacionService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("POST /api/cur-eva - Debe retornar 201 al procesar un cuerpo de entrada válido")
    void crear_DebeRetornarStatus201YPayloadEnriquecido() throws Exception {
        CursoEvaluacionRequestDTO requestDTO = new CursoEvaluacionRequestDTO();
        requestDTO.setNombre("ACTIVO");
        requestDTO.setIdCurso(12L);
        requestDTO.setIdEvaluacion(100L);

        when(cursoEvaluacionService.guardar(any(CursoEvaluacionRequestDTO.class))).thenReturn(responseMock);

        mockMvc.perform(post("/api/cur-eva")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCursoEvaluacion").value(1))
                .andExpect(jsonPath("$.nombreEvaluacion").value("Certamen 1"));

        verify(cursoEvaluacionService, times(1)).guardar(any(CursoEvaluacionRequestDTO.class));
    }

    
}
