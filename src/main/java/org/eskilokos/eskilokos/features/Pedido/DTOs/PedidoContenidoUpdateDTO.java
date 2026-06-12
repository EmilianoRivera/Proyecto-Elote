package org.eskilokos.eskilokos.features.Pedido.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

public class PedidoContenidoUpdateDTO {

    private Integer cantidad;

    @Schema(
            description = "Fase actual del reparto del pedido",
            allowableValues = {"RECIBIDO", "PREPARACION", "ESPERA_REPARTIDOR", "EN_REPARTO", "ENTREGADO"},
            example = "PREPARACION"
    )
    private String estadoReparto;

    // --- Constructores ---
    public PedidoContenidoUpdateDTO() {}

    public PedidoContenidoUpdateDTO(Integer cantidad, String estadoReparto) {
        this.cantidad = cantidad;
        this.estadoReparto = estadoReparto;
    }

    // --- Getters y Setters ---
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstadoReparto() {
        return estadoReparto;
    }

    public void setEstadoReparto(String estadoReparto) {
        this.estadoReparto = estadoReparto;
    }
}