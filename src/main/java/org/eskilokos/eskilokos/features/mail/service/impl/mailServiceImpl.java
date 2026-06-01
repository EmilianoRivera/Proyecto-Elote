package org.eskilokos.eskilokos.features.mail.service.impl;

import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.eskilokos.eskilokos.features.mail.service.mailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class mailServiceImpl implements mailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    public mailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private String resolverDestinatario(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new RuntimeException("El pedido #" + pedido.getIdPedido() + " no tiene cliente asignado.");
        }
        String email = pedido.getCliente().getEmail();
        if (email == null || email.isBlank()) {
            throw new RuntimeException(
                    "El cliente #" + pedido.getCliente().getIdCliente()
                            + " no tiene email registrado. Registra uno antes de procesar el pedido.");
        }
        return email;
    }

    private SimpleMailMessage construirMensaje(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        return mensaje;
    }

    @Override
    public void enviarConfirmacionPedido(Pedido pedido) {
        String destinatario = resolverDestinatario(pedido);

        String cuerpo = """
                ¡Hola %s! Tu pedido ha sido recibido con éxito.

                Número de pedido : #%d
                Total            : $%.2f
                Estado           : %s

                En breve nuestro equipo comenzará a prepararlo.
                ¡Gracias por elegirnos!

                — Eskilokos Restaurant
                """.formatted(
                pedido.getCliente().getNombre(),
                pedido.getIdPedido(),
                pedido.getCosto(),
                pedido.getEstadoAtencion());

        mailSender.send(construirMensaje(
                destinatario,
                "Pedido #" + pedido.getIdPedido() + " recibido",
                cuerpo));
    }

    @Override
    public void enviarPedidoEnCamino(Pedido pedido) {
        String destinatario = resolverDestinatario(pedido);

        String repartidor = (pedido.getRepartidor() != null
                && pedido.getRepartidor().getEmpleado() != null)
                        ? pedido.getRepartidor().getEmpleado().getNombre()
                        : "nuestro repartidor";

        String cuerpo = """
                ¡Hola %s! Tu pedido #%d ya está en camino.

                %s salió a entregarlo.
                Estado de reparto : %s

                Por favor ten listo el pago si es en efectivo.

                — Eskilokos Restaurant
                """.formatted(
                pedido.getCliente().getNombre(),
                pedido.getIdPedido(),
                repartidor,
                pedido.getEstadoReparto());

        mailSender.send(construirMensaje(
                destinatario,
                "Pedido #" + pedido.getIdPedido() + " en camino",
                cuerpo));
    }

    @Override
    public void enviarPedidoEntregado(Pedido pedido) {
        String destinatario = resolverDestinatario(pedido);

        String cuerpo = """
                ¡Hola %s! Tu pedido #%d fue entregado.

                Total cobrado : $%.2f
                Estado        : %s

                Esperamos que todo haya sido de tu agrado.
                Si tienes algún comentario, responde este correo.

                ¡Hasta la próxima!

                — Eskilokos Restaurant
                """.formatted(
                pedido.getCliente().getNombre(),
                pedido.getIdPedido(),
                pedido.getCosto(),
                pedido.getEstadoReparto());

        mailSender.send(construirMensaje(
                destinatario,
                "Pedido #" + pedido.getIdPedido() + " entregado",
                cuerpo));
    }
}