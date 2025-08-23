package com.afr.ApiPorta.model.dto.Auth;

import com.afr.ApiPorta.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String email;
    private String password;
    private String nombre;
    private String telefono;
    private Role role;

    private String negocio;
    private String descripcion;
    private String direccion;
}
