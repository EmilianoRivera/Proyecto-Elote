package org.eskilokos.eskilokos.features.Pedido.DTOs;

public class PedidoContenidoRequestDTO {
    private Integer idPlatillo;
    private Integer cantidad;

    // Constructor vacío
    public PedidoContenidoRequestDTO() {}

    // Constructor cómodo
    public PedidoContenidoRequestDTO(Integer idPlatillo, Integer cantidad) {
        this.idPlatillo = idPlatillo;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public Integer getIdPlatillo() { return idPlatillo; }
    public void setIdPlatillo(Integer idPlatillo) { this.idPlatillo = idPlatillo; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
