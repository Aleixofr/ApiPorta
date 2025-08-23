package com.afr.ApiPorta.model;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Vendedor vendedor;

    @Column(nullable = false)
    private String direccionEnvio;

    @Column(nullable = false)
    private String codigoPostal;

    @Enumerated(EnumType.STRING)
    private PedidoEstado estado = PedidoEstado.PENDIENTE;

    private BigDecimal total;

    private BigDecimal gastosEnvio;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition = "JSON")
    private String productosJson;
}
