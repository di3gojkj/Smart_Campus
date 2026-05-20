package com.diego.MS_Gestion_Usuario.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.diego.MS_Gestion_Usuario.client.EstadoClient;
import com.diego.MS_Gestion_Usuario.dto.RolDTO;
import com.diego.MS_Gestion_Usuario.dto.UsuarioRequestDTO;
import com.diego.MS_Gestion_Usuario.dto.UsuarioResponseDTO;
import com.diego.MS_Gestion_Usuario.exception.UsuarioNotFoundException;
import com.diego.MS_Gestion_Usuario.model.Rol;
import com.diego.MS_Gestion_Usuario.model.Usuario;
import com.diego.MS_Gestion_Usuario.repository.RolRepository;
import com.diego.MS_Gestion_Usuario.repository.UsuarioRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoClient estadoClient;
    private final PasswordEncoder passwordEncoder;


    public void registrarUsuario(String rawPassword) {
        // Cifrado de la contraseña
        String hashedPassword = passwordEncoder.encode(rawPassword);
        // Aquí guardarías 'hashedPassword' en tu base de datos
    }

    private UsuarioResponseDTO mapToDTO(Usuario u) {
        Set<RolDTO> rolesDTO = u.getRoles().stream()
                .map(r -> new RolDTO(r.getIdRol(), r.getNombre()))
                .collect(Collectors.toSet());
        return new UsuarioResponseDTO(u.getIdUsuario(), u.getRut(), u.getNombre(), u.getApellido(), u.getCorreo(), u.getIdEstado(), rolesDTO);
    }


    // Intercepta errores de red o códigos HTTP 404 provenientes del MS Estados
    public void validarEstadoRemoto(Long idEstado) {
        try {
            estadoClient.obtenerEstadoPorId(idEstado);
        } catch (FeignException.NotFound ex) {
            throw new RuntimeException("Regla Distribuida: El ID de Estado " + idEstado + " no se encuentra registrado en el MS Estados.");
        } catch (FeignException ex) {
            throw new RuntimeException("Falla de Infraestructura: El MS Gestión de Estado no responde en el puerto especificado.");
        }
    }

    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        return usuarioRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo '" + dto.getCorreo() + "' ya está registrado.");
        }
        if (usuarioRepository.findByRut(dto.getRut()).isPresent()) {
            throw new RuntimeException("El RUT '" + dto.getRut() + "' ya está registrado en el sistema.");
        }

        // Llamada de red a través de Feign
        validarEstadoRemoto(dto.getIdEstado());

        // Recolectar y validar roles internos locales
        Set<Rol> rolesAsignados = dto.getIdsRoles().stream()
                .map(idRol -> rolRepository.findById(idRol)
                        .orElseThrow(() -> new RuntimeException("El Rol con ID " + idRol + " no existe localmente.")))
                .collect(Collectors.toSet());

        Usuario usuario = new Usuario(null, dto.getRut(), dto.getNombre(), dto.getApellido(), dto.getCorreo(), dto.getClave(), dto.getIdEstado(), rolesAsignados);
        return mapToDTO(usuarioRepository.save(usuario));
    }
}
