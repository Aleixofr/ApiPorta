package com.afr.ApiPorta.service;

import com.afr.ApiPorta.model.CodigoPostal;
import com.afr.ApiPorta.model.Vendedor;
import com.afr.ApiPorta.model.dto.CodigoPostalDto;
import com.afr.ApiPorta.model.dto.CodigoPostalRequest;
import com.afr.ApiPorta.model.dto.VendedorDto;
import com.afr.ApiPorta.model.dto.VendedorSearchDto;
import com.afr.ApiPorta.repository.CodigoPostalRepository;
import com.afr.ApiPorta.repository.PedidoRepository;
import com.afr.ApiPorta.repository.ProductoRepository;
import com.afr.ApiPorta.repository.VendedorRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendedorService {

    private final VendedorRepository vendedorRepository;
    private final CodigoPostalRepository codigoPostalRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public List<VendedorDto> getAllVendedoresActivos(){
        return vendedorRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public VendedorDto getVendedorById(Long id){
        Vendedor vendedor = vendedorRepository.findByUsuarioId(id).orElseThrow(
                () -> new RuntimeException("Vendedor no encontrado")
        );

        return convertToDto(vendedor);
    }

    public List<VendedorSearchDto> getVendedoresCodigoPostal(String codigoPostal) {
        return vendedorRepository.findByCodigoPostal(codigoPostal)
                .stream()
                .map(this::convertToSearchDto)
                .collect(Collectors.toList());
    }

    public CodigoPostalDto addCodigoPostal(Long VendedorId, CodigoPostalRequest request) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(VendedorId).orElseThrow(
                () -> new RuntimeException("Vendedor no encontrado")
        );

        if(codigoPostalRepository.findByVendedorAndCodigo(vendedor, request.getCodigo()).isPresent()) {
            throw new RuntimeException("El codigo postal ya existe");
        }

        CodigoPostal codigoPostal = new CodigoPostal();
        codigoPostal.setVendedor(vendedor);
        codigoPostal.setCodigo(request.getCodigo());
        codigoPostal.setGastosEnvio(request.getGastosEnvio());

        CodigoPostal save = codigoPostalRepository.save(codigoPostal);

        return convertCodigoPostalToDto(save);
    }

    public List<CodigoPostalDto> getCodigosPostales(Long VendedorId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(VendedorId).orElseThrow(
                () -> new RuntimeException("Vendedor no encontrado")
        );

        return codigoPostalRepository.findByVendedor(vendedor)
                .stream()
                .map(this::convertCodigoPostalToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeCodigoPostal(Long codigoPostalId) {
        codigoPostalRepository.deleteByIdCustom(codigoPostalId);
    }


    private VendedorDto convertToDto(Vendedor vendedor) {
        VendedorDto dto = new VendedorDto();
        dto.setId(vendedor.getId());
        dto.setNombre(vendedor.getNombre());
        dto.setDescripcion(vendedor.getDescripcion());
        dto.setDireccion(vendedor.getDireccion());
        dto.setIsActive(vendedor.getIsActive());

        List<String> codigos = vendedor.getZonaPostal()
                .stream()
                .map(CodigoPostal::getCodigo)
                .collect(Collectors.toList());
        dto.setCodigosPostales(codigos);

        return dto;
    }

    private VendedorSearchDto convertToSearchDto(Vendedor vendedor) {
        VendedorSearchDto dto = new VendedorSearchDto();
        dto.setId(vendedor.getId());
        dto.setNombre(vendedor.getNombre());
        dto.setDescripcion(vendedor.getDescripcion());
        dto.setUsuarioId(vendedor.getUsuario().getId());

        Long totalPedidos = pedidoRepository.countByVendedor(vendedor);
        Integer totalProductos = productoRepository.findByVendedorAndIsAvailableTrue(vendedor).size();

        dto.setCantidadPedidos(totalPedidos);
        dto.setCantidadProductos(totalProductos);

        dto.setGastosEnvio(vendedor.getZonaPostal().isEmpty() ?
                java.math.BigDecimal.ZERO :
                vendedor.getZonaPostal().get(0).getGastosEnvio());

        return dto;
    }

    private CodigoPostalDto convertCodigoPostalToDto(CodigoPostal codigoPostal) {
        CodigoPostalDto dto = new CodigoPostalDto();
        dto.setId(codigoPostal.getId());
        dto.setCodigo(codigoPostal.getCodigo());
        dto.setGastosEnvio(codigoPostal.getGastosEnvio());
        return dto;
    }
}
