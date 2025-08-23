package com.afr.ApiPorta.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendedorDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private String direccion;
    private Boolean isActive;
    private List<String> codigosPostales;
}
