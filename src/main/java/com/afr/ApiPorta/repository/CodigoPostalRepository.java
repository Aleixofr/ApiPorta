package com.afr.ApiPorta.repository;

import com.afr.ApiPorta.model.CodigoPostal;
import com.afr.ApiPorta.model.Vendedor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodigoPostalRepository extends JpaRepository<CodigoPostal, Long> {
    List<CodigoPostal> findByVendedor(Vendedor vendedor);
    List<CodigoPostal> findByCodigo(String codigo);
    Optional<CodigoPostal> findByVendedorAndCodigo(Vendedor vendedor, String codigo);

    @Modifying
    @Transactional
    @Query("DELETE FROM CodigoPostal c WHERE c.id = :id")
    int deleteByIdCustom(@Param("id") Long id);

}
