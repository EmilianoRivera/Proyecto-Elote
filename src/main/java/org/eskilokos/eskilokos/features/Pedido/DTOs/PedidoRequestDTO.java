package org.eskilokos.eskilokos.features.Pedido.DTOs;

public class PedidoRequestDTO {
    private String estadoAtencion;
    private String estadoReparto;
    private Integer idCliente;
    private Integer numCocineros;
    private Integer idRepartidor;

    public PedidoRequestDTO() {}

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