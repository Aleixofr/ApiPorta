package com.afr.ApiPorta.controller;

import com.afr.ApiPorta.model.dto.Auth.AuthResponse;
import com.afr.ApiPorta.model.dto.Auth.LoginRequest;
import com.afr.ApiPorta.model.dto.Auth.RegisterRequest;
import com.afr.ApiPorta.model.dto.UsuarioDto;
import com.afr.ApiPorta.model.dto.response.ApiResponse;
import com.afr.ApiPorta.service.AuthService;
import com.afr.ApiPorta.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Validated @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("Login exitoso", authResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Validated @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Registro exitoso", authResponse));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UsuarioDto>> getProfile() {
        try {
            // Obtener el usuario autenticado del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.error("No autenticado"));
            }

            // El email/username viene del JWT
            String email = authentication.getName();

            // Obtener datos del usuario desde el servicio
            UsuarioDto usuario = usuarioService.getUsuarioByEmail(email);

            return ResponseEntity.ok(
                    ApiResponse.success("Perfil obtenido exitosamente", usuario)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Error interno del servidor"));
        }
    }
}
