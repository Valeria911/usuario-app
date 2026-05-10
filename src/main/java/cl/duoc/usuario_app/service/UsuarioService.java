package cl.duoc.usuario_app.service;

import cl.duoc.usuario_app.exception.ResourceNotFoundException;
import cl.duoc.usuario_app.model.Usuario;
import cl.duoc.usuario_app.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
@Slf4j
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos(){
        log.info("Buscando todos los usuarios");
        List<Usuario> usuarios = usuarioRepository.findAll();
        log.info("Se encontraron {} usuarios", usuarios.size());
        return usuarios;
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró usuario ID: {}", id);
                    return new ResourceNotFoundException("No se encontró un usuario con ID: " + id);
                });
    }

    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + correo));    }

    public Usuario guardarUsuario(Usuario usuario){
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario guardado con ID " + usuarioGuardado.getId());
        return usuarioGuardado;
    }

    public Usuario actualizarUsuario (Long id, Usuario usuario){
        Usuario usuarioOriginal = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encuentra usuario con ID: { }", id);
                    return new ResourceNotFoundException("No se encontró usuario con ID " + id);
                });

        usuarioOriginal.setNombre(usuario.getNombre());
        usuarioOriginal.setCorreo(usuario.getCorreo());
        usuarioOriginal.setContrasena(usuario.getContrasena());
        usuarioOriginal.setRol(usuario.getRol());
        usuarioOriginal.setActivo(usuario.getActivo());
        usuarioOriginal.setFechaRegistro(usuario.getFechaRegistro());

        Usuario usuarioGuardado = usuarioRepository.save(usuarioOriginal);

        log.info("Usuario actualizado correctamente con ID " + usuarioGuardado.getId());

        return usuarioGuardado;
    }

    public void eliminarPorId(Long id){

        Usuario usuarioOriginal = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con id { } ", id);
                    return new ResourceNotFoundException("Usuario no encontrado con id " + id);
                });

        usuarioRepository.delete(usuarioOriginal);

        log.info("Usuario eliminado correctamente con id " + id);
    }

}


