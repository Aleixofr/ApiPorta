package com.afr.ApiPorta.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProductoRequest {
    private Long productoId;
    private Integer cantidad;
}
