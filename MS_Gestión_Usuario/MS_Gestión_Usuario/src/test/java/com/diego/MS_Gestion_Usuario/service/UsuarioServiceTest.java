package com.diego.MS_Gestion_Usuario.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.diego.MS_Gestion_Usuario.client.EstadoClient;
import com.diego.MS_Gestion_Usuario.dto.EstadoResponseDTO;
import com.diego.MS_Gestion_Usuario.dto.UsuarioRequestDTO;
import com.diego.MS_Gestion_Usuario.dto.UsuarioResponseDTO;
import com.diego.MS_Gestion_Usuario.exception.UsuarioNotFoundException;
import com.diego.MS_Gestion_Usuario.model.Rol;
import com.diego.MS_Gestion_Usuario.model.Usuario;
import com.diego.MS_Gestion_Usuario.repository.RolRepository;
import com.diego.MS_Gestion_Usuario.repository.UsuarioRepository;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unitario de UsuarioService con Mockito")
public class UsuarioServiceTest {

    // 1. Creamos los Mocks (Los dobles de acción)
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private EstadoClient estadoClient;
    @Mock
    private PasswordEncoder passwordEncoder;

    // 2. Inyectamos los Mocks en el Servicio real
    @InjectMocks
    private UsuarioService usuarioService;

    // Variables globales para las pruebas
    private Usuario usuarioEjemplo;
    private Rol rolEjemplo;
    private UsuarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Preparamos datos en memoria antes de cada test
        rolEjemplo = new Rol(1L, "ESTUDIANTE", 1L);
        Set<Rol> roles = new HashSet<>();
        roles.add(rolEjemplo);

        usuarioEjemplo = new Usuario(1L, "12345678-9", "Diego", "Rivas", "diego@duocuc.cl", "claveEncriptada", 1L, roles);
        
        // El DTO que simula lo que envía Postman
        requestDTO = new UsuarioRequestDTO("12345678-9", "Diego", "Rivas", "diego@duocuc.cl", "123456", 1L, Set.of(1L));
    }

    @Test
    @DisplayName("obtenerTodos() retorna la lista de DTO de todos los usuarios")
    void obtenerTodos_debeRetornarListaDeUsuarios() {
        // Le enseñamos al Mock qué responder
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        // Ejecutamos
        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodos();

        // Validamos
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Diego", resultado.get(0).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerPorId() retorna el DTO del usuario cuando el ID existe")
    void obtenerPorId_debeRetornarUsuario_cuandoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));

        UsuarioResponseDTO resultado = usuarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("diego@duocuc.cl", resultado.getCorreo());
    }

    

    @Test
    @DisplayName("obtenerPorId() lanza UsuarioNotFoundException cuando el ID no existe")
    void obtenerPorId_debeLanzarExcepcion_cuandoNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> {
            usuarioService.obtenerPorId(99L);
        });
    }

    @Test
    @DisplayName("obtenerPorCorreo() lanza excepcion cuando el correo no existe")
    void obtenerPorCorreo_debeLanzarExcepcion_cuandoNoExiste() {
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.obtenerPorCorreo("fantasma@duocuc.cl"));
        assertTrue(ex.getMessage().contains("Usuario no encontrado con el correo"));
    }

    @Test
    @DisplayName("validarEstadoRemoto() lanza excepcion cuando Feign retorna 404 Not Found")
    void validarEstadoRemoto_debeLanzarExcepcion_cuandoFeignRetornaNotFound() {
        FeignException.NotFound mockNotFound = mock(FeignException.NotFound.class);
        when(estadoClient.obtenerEstadoPorId(99L)).thenThrow(mockNotFound);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.validarEstadoRemoto(99L));
        assertTrue(ex.getMessage().contains("Regla Distribuida"));
    }

    @Test
    @DisplayName("validarEstadoRemoto() lanza excepcion generica cuando MS Estados falla")
    void validarEstadoRemoto_debeLanzarExcepcion_cuandoHayErrorDeConexion() {
        when(estadoClient.obtenerEstadoPorId(99L)).thenThrow(new RuntimeException("Connection Refused"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.validarEstadoRemoto(99L));
        assertTrue(ex.getMessage().contains("Falla de Comunicación"));
    }

    @Test
    @DisplayName("guardar() lanza excepcion si el correo ya existe")
    void guardar_debeLanzarExcepcion_cuandoCorreoYaExiste() {
        when(usuarioRepository.findByCorreo(requestDTO.getCorreo())).thenReturn(Optional.of(usuarioEjemplo));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.guardar(requestDTO));
        assertTrue(ex.getMessage().contains("ya está registrado"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("guardar() lanza excepcion si el RUT ya existe")
    void guardar_debeLanzarExcepcion_cuandoRutYaExiste() {
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByRut(requestDTO.getRut())).thenReturn(Optional.of(usuarioEjemplo));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.guardar(requestDTO));
        assertTrue(ex.getMessage().contains("RUT"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("guardar() lanza excepcion si el Rol no existe localmente")
    void guardar_debeLanzarExcepcion_cuandoRolNoExiste() {
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByRut(anyString())).thenReturn(Optional.empty());
        when(estadoClient.obtenerEstadoPorId(anyLong())).thenReturn(new EstadoResponseDTO(1L, "ACTIVO"));
        when(rolRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.guardar(requestDTO));
        assertTrue(ex.getMessage().contains("no existe localmente"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("guardar() crea y retorna el usuario correctamente validando relaciones")
    void guardar_debeCrearUsuario_cuandoDatosSonValidos() {
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByRut(anyString())).thenReturn(Optional.empty());
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(new EstadoResponseDTO(1L, "ACTIVO"));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolEjemplo));
        when(passwordEncoder.encode(anyString())).thenReturn("claveEncriptada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo);

        UsuarioResponseDTO resultado = usuarioService.guardar(requestDTO);

        assertNotNull(resultado);
        assertEquals("Diego", resultado.getNombre());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("obtenerPorCorreo() retorna el DTO del usuario cuando el correo existe")
    void obtenerPorCorreo_debeRetornarUsuario_cuandoExiste() {
        when(usuarioRepository.findByCorreo("diego@duocuc.cl")).thenReturn(Optional.of(usuarioEjemplo));
        
        UsuarioResponseDTO resultado = usuarioService.obtenerPorCorreo("diego@duocuc.cl");
        
        assertNotNull(resultado);
        assertEquals("diego@duocuc.cl", resultado.getCorreo());
        verify(usuarioRepository, times(1)).findByCorreo(anyString());
    }

    @Test
    @DisplayName("guardar() lanza excepcion genérica si ocurre un error inesperado en BD")
    void guardar_debeLanzarExcepcion_cuandoFallaBaseDeDatos() {
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByRut(anyString())).thenReturn(Optional.empty());
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(new EstadoResponseDTO(1L, "ACTIVO"));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolEjemplo));
        when(passwordEncoder.encode(anyString())).thenReturn("clave");
        
        when(usuarioRepository.save(any(Usuario.class))).thenThrow(new RuntimeException("Error fatal de BD"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.guardar(requestDTO));
        assertTrue(ex.getMessage().contains("Error fatal de BD"));
    }
}