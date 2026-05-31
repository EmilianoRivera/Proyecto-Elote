package org.eskilokos.eskilokos.core.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Fetch;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Pedido")
public class Pedido implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer idPedido;

    @Column(name = "Costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal costo;

    @Column(name = "Estado_Atencion", length = 50)
    private String estadoAtencion;

    @Column(name = "Estado_Reparto", length = 50)
    private String estadoReparto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCocinero")
    private Cocinero cocinero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRepartidor")
    private Repartidor repartidor;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoContenido> contenidos = new HashSet<>();

    public Pedido(Integer idPedido){
        this.idPedido = idPedido;
    }


}
// idpedido, costo, idcliente, cocinero, idrepartidor, estadoatencion, estadoreparto
