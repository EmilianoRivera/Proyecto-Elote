package org.eskilokos.eskilokos.features.Pedido.service.impl;

import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoRequestDTO;
import org.eskilokos.eskilokos.features.Pedido.repository.PedidoRepository;
import org.eskilokos.eskilokos.features.Pedido.service.PedidoService;
import org.eskilokos.eskilokos.features.mail.service.mailService;

// Imports de tus otros repositorios (Asegúrate de que estas rutas sean las correctas en tu proyecto)
import org.eskilokos.eskilokos.features.Cliente.repository.ClienteRepository;
import org.eskilokos.eskilokos.features.Personal.repository.CocineroRepository;
import org.eskilokos.eskilokos.features.Personal.repository.RepartidorRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final CocineroRepository cocineroRepository;
    private final RepartidorRepository repartidorRepository;
    private final mailService emailService;

    // Constructor con todas las dependencias inyectadas de forma limpia
    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             ClienteRepository clienteRepository,
                             CocineroRepository cocineroRepository,
                             RepartidorRepository repartidorRepository,
                             mailService emailService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.cocineroRepository = cocineroRepository;
        this.repartidorRepository = repartidorRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> findById(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Override
    @Transactional
    public Pedido save(PedidoRequestDTO dto) {
        // 1. Buscamos y rehidratamos las entidades correspondientes de la BD
        org.eskilokos.eskilokos.core.entidades.Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + dto.getIdCliente()));

        org.eskilokos.eskilokos.core.entidades.Cocinero cocinero = cocineroRepository.findById(dto.getNumCocineros())
                .orElseThrow(() -> new RuntimeException("Personal de cocina no encontrado"));

        org.eskilokos.eskilokos.core.entidades.Repartidor repartidor = repartidorRepository.findById(dto.getIdRepartidor())
                .orElseThrow(() -> new RuntimeException("Repartidor no encontrado")); // Corregido el paréntesis que faltaba

        // 2. Instanciamos el Pedido usando la entidad del Core (gracias al import de la línea 3)
        Pedido pedido = new Pedido();
        pedido.setCosto(dto.getCosto());
        pedido.setEstadoAtencion("Recibido");
        pedido.setEstadoReparto("RECIBIDO");

        pedido.setCliente(cliente);
        pedido.setCocinero(cocinero);
        pedido.setRepartidor(repartidor);

        // 3. Persistimos en YugabyteDB
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 4. Enviamos la notificación por correo usando la instancia local inyectada 'emailService'
        try {
            emailService.enviarNotificacionFase(pedidoGuardado);
            System.out.println("✅ Notificación de correo enviada de forma transparente.");
        } catch (Exception e) {
            System.err.println("⚠️ Pedido registrado en la base de datos, pero el correo falló: " + e.getMessage());
        }

        return pedidoGuardado;
    }

    @Override
    @Transactional
    public Pedido update(Integer id, org.eskilokos.eskilokos.features.Pedido.DTOs.PedidoContenidoUpdateDTO dto) {
        // 1. Buscamos el pedido existente
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        // Guardamos la fase que tenía antes para el detector de cambios
        String faseAnterior = pedidoExistente.getEstadoReparto();

        // 2. Actualizamos los estados que vienen del nuevo DTO de Swagger
        pedidoExistente.setEstadoReparto(dto.getEstadoReparto());

        // Si tu nuevo DTO solo maneja el estado de reparto, el costo y la atención se pueden quedar
        // con lo que ya tenían, o si deseas puedes añadir esos campos al PedidoContenidoUpdateDTO.

        // 3. Guardamos los cambios en YugabyteDB
        Pedido pedidoActualizado = pedidoRepository.save(pedidoExistente);

        // 4. El detector dispara el correo si la fase cambió (ej. de PREPARACION a EN_REPARTO)
        if (pedidoActualizado.getEstadoReparto() != null && !pedidoActualizado.getEstadoReparto().equalsIgnoreCase(faseAnterior)) {
            try {
                emailService.enviarNotificacionFase(pedidoActualizado);
                System.out.println("✅ ¡Cambio de fase detectado! Correo enviado al cliente.");
            } catch (Exception e) {
                System.err.println("⚠️ Error al enviar correo de actualización: " + e.getMessage());
            }
        }

        return pedidoActualizado;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Pedido agregarPlatillo(Integer idPedido, Integer idPlatillo, Integer cantidad) {
        return null; // Deja aquí tu lógica original para meter alimentos al pedido
    }

    @Override
    @Transactional
    public Pedido quitarPlatillo(Integer idPedido, Integer idPlatillo) {
        return null; // Deja aquí tu lógica original para remover alimentos
    }

    @Override
    @Transactional(readOnly = true)
    public List<org.eskilokos.eskilokos.core.entidades.PedidoContenido> getContenido(Integer idPedido) {
        return null; // Deja aquí tu lógica original para listar los elotes/esquites del pedido
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByClienteId(Integer idCliente) {
        return pedidoRepository.findByCliente_IdCliente(idCliente);
    }
}