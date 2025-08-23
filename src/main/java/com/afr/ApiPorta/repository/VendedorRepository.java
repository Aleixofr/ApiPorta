package com.afr.ApiPorta.repository;

import com.afr.ApiPorta.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor,Long> {
    List<Vendedor> findByIsActiveTrue();
    Optional<Vendedor> findByUsuarioId(Long id);

    @Query("SELECT v FROM Vendedor v JOIN v.zonaPostal zp WHERE zp.codigo = :codigoPostal AND v.isActive = true")
    List<Vendedor> findByCodigoPostal(@Param("codigoPostal")  String codigoPostal);
}
