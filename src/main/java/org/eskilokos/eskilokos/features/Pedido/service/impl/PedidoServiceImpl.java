package org.eskilokos.eskilokos.features.Pedido.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.eskilokos.eskilokos.core.entidades.*;
import org.eskilokos.eskilokos.features.Pedido.repository.PedidoRepository;
import org.eskilokos.eskilokos.features.Pedido.service.PedidoService;
import org.eskilokos.eskilokos.features.mail.service.mailService; // <-- Importación del servicio de correo
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final mailService correoService; // <-- Inyección de dependencia

    @PersistenceContext
    private EntityManager entityManager;

    // Actualizamos el constructor para recibir el mailService
    public PedidoServiceImpl(PedidoRepository pedidoRepository, mailService correoService) {
        this.pedidoRepository = pedidoRepository;
        this.correoService = correoService;
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
        // 1. Validaciones estrictas: Asegurarnos de que el JSON sí trajo los IDs
        if (pedido.getCliente() == null || pedido.getCliente().getIdCliente() == null) {
            throw new IllegalArgumentException("❌ ERROR: Falta el idCliente en el JSON.");
        }
        if (pedido.getCocinero() == null || pedido.getCocinero().getIdCocinero() == null) {
            throw new IllegalArgumentException("❌ ERROR: Falta el idCocinero en el JSON.");
        }
        if (pedido.getRepartidor() == null || pedido.getRepartidor().getIdRepartidor() == null) {
            throw new IllegalArgumentException("❌ ERROR: Falta el idRepartidor en el JSON.");
        }

        // 2. Buscamos los objetos REALES en la base de datos
        // Esto es VITAL para que el servicio de correos pueda leer el "email" real del cliente
        Cliente clienteReal = entityManager.find(Cliente.class, pedido.getCliente().getIdCliente());
        Cocinero cocineroReal = entityManager.find(Cocinero.class, pedido.getCocinero().getIdCocinero());
        Repartidor repartidorReal = entityManager.find(Repartidor.class, pedido.getRepartidor().getIdRepartidor());

        if (clienteReal == null) {
            throw new RuntimeException("El cliente no existe en la base de datos.");
        }

        // 3. Se los asignamos al pedido
        pedido.setCliente(clienteReal);
        pedido.setCocinero(cocineroReal);
        pedido.setRepartidor(repartidorReal);

        // 4. Guardamos el pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // ✉️ MAGIA DE CORREO
        try {
            correoService.enviarConfirmacionPedido(pedidoGuardado);
            System.out.println("✅ Correo de confirmación enviado exitosamente.");
        } catch (Exception e) {
            System.err.println("❌ No se pudo enviar el correo de confirmación: " + e.getMessage());
        }

        return pedidoGuardado;
    }
    @Override
    @Transactional
    public Pedido update(Integer id, Pedido pedido) {
        Pedido existing = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));

        // 1. Guardamos el estado ANTERIOR para comparar
        String estadoRepartoAnterior = existing.getEstadoReparto();

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

        Pedido pedidoActualizado = pedidoRepository.save(existing);

        // ✉️ MAGIA DE CORREO: Evaluar si el estado cambió para notificar al cliente
        try {
            String estadoNuevo = pedidoActualizado.getEstadoReparto();

            if (estadoNuevo != null && !estadoNuevo.equals(estadoRepartoAnterior)) {
                if (estadoNuevo.equalsIgnoreCase("En camino")) {
                    correoService.enviarPedidoEnCamino(pedidoActualizado);
                    System.out.println("✅ Correo 'En Camino' enviado exitosamente.");
                } else if (estadoNuevo.equalsIgnoreCase("Entregado")) {
                    correoService.enviarPedidoEntregado(pedidoActualizado);
                    System.out.println("✅ Correo 'Entregado' enviado exitosamente.");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ No se pudo enviar el correo de actualización: " + e.getMessage());
        }

        return pedidoActualizado;
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