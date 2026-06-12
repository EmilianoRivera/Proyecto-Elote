package org.eskilokos.eskilokos.features.mail.service;

import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class mailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarNotificacionFase(Pedido pedido) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(pedido.getCliente().getEmail());

            String asunto = "Pedido #" + pedido.getIdPedido() + " recibido";
            String tituloHtml = "¡Tu pedido ha sido recibido con éxito! 🥞";
            String mensajeCuerpo = "En breve nuestro equipo comenzará a prepararlo en la cocina.";

            // Cambiamos el diseño del correo dinámicamente según la fase
            if (pedido.getEstadoReparto() != null) {
                switch (pedido.getEstadoReparto().toUpperCase()) {
                    case "RECIBIDO":
                        asunto = "Pedido #" + pedido.getIdPedido() + " recibido con éxito";
                        break;
                    case "PREPARACION":
                        asunto = "Tu orden #" + pedido.getIdPedido() + " está en la cocina";
                        tituloHtml = "¡Tu elote se está preparando! 🧑‍🍳";
                        mensajeCuerpo = "El chef ha tomado tu orden y ya está en el comal.";
                        break;
                    case "ESPERA_REPARTIDOR":
                        asunto = "Pedido #" + pedido.getIdPedido() + " listo para salir";
                        tituloHtml = "¡Tu orden está lista! 📦";
                        mensajeCuerpo = "La comida está empaquetada esperando al repartidor.";
                        break;
                    case "EN_REPARTO":
                        asunto = "El repartidor va en camino con tu Pedido #" + pedido.getIdPedido();
                        tituloHtml = "¡Tu pedido va en camino! 🛵";
                        mensajeCuerpo = "El repartidor ya va en ruta hacia tu ubicación.";
                        break;
                    case "ENTREGADO":
                        asunto = "¡Pedido #" + pedido.getIdPedido() + " entregado con éxito!";
                        tituloHtml = "¡Buen provecho! 🎉";
                        mensajeCuerpo = "Tu pedido ha sido entregado. ¡Que lo disfrutes!";
                        break;
                }
            }

            String htmlContent = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #eee; padding: 20px; border-radius: 8px;">
                    <h2 style="color: #e67e22; text-align: center;">%s</h2>
                    <hr style="border: 0; border-top: 1px solid #eee;">
                    <p>¡Hola <strong>%s</strong>!</p>
                    <p>%s</p>
                    <div style="background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0;">
                        <p style="margin: 5px 0;"><strong>Número de pedido:</strong> #%d</p>
                        <p style="margin: 5px 0;"><strong>Total de la compra:</strong> $%s</p>
                        <p style="margin: 5px 0;"><strong>Estatus:</strong> <span style="background-color: #e67e22; color: white; padding: 2px 8px; border-radius: 3px;">%s</span></p>
                    </div>
                    <p style="text-align: center; color: #777;">— Eskilokos Restaurant</p>
                </div>
                """.formatted(tituloHtml, pedido.getCliente().getNombre(), mensajeCuerpo, pedido.getIdPedido(), pedido.getCosto(), pedido.getEstadoReparto());

            helper.setSubject(asunto);
            helper.setText(htmlContent, true);

            // LINEA CORREGIDA: Solo se envía, no lleva un 'return' antes
            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar el correo: " + e.getMessage());
        }
    }
}