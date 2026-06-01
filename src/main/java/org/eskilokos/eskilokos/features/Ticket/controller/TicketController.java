package org.eskilokos.eskilokos.features.Ticket.controller;

import org.eskilokos.eskilokos.features.Ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/apiTicket/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/generar/{idPedido}")
    public ResponseEntity<byte[]> generarTicket(@PathVariable Integer idPedido) throws IOException {
        byte[] pdf = ticketService.generarTicket(idPedido);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ticket-pedido-" + idPedido + ".pdf");
        headers.setContentLength(pdf.length);

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}