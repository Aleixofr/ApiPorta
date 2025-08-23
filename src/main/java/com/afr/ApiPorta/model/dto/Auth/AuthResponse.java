package com.afr.ApiPorta.model.dto.Auth;

import com.afr.ApiPorta.model.Usuario;
import com.afr.ApiPorta.model.dto.UsuarioDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UsuarioDto usuario;
}
