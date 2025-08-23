package com.afr.ApiPorta.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagen;
    private Boolean isAvailable;
    private String vendedorNombre;
    private Long vendedorId;
}
