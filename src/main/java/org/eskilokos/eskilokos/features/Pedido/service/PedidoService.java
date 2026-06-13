package org.eskilokos.eskilokos.features.Pedido.service;

import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.eskilokos.eskilokos.core.entidades.PedidoContenido;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoContenidoUpdateDTO;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoRequestDTO;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    List<Pedido> findAll();

    Optional<Pedido> findById(Integer id);

    Pedido save(PedidoRequestDTO dto);

    // Actualiza los estados generales del pedido
    Pedido update(Integer id, PedidoUpdateDTO dto);

    void deleteById(Integer id);

    List<Pedido> findByClienteId(Integer idCliente);

    // Gestiona alimentos y recalcula el costo de forma automatizada
    Pedido agregarPlatillo(Integer idPedido, Integer idPlatillo, Integer cantidad);
    Pedido quitarPlatillo(Integer idPedido, Integer idPlatillo);
    List<PedidoContenido> getContenido(Integer idPedido);
}