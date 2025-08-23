package com.afr.ApiPorta.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodigoPostalDto {
    private Long id;
    private String codigo;
    private BigDecimal gastosEnvio;
}
