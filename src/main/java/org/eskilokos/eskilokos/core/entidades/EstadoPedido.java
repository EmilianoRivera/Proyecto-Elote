package org.eskilokos.eskilokos.core.entidades;

public enum EstadoPedido {
    RECIBIDO("Recibido"),
    PREPARACION("En Preparación"),
    ESPERA_REPARTIDOR("Listo para recolectar"),
    EN_REPARTO("En Camino"),
    ENTREGADO("Entregado y Finalizado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
