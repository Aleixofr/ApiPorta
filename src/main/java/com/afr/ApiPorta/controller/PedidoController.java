package com.afr.ApiPorta.controller;

import com.afr.ApiPorta.model.dto.*;
import com.afr.ApiPorta.model.dto.response.ApiResponse;
import com.afr.ApiPorta.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<ApiResponse<PedidoDto>> createPedido(@RequestBody PedidoRequest request,
                                                               @RequestHeader("Usuario-Id") Long clienteId) {
        PedidoDto pedido = pedidoService.createPedido(clienteId, request);
        return ResponseEntity.ok(ApiResponse.success("Pedido creado exitosamente", pedido));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<PedidoDto>>> getPedidosByCliente(@PathVariable Long clienteId) {
        List<PedidoDto> pedidos = pedidoService.getPedidosByCliente(clienteId);
        return ResponseEntity.ok(ApiResponse.success(pedidos));
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<ApiResponse<List<PedidoDto>>> getPedidosByVendedor(@PathVariable Long vendedorId) {
        List<PedidoDto> pedidos = pedidoService.getPedidosByVendedor(vendedorId);
        return ResponseEntity.ok(ApiResponse.success(pedidos));
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<ApiResponse<PedidoDto>> getPedidoById(@PathVariable Long pedidoId) {
        PedidoDto pedido = pedidoService.getPedidoById(pedidoId);
        return ResponseEntity.ok(ApiResponse.success(pedido));
    }

    @PutMapping("/{pedidoId}/estado")
    public ResponseEntity<ApiResponse<PedidoDto>> updateEstadoPedido(@PathVariable Long pedidoId,
                                                                     @RequestBody PedidoEstadoRequest request) {
        PedidoDto pedido = pedidoService.updateEstadoPedido(pedidoId, request.getEstado());
        return ResponseEntity.ok(ApiResponse.success("Estado del pedido actualizado", pedido));
    }

    @PutMapping("/{pedidoId}/cancelar")
    public ResponseEntity<ApiResponse<PedidoDto>> cancelarPedido(@PathVariable Long pedidoId) {
        PedidoDto pedido = pedidoService.cancelarPedido(pedidoId);
        return ResponseEntity.ok(ApiResponse.success("Pedido cancelado", pedido));
    }
}
