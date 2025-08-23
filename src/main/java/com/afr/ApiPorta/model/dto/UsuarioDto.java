package com.afr.ApiPorta.model.dto;

import com.afr.ApiPorta.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {
    private Long id;
    private String email;
    private String nombre;
    private String telefono;
    private Role role;
    private VendedorDto vendedor;
}
