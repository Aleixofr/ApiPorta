package com.afr.ApiPorta.controller;

import com.afr.ApiPorta.model.dto.CodigoPostalDto;
import com.afr.ApiPorta.model.dto.CodigoPostalRequest;
import com.afr.ApiPorta.model.dto.VendedorDto;
import com.afr.ApiPorta.model.dto.VendedorSearchDto;
import com.afr.ApiPorta.model.dto.response.ApiResponse;
import com.afr.ApiPorta.service.VendedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendedores")
@RequiredArgsConstructor
@CrossOrigin("*")
public class VendedorController {

    private final VendedorService vendedorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VendedorDto>>> getAllVendedores() {
        List<VendedorDto> vendedores = vendedorService.getAllVendedoresActivos();
        return ResponseEntity.ok(ApiResponse.success(vendedores));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendedorDto>> getVendedorById(@PathVariable Long id) {
        VendedorDto vendedor = vendedorService.getVendedorById(id);
        return ResponseEntity.ok(ApiResponse.success(vendedor));
    }

    @GetMapping("/codigo-postal/{codigoPostal}")
    public ResponseEntity<ApiResponse<List<VendedorSearchDto>>> getVendedoresByCodigoPostal(@PathVariable String codigoPostal) {
        List<VendedorSearchDto> vendedores = vendedorService.getVendedoresCodigoPostal(codigoPostal);
        return ResponseEntity.ok(ApiResponse.success(vendedores));
    }

    @PostMapping("/{vendedorId}/codigos-postales")
    public ResponseEntity<ApiResponse<CodigoPostalDto>> addCodigoPostal(@PathVariable Long vendedorId,
                                                                        @RequestBody CodigoPostalRequest request) {
        CodigoPostalDto codigoPostal = vendedorService.addCodigoPostal(vendedorId, request);
        return ResponseEntity.ok(ApiResponse.success("Zona de reparto añadida", codigoPostal));
    }

    @GetMapping("/{vendedorId}/codigos-postales")
    public ResponseEntity<ApiResponse<List<CodigoPostalDto>>> getCodigosPostales(@PathVariable Long vendedorId) {
        List<CodigoPostalDto> codigos = vendedorService.getCodigosPostales(vendedorId);
        return ResponseEntity.ok(ApiResponse.success(codigos));
    }

    @DeleteMapping("/codigos-postales/{codigoPostalId}")
    public ResponseEntity<ApiResponse<Void>> removeCodigoPostal(@PathVariable Long codigoPostalId) {
        vendedorService.removeCodigoPostal(codigoPostalId);
        return ResponseEntity.ok(ApiResponse.success("Zona de reparto eliminada", null));
    }
}
