package org.eskilokos.eskilokos.features.Pedido.DTOs;

import java.math.BigDecimal;

public class PedidoRequestDTO {
    private BigDecimal costo;
    private String estadoAtencion;
    private String estadoReparto;

    // En lugar de los objetos completos, ¡solo pedimos los IDs primitivos!
    private Integer idCliente;
    private Integer numCocineros; // O el ID que use tu cocinero
    private Integer idRepartidor;

    // Constructor vacío
    public PedidoRequestDTO() {}

    // Getters y Setters
    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }

    public String getEstadoAtencion() { return estadoAtencion; }
    public void setEstadoAtencion(String estadoAtencion) { this.estadoAtencion = estadoAtencion; }

    public String getEstadoReparto() { return estadoReparto; }
    public void setEstadoReparto(String estadoReparto) { this.estadoReparto = estadoReparto; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public Integer getNumCocineros() { return numCocineros; }
    public void setNumCocineros(Integer numCocineros) { this.numCocineros = numCocineros; }

    public Integer getIdRepartidor() { return idRepartidor; }
    public void setIdRepartidor(Integer idRepartidor) { this.idRepartidor = idRepartidor; }
}
