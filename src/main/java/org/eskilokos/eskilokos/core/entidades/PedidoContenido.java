package org.eskilokos.eskilokos.core.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Pedido_Contenido")
public class PedidoContenido implements Serializable {
    @EmbeddedId
    private PedidoContenidoId id= new PedidoContenidoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPedido")
    @JoinColumn(name = "idPedido", referencedColumnName = "idPedido")
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idPlatillo")
    @JoinColumn(name = "idPlatillo", referencedColumnName = "idPlatillo")
    private Platillo platillo;

    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

    public PedidoContenido(Pedido pedido, Platillo platillo, Integer cantidad) {
        this.pedido = pedido;
        this.platillo = platillo;
        this.cantidad = cantidad;
        this.id.setIdPedido(pedido.getIdPedido());
        this.id.setIdPlatillo(platillo.getIdPlatillo());
    }
}
