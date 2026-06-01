package org.eskilokos.eskilokos.features.Pedido.service;

import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.eskilokos.eskilokos.core.entidades.PedidoContenido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> findAll();

    Optional<Pedido> findById(Integer id);

    Pedido save(Pedido pedido);

    Pedido update(Integer id, Pedido pedido);

    void deleteById(Integer id);

    // Cliente (Hacer)
    List<Pedido> findByClienteId(Integer idCliente);

    // Contenido (Contener)
    Pedido agregarPlatillo(Integer idPedido, Integer idPlatillo, Integer cantidad);

    Pedido quitarPlatillo(Integer idPedido, Integer idPlatillo);

    List<PedidoContenido> getContenido(Integer idPedido);
}