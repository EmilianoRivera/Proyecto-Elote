package org.eskilokos.eskilokos.features.Pedido.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import org.eskilokos.eskilokos.core.entidades.EstadoPedido;

public class PedidoUpdateDTO {

    @Schema(description = "Estado operativo de la cocina", example = "En Preparación")
    private String estadoAtencion;

    @Schema(description = "Fase del reparto que dispara los correos electrónicos automáticos")
    private EstadoPedido estadoReparto;

    public PedidoUpdateDTO() {}

    public String getEstadoAtencion() { return estadoAtencion; }
    public void setEstadoAtencion(String estadoAtencion) { this.estadoAtencion = estadoAtencion; }

    public EstadoPedido getEstadoReparto() { return estadoReparto; }
    public void setEstadoReparto(EstadoPedido estadoReparto) { this.estadoReparto = estadoReparto; }
}