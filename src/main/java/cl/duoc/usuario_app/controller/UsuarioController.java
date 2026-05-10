package cl.duoc.usuario_app.controller;

import cl.duoc.usuario_app.model.Usuario;
import cl.duoc.usuario_app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {

        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/recuperar/{correo}")
    public ResponseEntity<Usuario> obtenerPorCorreo(@RequestParam String correo) {
        Usuario usuario = usuarioService.obtenerPorCorreo(correo);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        usuario.setId(null);
        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {

        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {

        usuarioService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
