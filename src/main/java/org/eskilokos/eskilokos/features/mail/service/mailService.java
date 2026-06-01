package org.eskilokos.eskilokos.features.mail.service;

import org.eskilokos.eskilokos.core.entidades.Pedido;

public interface mailService {

    // Dispara cuando el pedido se registra por primera vez
    void enviarConfirmacionPedido(Pedido pedido);

    // Dispara cuando el estado de reparto cambia a "En camino"
    void enviarPedidoEnCamino(Pedido pedido);

    // Dispara cuando el estado de reparto cambia a "Entregado"
    void enviarPedidoEntregado(Pedido pedido);
}