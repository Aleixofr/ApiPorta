package com.afr.ApiPorta.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {
    private Long VendedorId;
    private String direccionEnvio;
    private String codigoPostal;
    private List<PedidoProductoRequest> productos;
}
