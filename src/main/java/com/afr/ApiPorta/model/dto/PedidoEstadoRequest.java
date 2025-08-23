package com.afr.ApiPorta.model.dto;

import com.afr.ApiPorta.model.PedidoEstado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEstadoRequest {
    private PedidoEstado estado;
}
