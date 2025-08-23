package com.afr.ApiPorta.controller;

import com.afr.ApiPorta.model.dto.ProductoDto;
import com.afr.ApiPorta.model.dto.ProductoRequest;
import com.afr.ApiPorta.model.dto.ProductoSearchDto;
import com.afr.ApiPorta.model.dto.response.ApiResponse;
import com.afr.ApiPorta.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<ApiResponse<List<ProductoDto>>> getProductosByVendedor(@PathVariable Long vendedorId) {
        List<ProductoDto> productos = productoService.getProductosByVendedor(vendedorId);
        return ResponseEntity.ok(ApiResponse.success(productos));
    }

    @GetMapping("/codigo-postal/{codigoPostal}")
    public ResponseEntity<ApiResponse<List<ProductoDto>>> getProductosByCodigoPostal(@PathVariable String codigoPostal) {
        List<ProductoDto> productos = productoService.getProductosByCodigoPostal(codigoPostal);
        return ResponseEntity.ok(ApiResponse.success(productos));
    }

    @PostMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ProductoDto>>> searchProductos(@RequestBody ProductoSearchDto searchDto) {
        List<ProductoDto> productos = productoService.searchProductos(searchDto);
        return ResponseEntity.ok(ApiResponse.success(productos));
    }

    @PostMapping("/vendedor/{vendedorId}")
    public ResponseEntity<ApiResponse<ProductoDto>> createProducto(@PathVariable Long vendedorId,
                                                                   @RequestBody ProductoRequest request) {
        ProductoDto producto = productoService.createProducto(vendedorId, request);
        return ResponseEntity.ok(ApiResponse.success("Producto creado exitosamente", producto));
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<ApiResponse<ProductoDto>> updateProducto(@PathVariable Long productoId,
                                                                   @RequestBody ProductoRequest request) {
        ProductoDto producto = productoService.updateProducto(productoId, request);
        return ResponseEntity.ok(ApiResponse.success("Producto actualizado", producto));
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<ApiResponse<Void>> deleteProducto(@PathVariable Long productoId) {
        productoService.deleteProducto(productoId);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado", null));
    }

    @PatchMapping("/{productoId}/disponibilidad")
    public ResponseEntity<ApiResponse<ProductoDto>> toggleDisponibilidad(@PathVariable Long productoId) {
        ProductoDto producto = productoService.toggleDisponibilidad(productoId);
        return ResponseEntity.ok(ApiResponse.success("Disponibilidad actualizada", producto));
    }

}
