package com.afr.ApiPorta.repository;

import com.afr.ApiPorta.model.Pedido;
import com.afr.ApiPorta.model.PedidoEstado;
import com.afr.ApiPorta.model.Usuario;
import com.afr.ApiPorta.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido,Long> {

    List<Pedido> findByCliente(Usuario cliente);
    List<Pedido> findByVendedor(Vendedor vendedor);
    List<Pedido> findByEstado(PedidoEstado estado);
    List<Pedido> findByVendedorAndEstado(Vendedor vendedor, PedidoEstado estado);

    //Pedidos ordenados por fecha
    List<Pedido> findByClienteOrderByCreatedAtDesc(Usuario cliente);
    List<Pedido> findByVendedorOrderByCreatedAtDesc(Vendedor vendedor);

    Long countByVendedor(Vendedor vendedor);
}
