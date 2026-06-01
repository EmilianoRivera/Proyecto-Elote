package org.eskilokos.eskilokos.features.Pedido.service.impl;

import org.eskilokos.eskilokos.core.entidades.*;
import org.eskilokos.eskilokos.features.Pedido.repository.PedidoRepository;
import org.eskilokos.eskilokos.features.Pedido.service.PedidoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> findById(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido update(Integer id, Pedido pedido) {
        Pedido existing = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
        existing.setCosto(pedido.getCosto());
        existing.setEstadoAtencion(pedido.getEstadoAtencion());
        existing.setEstadoReparto(pedido.getEstadoReparto());
        existing.setCliente(pedido.getCliente());
        existing.setCocinero(pedido.getCocinero());
        existing.setRepartidor(pedido.getRepartidor());
        return pedidoRepository.save(existing);
    }

    @Override
    public void deleteById(Integer id) {
        pedidoRepository.deleteById(id);
    }

    // --- Cliente (Hacer) ---
    @Override
    public List<Pedido> findByClienteId(Integer idCliente) {
        return pedidoRepository.findByCliente_IdCliente(idCliente);
    }

    // --- Contenido (Contener) ---
    @Override
    public Pedido agregarPlatillo(Integer idPedido, Integer idPlatillo, Integer cantidad) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));

        // Si ya existe ese platillo en el pedido, actualiza la cantidad
        Optional<PedidoContenido> existente = pedido.getContenidos().stream()
                .filter(c -> c.getPlatillo().getIdPlatillo().equals(idPlatillo))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().setCantidad(cantidad);
        } else {
            PedidoContenido nuevo = new PedidoContenido(pedido, new Platillo(idPlatillo), cantidad);
            pedido.getContenidos().add(nuevo);
        }

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido quitarPlatillo(Integer idPedido, Integer idPlatillo) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));

        pedido.getContenidos().removeIf(c -> c.getPlatillo().getIdPlatillo().equals(idPlatillo));
        return pedidoRepository.save(pedido);
    }

    @Override
    public List<PedidoContenido> getContenido(Integer idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));
        return new ArrayList<>(pedido.getContenidos());
    }
}