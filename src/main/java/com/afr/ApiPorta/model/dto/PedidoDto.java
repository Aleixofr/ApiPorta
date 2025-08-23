package com.afr.ApiPorta.model.dto;

import com.afr.ApiPorta.model.PedidoEstado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDto {
    private Long id;
    private String clienteNombre;
    private String vendedorNombre;
    private String direccionEnvio;
    private String codigoPostal;
    private PedidoEstado estado;
    private BigDecimal total;
    private BigDecimal gastosEnvio;
    private LocalDateTime createdAt;
    private String productosJson;
}
