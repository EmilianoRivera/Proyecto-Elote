package org.eskilokos.eskilokos.features.Pedido.service;

import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.eskilokos.eskilokos.core.entidades.PedidoContenido;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoContenidoUpdateDTO;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoRequestDTO;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    List<Pedido> findAll();

    Optional<Pedido> findById(Integer id);

    Pedido save(PedidoRequestDTO dto);

    // DEJA SOLO ESTE UPDATE (Cambiamos PedidoRequestDTO por PedidoContenidoUpdateDTO)
    Pedido update(Integer id, PedidoContenidoUpdateDTO dto);

    void deleteById(Integer id);

    List<Pedido> findByClienteId(Integer idCliente);

    Pedido agregarPlatillo(Integer idPedido, Integer idPlatillo, Integer cantidad);
    Pedido quitarPlatillo(Integer idPedido, Integer idPlatillo);
    List<PedidoContenido> getContenido(Integer idPedido);
}