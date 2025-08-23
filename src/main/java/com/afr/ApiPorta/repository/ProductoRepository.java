package com.afr.ApiPorta.repository;

import com.afr.ApiPorta.model.Producto;
import com.afr.ApiPorta.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByVendedor(Vendedor vendedor);
    List<Producto> findByVendedorAndIsAvailableTrue(Vendedor vendedor);

    @Query("SELECT p FROM Producto p WHERE p.vendedor.id = :vendedorId AND p.nombre ILIKE %:nombre%")
    List<Producto> findByVendedorIdAndNombreContaining(@Param("vendedorId") Long vendedorId, @Param("nombre") String nombre);

    @Query("SELECT p FROM Producto p JOIN p.vendedor v JOIN v.zonaPostal zp WHERE zp.codigo = :codigoPostal AND p.isAvailable = true")
    List<Producto> findAvailableByCodigoPostal(@Param("codigoPostal") String codigoPostal);
}
