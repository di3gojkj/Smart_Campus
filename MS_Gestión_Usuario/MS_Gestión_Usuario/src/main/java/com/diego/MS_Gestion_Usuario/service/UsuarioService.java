package com.diego.MS_Gestion_Usuario.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.diego.MS_Gestion_Usuario.client.EstadoClient;
import com.diego.MS_Gestion_Usuario.dto.RolDTO;
import com.diego.MS_Gestion_Usuario.dto.UsuarioRequestDTO;
import com.diego.MS_Gestion_Usuario.dto.UsuarioResponseDTO;
import com.diego.MS_Gestion_Usuario.dto.EstadoResponseDTO;
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

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EstadoClient estadoClient;
    private final PasswordEncoder passwordEncoder;

    private UsuarioResponseDTO mapToDTO(Usuario u) {
        Set<RolDTO> rolesDTO = u.getRoles().stream()
                .map(r -> new RolDTO(r.getIdRol(), r.getNombre()))
                .collect(Collectors.toSet());
        return new UsuarioResponseDTO(u.getIdUsuario(), u.getRut(), u.getNombre(), u.getApellido(), u.getCorreo(), u.getIdEstado(), rolesDTO);
    }

    @Transactional(readOnly = true)
    public void validarEstadoRemoto(Long idEstado) {
        logger.info("Verificando existencia del Estado ID: {} en el microservicio remoto", idEstado);
        try {
            EstadoResponseDTO estado = estadoClient.obtenerEstadoPorId(idEstado);
            logger.debug("Estado obtenido correctamente: {}", estado.getNombre());
        } catch (FeignException.NotFound ex) {
            logger.warn("El ID de Estado {} no fue localizado en el MS Estados", idEstado);
            throw new RuntimeException("Regla Distribuida: El ID de Estado " + idEstado + " no se encuentra registrado en el sistema.");
        } catch (Exception ex) {
            logger.error("Error crítico de infraestructura al comunicar con MS Estados: {}", ex.getMessage());
            throw new RuntimeException("Falla de Comunicación: El MS Gestión de Estado no responde en el puerto especificado.");
        }
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodos() {
        logger.info("Buscando listado completo de usuarios registrados");
        return usuarioRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Long id) {
        logger.info("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> {
                    logger.warn("Usuario con ID {} no existe en la base de datos", id);
                    return new UsuarioNotFoundException(id);
                });
    }

    @Transactional
    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto) {
        logger.info("Iniciando proceso de registro para el RUT: {}", dto.getRut());

        if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            logger.warn("Intento de registro fallido: El correo {} ya existe", dto.getCorreo());
            throw new RuntimeException("El correo '" + dto.getCorreo() + "' ya está registrado.");
        }
        if (usuarioRepository.findByRut(dto.getRut()).isPresent()) {
            logger.warn("Intento de registro fallido: El RUT {} ya existe", dto.getRut());
            throw new RuntimeException("El RUT '" + dto.getRut() + "' ya está registrado en el sistema.");
        }

        // Validación perimetral síncrona vía Feign
        validarEstadoRemoto(dto.getIdEstado());

        // Resolución de relaciones internas locales
        Set<Rol> rolesAsignados = dto.getIdsRoles().stream()
                .map(idRol -> rolRepository.findById(idRol)
                        .orElseThrow(() -> {
                            logger.warn("El Rol con ID {} no existe localmente", idRol);
                            return new RuntimeException("El Rol con ID " + idRol + " no existe localmente.");
                        }))
                .collect(Collectors.toSet());

        // Encriptación segura de la credencial antes de la persistencia física
        String claveEncriptada = passwordEncoder.encode(dto.getClave());
        logger.debug("Clave cifrada con éxito para el nuevo usuario");

        Usuario usuario = new Usuario(null, dto.getRut(), dto.getNombre(), dto.getApellido(), dto.getCorreo(), claveEncriptada, dto.getIdEstado(), rolesAsignados);
        Usuario guardado = usuarioRepository.save(usuario);

        logger.info("Usuario guardado con éxito. ID Asignado: {}", guardado.getIdUsuario());
        return mapToDTO(guardado);
    }
}