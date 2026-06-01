package org.eskilokos.eskilokos.features.Ticket.service.imlo;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.eskilokos.eskilokos.core.entidades.Pedido;
import org.eskilokos.eskilokos.core.entidades.PedidoContenido;
import org.eskilokos.eskilokos.features.Pedido.repository.PedidoRepository;
import org.eskilokos.eskilokos.features.Ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public byte[] generarTicket(Integer idPedido) throws IOException {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new FileNotFoundException("Pedido no encontrado: " + idPedido));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document documento = new Document(PageSize.A6);
            PdfWriter.getInstance(documento, out);
            documento.open();

            // Título
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph titulo = new Paragraph("ESKILOKOS", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            Font fuenteSubtitulo = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Paragraph subtitulo = new Paragraph("Ticket de Pedido", fuenteSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(subtitulo);

            documento.add(new Paragraph(" "));

            // Datos del pedido
            Font fuenteNormal = new Font(Font.FontFamily.HELVETICA, 10);
            documento.add(new Paragraph("Pedido #: " + pedido.getIdPedido(), fuenteNormal));

            if (pedido.getCliente() != null) {
                documento.add(new Paragraph("Cliente: " + pedido.getCliente().getNombre(), fuenteNormal));
                documento.add(new Paragraph("Teléfono: " + pedido.getCliente().getTelefono(), fuenteNormal));
            }

            documento.add(new Paragraph("Estado: " + pedido.getEstadoAtencion(), fuenteNormal));
            documento.add(new Paragraph(" "));

            // Tabla de platillos
            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{4f, 1.5f, 2f});

            Font fuenteEncabezado = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            tabla.addCell(celdaEncabezado("Platillo", fuenteEncabezado));
            tabla.addCell(celdaEncabezado("Cant.", fuenteEncabezado));
            tabla.addCell(celdaEncabezado("Precio", fuenteEncabezado));

            for (PedidoContenido contenido : pedido.getContenidos()) {
                tabla.addCell(new Phrase(contenido.getPlatillo().getNombre(), fuenteNormal));
                tabla.addCell(new Phrase(String.valueOf(contenido.getCantidad()), fuenteNormal));
                tabla.addCell(new Phrase("$" + contenido.getPlatillo().getPrecio(), fuenteNormal));
            }

            documento.add(tabla);
            documento.add(new Paragraph(" "));

            // Total
            Font fuenteTotal = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Paragraph total = new Paragraph("TOTAL: $" + pedido.getCosto(), fuenteTotal);
            total.setAlignment(Element.ALIGN_RIGHT);
            documento.add(total);

            documento.add(new Paragraph(" "));
            Paragraph gracias = new Paragraph("¡Gracias por tu pedido!", fuenteSubtitulo);
            gracias.setAlignment(Element.ALIGN_CENTER);
            documento.add(gracias);

            documento.close();
        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF: " + e.getMessage(), e);
        }

        return out.toByteArray();
    }

    private PdfPCell celdaEncabezado(String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setPadding(4);
        return celda;
    }
}