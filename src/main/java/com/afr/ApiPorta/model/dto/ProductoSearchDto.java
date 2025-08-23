package com.afr.ApiPorta.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoSearchDto {
    private String codigoPostal;
    private String parametroBusqueda;
    private BigDecimal maxPrecio;
    private BigDecimal minPrecio;
}
