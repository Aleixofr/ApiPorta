package com.afr.ApiPorta.service;

import com.afr.ApiPorta.model.*;
import com.afr.ApiPorta.model.dto.*;
import com.afr.ApiPorta.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VendedorRepository vendedorRepository;
    private final ProductoRepository productoRepository;
    private final CodigoPostalRepository codigoPostalRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public PedidoDto createPedido(Long clienteId, PedidoRequest request) {
        // Validar cliente
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (cliente.getRole() != Role.CLIENTE) {
            throw new RuntimeException("Solo los clientes pueden hacer pedidos");
        }

        // Validar vendedor
        Vendedor vendedor = vendedorRepository.findByUsuarioId(request.getVendedorId())
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        if (!vendedor.getIsActive()) {
            throw new RuntimeException("Vendedor no activo");
        }

        // Validar que el vendedor reparte en esa zona
//        CodigoPostal codigoPostal = codigoPostalRepository
//                .findByVendedorAndCodigo(vendedor, request.getCodigoPostal())
//                .orElseThrow(() -> new RuntimeException("El vendedor no reparte en ese código postal"));

        // Validar y procesar productos
        List<PedidoProductoDto> productosDto = procesarProductos(request.getProductos(), vendedor);

        // Calcular totales
        BigDecimal subtotal = calcularSubtotal(productosDto);
        BigDecimal gastosEnvio = BigDecimal.ZERO;
        BigDecimal total = subtotal.add(gastosEnvio);

        // Crear pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setVendedor(vendedor);
        pedido.setDireccionEnvio(request.getDireccionEnvio());
        pedido.setCodigoPostal(request.getCodigoPostal());
        pedido.setEstado(PedidoEstado.PENDIENTE);
        pedido.setTotal(total);
        pedido.setGastosEnvio(gastosEnvio);

        // Convertir productos a JSON
        try {
            String productosJson = objectMapper.writeValueAsString(productosDto);
            pedido.setProductosJson(productosJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar productos", e);
        }

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return convertToDto(pedidoGuardado);
    }

    private List<PedidoProductoDto> procesarProductos(List<PedidoProductoRequest> productosRequest, Vendedor vendedor) {
        return productosRequest.stream().map(request -> {
            // Validar producto
            Producto producto = productoRepository.findById(request.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + request.getProductoId()));

            // Validar que el producto pertenece al vendedor
            if (producto.getVendedor().getId() != vendedor.getId()) {
                throw new RuntimeException("El producto no pertenece a este vendedor");
            }

            // Validar disponibilidad
            if (!producto.getIsAvailable()) {
                throw new RuntimeException("Producto no disponible: " + producto.getNombre());
            }

            // Validar cantidad
            if (request.getCantidad() <= 0) {
                throw new RuntimeException("Cantidad debe ser mayor a 0");
            }

            // Crear DTO del producto del pedido
            PedidoProductoDto dto = new PedidoProductoDto();
            dto.setProductoId(producto.getId());
            dto.setNombre(producto.getNombre());
            dto.setCantidad(request.getCantidad());
            dto.setPrecioUnitario(producto.getPrecio());
            dto.setPrecioTotal(producto.getPrecio().multiply(BigDecimal.valueOf(request.getCantidad())));

            return dto;
        }).collect(Collectors.toList());
    }

    private BigDecimal calcularSubtotal(List<PedidoProductoDto> productos) {
        return productos.stream()
                .map(PedidoProductoDto::getPrecioTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<PedidoDto> getPedidosByCliente(Long clienteId) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return pedidoRepository.findByClienteOrderByCreatedAtDesc(cliente)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PedidoDto> getPedidosByVendedor(Long vendedorId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(vendedorId)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        return pedidoRepository.findByVendedorOrderByCreatedAtDesc(vendedor)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PedidoDto getPedidoById(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        return convertToDto(pedido);
    }

    @Transactional
    public PedidoDto updateEstadoPedido(Long pedidoId, PedidoEstado nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Validar transiciones de estado
        validarTransicionEstado(pedido.getEstado(), nuevoEstado);

        pedido.setEstado(nuevoEstado);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        return convertToDto(pedidoActualizado);
    }

    @Transactional
    public PedidoDto cancelarPedido(Long pedidoId) {
        return updateEstadoPedido(pedidoId, PedidoEstado.CANCELADO);
    }

    private void validarTransicionEstado(PedidoEstado estadoActual, PedidoEstado nuevoEstado) {
        // Reglas de negocio para transiciones
        switch (estadoActual) {
            case PENDIENTE:
                if (nuevoEstado != PedidoEstado.CONFIRMADO && nuevoEstado != PedidoEstado.CANCELADO) {
                    throw new RuntimeException("Desde PENDIENTE solo se puede ir a CONFIRMADO o CANCELADO");
                }
                break;
            case CONFIRMADO:
                if (nuevoEstado != PedidoEstado.CANCELADO) {
                    throw new RuntimeException("Desde CONFIRMADO solo se puede ir a CANCELADO");
                }
                break;
            case CANCELADO:
                throw new RuntimeException("No se puede cambiar el estado de un pedido cancelado");
        }
    }

    private PedidoDto convertToDto(Pedido pedido) {
        PedidoDto dto = new PedidoDto();
        dto.setId(pedido.getId());
        dto.setClienteNombre(pedido.getCliente().getNombre());
        dto.setVendedorNombre(pedido.getVendedor().getNombre());
        dto.setDireccionEnvio(pedido.getDireccionEnvio());
        dto.setCodigoPostal(pedido.getCodigoPostal());
        dto.setEstado(pedido.getEstado());
        dto.setTotal(pedido.getTotal());
        dto.setGastosEnvio(pedido.getGastosEnvio());
        dto.setCreatedAt(pedido.getCreatedAt());

        // Convertir JSON a productos
        if (pedido.getProductosJson() != null) {
            dto.setProductosJson(pedido.getProductosJson());
        }

        return dto;
    }

    public List<PedidoProductoDto> getProductosFromJson(String productosJson) {
        try {
            TypeReference<List<PedidoProductoDto>> typeRef = new TypeReference<List<PedidoProductoDto>>() {};
            return objectMapper.readValue(productosJson, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al leer productos del pedido", e);
        }
    }
}
