package org.eskilokos.eskilokos.features.Pedido.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

public class PedidoContenidoUpdateDTO {

    @Schema(description = "Nueva cantidad para el platillo seleccionado dentro del pedido", example = "4")
    private Integer cantidad;

    public PedidoContenidoUpdateDTO() {}

    public PedidoContenidoUpdateDTO(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}