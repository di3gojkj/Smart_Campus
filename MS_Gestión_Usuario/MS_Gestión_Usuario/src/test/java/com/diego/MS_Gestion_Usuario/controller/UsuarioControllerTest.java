package com.diego.MS_Gestion_Usuario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.diego.MS_Gestion_Usuario.dto.RolDTO;
import com.diego.MS_Gestion_Usuario.dto.UsuarioRequestDTO;
import com.diego.MS_Gestion_Usuario.dto.UsuarioResponseDTO;
import com.diego.MS_Gestion_Usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

// Prueba aislada de la capa Web (Controlador) ignorando la seguridad momentáneamente para el test
@WebMvcTest(controllers = UsuarioController.class, properties = {"spring.security.enabled=false"})
@AutoConfigureMockMvc(addFilters = false) // Apaga el filtro de Spring Security para dejar pasar el MockMvc
@DisplayName("Tests del UsuarioController con MockMvc")
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Inyectamos un Mock del servicio porque aquí solo evaluamos que las URLs respondan bien
    @MockitoBean
    private UsuarioService usuarioService;

    // Herramienta para convertir los DTO a JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/usuarios debe retornar un JSON con la lista y código 200")
    void obtenerTodos_debeRetornar200() throws Exception {
        // Simulamos una respuesta del servicio
        UsuarioResponseDTO dto = new UsuarioResponseDTO(1L, "12345678-9", "Diego", "Rivas", "diego@duocuc.cl", 1L, Set.of(new RolDTO(1L, "ESTUDIANTE")));
        when(usuarioService.obtenerTodos()).thenReturn(List.of(dto));

        // Ejecutamos la petición simulada
        mockMvc.perform(get("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // Esperamos un 200 OK
                .andExpect(jsonPath("$[0].nombre").value("Diego"))
                .andExpect(jsonPath("$[0].rut").value("12345678-9"));
    }

    @Test
    @DisplayName("GET /api/usuarios debe retornar una lista vacía y código 200 cuando no hay datos")
    void obtenerTodos_debeRetornarListaVacia() throws Exception {
        // Simulamos que la base de datos no tiene registros
        when(usuarioService.obtenerTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0)); // Verifica que el array JSON está vacío
    }

    @Test
    @DisplayName("GET /api/usuarios/{id} debe retornar 200 cuando el usuario existe")
    void obtenerPorId_debeRetornar200_cuandoExiste() throws Exception {
        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "12345678-9", "Diego", "Rivas", "diego@duocuc.cl", 1L, null);
        when(usuarioService.obtenerPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    @DisplayName("POST /api/usuarios debe retornar 201 Created con datos válidos")
    void crear_debeRetornar201_cuandoDatosSonValidos() throws Exception {
        // Objeto que simulamos enviar desde Postman
        UsuarioRequestDTO request = new UsuarioRequestDTO("12345678-9", "Diego", "Rivas", "diego@duocuc.cl", "123456", 1L, Set.of(1L));
        // Objeto que simulamos recibir
        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "12345678-9", "Diego", "Rivas", "diego@duocuc.cl", 1L, Set.of(new RolDTO(1L, "ESTUDIANTE")));
        
        when(usuarioService.guardar(any(UsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // Esperamos un 201
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nombre").value("Diego"));
    }

    @Test
    @DisplayName("GET /api/usuarios/correo/{correo} debe retornar 200 cuando existe")
    void obtenerPorCorreo_debeRetornar200_cuandoExiste() throws Exception {
        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "12345678-9", "Diego", "Rivas", "diego@duocuc.cl", 1L, null);
        when(usuarioService.obtenerPorCorreo("diego@duocuc.cl")).thenReturn(response);

        mockMvc.perform(get("/api/usuarios/correo/diego@duocuc.cl")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("diego@duocuc.cl"));
    }

    @Test
    @DisplayName("POST /api/usuarios debe retornar 400 Bad Request si el JSON está vacío (Falla @Valid)")
    void crear_debeRetornar400_cuandoFaltanDatosObligatorios() throws Exception {
        // Enviamos un JSON vacío "{}", lo que hará que las validaciones @Valid fallen
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest()); 
    }
}