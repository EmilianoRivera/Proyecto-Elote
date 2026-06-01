package org.eskilokos.eskilokos.features.Pedido.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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

    @PersistenceContext
    private EntityManager entityManager;

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
    @Transactional
    public Pedido save(Pedido pedido) {
        // Solución al POST: Enlazamos correctamente todas las referencias existentes
        if (pedido.getCliente() != null && pedido.getCliente().getIdCliente() != null) {
            pedido.setCliente(entityManager.getReference(Cliente.class, pedido.getCliente().getIdCliente()));
        }
        if (pedido.getCocinero() != null && pedido.getCocinero().getIdCocinero() != null) {
            pedido.setCocinero(entityManager.getReference(Cocinero.class, pedido.getCocinero().getIdCocinero()));
        }
        if (pedido.getRepartidor() != null && pedido.getRepartidor().getIdRepartidor() != null) {
            pedido.setRepartidor(entityManager.getReference(Repartidor.class, pedido.getRepartidor().getIdRepartidor()));
        }

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido update(Integer id, Pedido pedido) {
        Pedido existing = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        existing.setCosto(pedido.getCosto());
        existing.setEstadoAtencion(pedido.getEstadoAtencion());
        existing.setEstadoReparto(pedido.getEstadoReparto());

        // Solución al PUT: También debemos usar getReference aquí para no romper las llaves foráneas
        if (pedido.getCliente() != null && pedido.getCliente().getIdCliente() != null) {
            existing.setCliente(entityManager.getReference(Cliente.class, pedido.getCliente().getIdCliente()));
        } else {
            existing.setCliente(null);
        }

        if (pedido.getCocinero() != null && pedido.getCocinero().getIdCocinero() != null) {
            existing.setCocinero(entityManager.getReference(Cocinero.class, pedido.getCocinero().getIdCocinero()));
        } else {
            existing.setCocinero(null);
        }

        if (pedido.getRepartidor() != null && pedido.getRepartidor().getIdRepartidor() != null) {
            existing.setRepartidor(entityManager.getReference(Repartidor.class, pedido.getRepartidor().getIdRepartidor()));
        } else {
            existing.setRepartidor(null);
        }

        return pedidoRepository.save(existing);
    }

    @Override
    @Transactional
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
    @Transactional
    public Pedido agregarPlatillo(Integer idPedido, Integer idPlatillo, Integer cantidad) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));

        // Inicializamos la colección por si Hibernate la tiene en estado LAZY descuidada
        pedido.getContenidos().size();

        Optional<PedidoContenido> existente = pedido.getContenidos().stream()
                .filter(c -> c.getPlatillo().getIdPlatillo().equals(idPlatillo))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().setCantidad(cantidad);
        } else {
            // Usamos getReference para el platillo para que no se muera por transitorio
            Platillo platilloRef = entityManager.getReference(Platillo.class, idPlatillo);
            PedidoContenido nuevo = new PedidoContenido(pedido, platilloRef, cantidad);
            pedido.getContenidos().add(nuevo);
        }

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido quitarPlatillo(Integer idPedido, Integer idPlatillo) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));

        pedido.getContenidos().removeIf(c -> c.getPlatillo().getIdPlatillo().equals(idPlatillo));
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional // Agregamos Transactional aquí para que no falle al leer la lista Lazy de contenidos
    public List<PedidoContenido> getContenido(Integer idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));

        // Forzamos la carga de los datos en la misma transacción
        pedido.getContenidos().size();
        return new ArrayList<>(pedido.getContenidos());
    }
}