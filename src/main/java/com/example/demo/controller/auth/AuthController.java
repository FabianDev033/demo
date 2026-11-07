package com.example.demo.controller.auth;
import com.example.demo.dto.auth.AuthResponse; import com.example.demo.dto.auth.CredencialesRequest; import com.example.demo.domain.usuario.Usuario; import com.example.demo.service.UsuarioService; import io.swagger.v3.oas.annotations.Operation; import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") public class AuthController {
 private final UsuarioService auth; public AuthController(UsuarioService auth){this.auth=auth;}
 @PostMapping("/registro") @ResponseStatus(HttpStatus.CREATED) @Operation(summary="Registrar usuario") public AuthResponse registrar(@Valid @RequestBody CredencialesRequest req){Usuario u=auth.registrar(req.email(),req.password());return new AuthResponse(auth.token(u),u.id(),u.email(),u.rol().name());}
 @PostMapping("/login") @Operation(summary="Iniciar sesión") public AuthResponse login(@Valid @RequestBody CredencialesRequest req){Usuario u=auth.autenticar(req.email(),req.password());return new AuthResponse(auth.token(u),u.id(),u.email(),u.rol().name());}
}
