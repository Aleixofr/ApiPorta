package com.afr.ApiPorta.service;

import com.afr.ApiPorta.model.Producto;
import com.afr.ApiPorta.model.Vendedor;
import com.afr.ApiPorta.model.dto.ProductoDto;
import com.afr.ApiPorta.model.dto.ProductoRequest;
import com.afr.ApiPorta.model.dto.ProductoSearchDto;
import com.afr.ApiPorta.repository.ProductoRepository;
import com.afr.ApiPorta.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final VendedorRepository vendedorRepository;

    public List<ProductoDto> getProductosByVendedor(Long vendedorId) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(vendedorId).orElseThrow(
                () -> new RuntimeException("No existe el vendedor " + vendedorId)
        );
        return productoRepository.findByVendedor(vendedor)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    public List<ProductoDto> getProductosByCodigoPostal(String codigoPostal) {
        return productoRepository.findAvailableByCodigoPostal(codigoPostal)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProductoDto> searchProductos(ProductoSearchDto searchDto) {

        List<Producto> productos = productoRepository.findAvailableByCodigoPostal(searchDto.getCodigoPostal());

        return productos.stream()
                .filter(p -> searchDto.getParametroBusqueda() == null ||
                        p.getNombre().toLowerCase().contains(searchDto.getParametroBusqueda().toLowerCase()))
                .filter(p -> searchDto.getMinPrecio() == null ||
                        p.getPrecio().compareTo(searchDto.getMinPrecio()) >= 0)
                .filter(p -> searchDto.getMaxPrecio() == null ||
                        p.getPrecio().compareTo(searchDto.getMaxPrecio()) <= 0)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductoDto createProducto(Long VendedorId, ProductoRequest request) {
        Vendedor vendedor = vendedorRepository.findByUsuarioId(VendedorId).orElseThrow(
                () -> new RuntimeException("No existe el vendedor " + VendedorId)
        );

        Producto producto = new Producto();
        producto.setVendedor(vendedor);
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);

        Producto saved = productoRepository.save(producto);
        return convertToDto(saved);
    }

    public ProductoDto updateProducto(Long productoId, ProductoRequest request) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        if (request.getIsAvailable() != null) {
            producto.setIsAvailable(request.getIsAvailable());
        }

        Producto saved = productoRepository.save(producto);
        return convertToDto(saved);
    }

    public void deleteProducto(Long productoId) {
        productoRepository.deleteById(productoId);
    }

    public ProductoDto toggleDisponibilidad(Long productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setIsAvailable(!producto.getIsAvailable());
        Producto saved = productoRepository.save(producto);
        return convertToDto(saved);
    }

    private ProductoDto convertToDto(Producto producto) {
        ProductoDto dto = new ProductoDto();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setIsAvailable(producto.getIsAvailable());
        dto.setVendedorNombre(producto.getVendedor().getNombre());
        dto.setVendedorId(producto.getVendedor().getUsuario().getId());
        return dto;
    }
}
