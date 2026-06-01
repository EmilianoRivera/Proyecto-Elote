package org.eskilokos.eskilokos.features.Pedido.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.eskilokos.eskilokos.core.entidades.*;
import org.eskilokos.eskilokos.features.mail.service.mailService;
import org.eskilokos.eskilokos.features.Pedido.repository.PedidoRepository;
import org.eskilokos.eskilokos.features.Pedido.service.PedidoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    // NUEVO: inyectado para disparar correos en save y update
    private final mailService mailService;

    @PersistenceContext
    private EntityManager entityManager;

    // MODIFICADO: se agrega mailService al constructor
    public PedidoServiceImpl(PedidoRepository pedidoRepository, mailService mailService) {
        this.pedidoRepository = pedidoRepository;
        this.mailService = mailService;
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
        if (pedido.getCliente() != null && pedido.getCliente().getIdCliente() != null) {
            pedido.setCliente(entityManager.getReference(Cliente.class, pedido.getCliente().getIdCliente()));
        }
        if (pedido.getCocinero() != null && pedido.getCocinero().getIdCocinero() != null) {
            pedido.setCocinero(entityManager.getReference(Cocinero.class, pedido.getCocinero().getIdCocinero()));
        }
        if (pedido.getRepartidor() != null && pedido.getRepartidor().getIdRepartidor() != null) {
            pedido.setRepartidor(
                    entityManager.getReference(Repartidor.class, pedido.getRepartidor().getIdRepartidor()));
        }

        Pedido guardado = pedidoRepository.save(pedido);

        // NUEVO: correo de confirmación al cliente en cuanto se registra el pedido
        mailService.enviarConfirmacionPedido(guardado);

        return guardado;
    }

    @Override
    @Transactional
    public Pedido update(Integer id, Pedido pedido) {
        Pedido existing = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        existing.setCosto(pedido.getCosto());
        existing.setEstadoAtencion(pedido.getEstadoAtencion());
        existing.setEstadoReparto(pedido.getEstadoReparto());

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
            existing.setRepartidor(
                    entityManager.getReference(Repartidor.class, pedido.getRepartidor().getIdRepartidor()));
        } else {
            existing.setRepartidor(null);
        }

        Pedido actualizado = pedidoRepository.save(existing);

        // NUEVO: se detecta la transición de estado y se dispara el correo
        // correspondiente.
        // Se usa el valor que viene en el body (pedido.getEstadoReparto()), no el del
        // objeto
        // guardado, porque getReference devuelve un proxy y getEstadoReparto podría no
        // estar
        // inicializado todavía en ese punto de la transacción.
        String nuevoEstadoReparto = pedido.getEstadoReparto();
        if ("En camino".equalsIgnoreCase(nuevoEstadoReparto)) {
            mailService.enviarPedidoEnCamino(actualizado);
        } else if ("Entregado".equalsIgnoreCase(nuevoEstadoReparto)) {
            mailService.enviarPedidoEntregado(actualizado);
        }

        return actualizado;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public List<Pedido> findByClienteId(Integer idCliente) {
        return pedidoRepository.findByCliente_IdCliente(idCliente);
    }

    @Override
    @Transactional
    public Pedido agregarPlatillo(Integer idPedido, Integer idPlatillo, Integer cantidad) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));

        pedido.getContenidos().size();

        Optional<PedidoContenido> existente = pedido.getContenidos().stream()
                .filter(c -> c.getPlatillo().getIdPlatillo().equals(idPlatillo))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().setCantidad(cantidad);
        } else {
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
    @Transactional
    public List<PedidoContenido> getContenido(Integer idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));
        pedido.getContenidos().size();
        return new ArrayList<>(pedido.getContenidos());
    }
}