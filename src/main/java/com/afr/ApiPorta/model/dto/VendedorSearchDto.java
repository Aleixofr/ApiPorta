package com.afr.ApiPorta.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendedorSearchDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal gastosEnvio;
    private Long cantidadPedidos;
    private Integer cantidadProductos;
    private Long usuarioId;
}
