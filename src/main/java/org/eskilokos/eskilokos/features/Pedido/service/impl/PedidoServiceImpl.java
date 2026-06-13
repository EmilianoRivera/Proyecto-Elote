package org.eskilokos.eskilokos.features.Pedido.service.impl;
import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoRequestDTO;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoUpdateDTO;
import org.eskilokos.eskilokos.features.Pedido.repository.PedidoRepository;
import org.eskilokos.eskilokos.features.Pedido.service.PedidoService;
import org.eskilokos.eskilokos.features.mail.service.mailService;

import org.eskilokos.eskilokos.features.Cliente.repository.ClienteRepository;
import org.eskilokos.eskilokos.features.Personal.repository.CocineroRepository;
import org.eskilokos.eskilokos.features.Personal.repository.RepartidorRepository;

// 👇 RUTAS 100% CORREGIDAS SEGÚN TU EXPLORADOR DE ARCHIVOS
import org.eskilokos.eskilokos.features.Pedido.repository.PedidoContenidoRepository;
import org.eskilokos.eskilokos.features.Platillo.repository.PlatilloRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final CocineroRepository cocineroRepository;
    private final RepartidorRepository repartidorRepository;
    private final mailService emailService;
    private final PedidoContenidoRepository pedidoContenidoRepository;
    private final PlatilloRepository platilloRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             ClienteRepository clienteRepository,
                             CocineroRepository cocineroRepository,
                             RepartidorRepository repartidorRepository,
                             mailService emailService,
                             PedidoContenidoRepository pedidoContenidoRepository,
                             PlatilloRepository platilloRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.cocineroRepository = cocineroRepository;
        this.repartidorRepository = repartidorRepository;
        this.emailService = emailService;
        this.pedidoContenidoRepository = pedidoContenidoRepository;
        this.platilloRepository = platilloRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findAll() { return pedidoRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> findById(Integer id) { return pedidoRepository.findById(id); }

    @Override
    @Transactional
    public Pedido save(PedidoRequestDTO dto) {
        org.eskilokos.eskilokos.core.entidades.Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        org.eskilokos.eskilokos.core.entidades.Cocinero cocinero = cocineroRepository.findById(dto.getNumCocineros())
                .orElseThrow(() -> new RuntimeException("Personal de cocina no encontrado"));
        org.eskilokos.eskilokos.core.entidades.Repartidor repartidor = repartidorRepository.findById(dto.getIdRepartidor())
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado"));

        Pedido pedido = new Pedido();
        // CORRECCIÓN BIGDECIMAL: Nace en 0 estrictamente con formato BigDecimal
        pedido.setCosto(BigDecimal.ZERO);
        pedido.setEstadoAtencion("Recibido");
        pedido.setEstadoReparto("RECIBIDO");
        pedido.setCliente(cliente);
        pedido.setCocinero(cocinero);
        pedido.setRepartidor(repartidor);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        try {
            emailService.enviarNotificacionFase(pedidoGuardado);
        } catch (Exception e) {
            System.err.println("⚠️ Error al despachar correo de bienvenida: " + e.getMessage());
        }

        return pedidoGuardado;
    }

    @Override
    @Transactional
    public Pedido update(Integer id, PedidoUpdateDTO dto) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        String faseAnterior = pedidoExistente.getEstadoReparto();

        if (dto.getEstadoAtencion() != null) pedidoExistente.setEstadoAtencion(dto.getEstadoAtencion());
        if (dto.getEstadoReparto() != null) pedidoExistente.setEstadoReparto(dto.getEstadoReparto().name());

        Pedido pedidoActualizado = pedidoRepository.save(pedidoExistente);

        if (pedidoActualizado.getEstadoReparto() != null && !pedidoActualizado.getEstadoReparto().equalsIgnoreCase(faseAnterior)) {
            try {
                emailService.enviarNotificacionFase(pedidoActualizado);
            } catch (Exception e) {
                System.err.println("⚠️ Alerta de correo fallida: " + e.getMessage());
            }
        }
        return pedidoActualizado;
    }

    @Override
    @Transactional
    public Pedido agregarPlatillo(Integer idPedido, Integer idPlatillo, Integer cantidad) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        org.eskilokos.eskilokos.core.entidades.Platillo platillo = platilloRepository.findById(idPlatillo)
                .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));

        List<org.eskilokos.eskilokos.core.entidades.PedidoContenido> listaCompleta = pedidoContenidoRepository.findByPedido_IdPedido(idPedido);

        org.eskilokos.eskilokos.core.entidades.PedidoContenido contenido = null;
        for (org.eskilokos.eskilokos.core.entidades.PedidoContenido item : listaCompleta) {
            if (item.getPlatillo().getIdPlatillo().equals(idPlatillo)) {
                contenido = item;
                break;
            }
        }

        if (contenido == null) {
            contenido = new org.eskilokos.eskilokos.core.entidades.PedidoContenido();
            contenido.setPedido(pedido);
            contenido.setPlatillo(platillo);
            listaCompleta.add(contenido);
        }

        contenido.setCantidad(cantidad);
        pedidoContenidoRepository.save(contenido);

        // CORRECCIÓN BIGDECIMAL: Matemáticas puras usando los métodos de la clase
        BigDecimal costoTotalCalculado = BigDecimal.ZERO;

        for (org.eskilokos.eskilokos.core.entidades.PedidoContenido item : listaCompleta) {
            // Convierte la cantidad (Integer) a BigDecimal
            BigDecimal cantidadDecimal = new BigDecimal(item.getCantidad());

            // Asumiendo que getPrecio() devuelve un Double o BigDecimal. Si es Double, pon BigDecimal.valueOf(item.getPlatillo().getPrecio())
            // Si getPrecio() ya es BigDecimal, lo usamos directo:
            BigDecimal precioPlatillo = item.getPlatillo().getPrecio();

            // Multiplica precio * cantidad y lo suma al total
            BigDecimal subtotal = precioPlatillo.multiply(cantidadDecimal);
            costoTotalCalculado = costoTotalCalculado.add(subtotal);
        }

        pedido.setCosto(costoTotalCalculado);
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido quitarPlatillo(Integer idPedido, Integer idPlatillo) {
        return pedidoRepository.findById(idPedido).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<org.eskilokos.eskilokos.core.entidades.PedidoContenido> getContenido(Integer idPedido) {
        return pedidoContenidoRepository.findByPedido_IdPedido(idPedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByClienteId(Integer idCliente) {
        return pedidoRepository.findByCliente_IdCliente(idCliente);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) { pedidoRepository.deleteById(id); }
}