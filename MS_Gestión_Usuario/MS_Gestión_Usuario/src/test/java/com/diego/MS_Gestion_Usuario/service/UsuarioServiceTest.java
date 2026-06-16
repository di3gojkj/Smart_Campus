package com.diego.MS_Gestion_Usuario.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("guardar() crea y retorna el usuario correctamente validando relaciones")
    void guardar_debeCrearUsuario_cuandoDatosSonValidos() {
        // GIVEN: Simulamos que el correo y el RUT no existen para que pase la validación
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByRut(anyString())).thenReturn(Optional.empty());
        
        // Simulamos que el Feign Client encuentra el estado
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(new EstadoResponseDTO(1L, "ACTIVO"));
        
        // Simulamos que encuentra el Rol
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolEjemplo));
        
        // Simulamos la encriptación
        when(passwordEncoder.encode(anyString())).thenReturn("claveEncriptada");
        
        // Simulamos el guardado final
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo);

        //Ejecutamos el guardado
        UsuarioResponseDTO resultado = usuarioService.guardar(requestDTO);

        //Comprobamos el éxito
        assertNotNull(resultado);
        assertEquals("Diego", resultado.getNombre());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}